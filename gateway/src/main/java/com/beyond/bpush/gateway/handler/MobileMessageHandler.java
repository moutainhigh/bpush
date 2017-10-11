package com.beyond.bpush.gateway.handler;

import com.beyond.bpush.core.MetricBuilder;
import com.beyond.bpush.core.entity.ClientStatus;
import com.beyond.bpush.core.service.ClientServiceImpl;
import com.beyond.bpush.gateway.Connection;
import com.beyond.bpush.gateway.keeper.ClientKeeper;
import com.beyond.bpush.gateway.keeper.ConnectionKeeper;
import com.beyond.bpush.protobuf.PBAPNSBody;
import com.beyond.bpush.protobuf.PBAPNSEvent;
import com.beyond.bpush.protobuf.PBAPNSMessage;
import com.beyond.bpush.protobuf.PBAPNSUserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by admin on 14-8-6.
 */
public class MobileMessageHandler extends ChannelInboundHandlerAdapter {

    public static final String MULTI_CLIENTS = "multi_clients";
    public static final String SYNC = "sync";
    protected static Logger logger = LoggerFactory.getLogger(MobileMessageHandler.class);

    public MobileMessageHandler(){

    }

    /**
     * 接收到新的连接
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        logger.info("channelActive: {}", ctx.channel().hashCode());
    }

    /**
     * 读取新消息 LengthFieldBasedFrameDecoder 自动解包
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (logger.isDebugEnabled()) {
            logger.info("channelRead: {}", ctx.channel().hashCode());
        }
        MetricBuilder.requestMeter.mark();

        final PBAPNSEvent pbapnsEvent;

        try {
            byte[] bytes = (byte[]) msg;
            pbapnsEvent = PBAPNSEvent.newBuilder().mergeFrom(bytes).build();
        } catch (Exception e) {
            logger.error("Invalid Data Package.", e);
            ctx.close();
            return;
        }

        ReferenceCountUtil.release(msg);

        if (logger.isDebugEnabled()){
            logger.debug("Got Message. {}", pbapnsEvent);
        }

        if (StringUtils.isEmpty(pbapnsEvent.getUserId()) || pbapnsEvent.getOp() <= 0){
            logger.error("Invalid Client!! so close connection!! ");
            ctx.close();
            return;
        }

        if (pbapnsEvent.getTypeId() == PBAPNSEvent.DeviceTypes.Android_VALUE){
            MetricBuilder.clientAndroidMeter.mark();
        }else{
            MetricBuilder.clientIOSMeter.mark();
        }

        if(pbapnsEvent.getOp() == PBAPNSEvent.Ops.Online_VALUE){
            Connection conn = buildConnection(ctx, pbapnsEvent);
            //记录客户端
            MessageHandlerPoolTasks.instance.getExecutor().submit(new OnNewlyAddThread(pbapnsEvent));
            ack(ctx, conn, pbapnsEvent, SYNC);

        }else if(pbapnsEvent.getOp() == PBAPNSEvent.Ops.KeepAlive_VALUE){
            //心跳
            Connection conn = buildConnection(ctx, pbapnsEvent);
            ack(ctx, conn, pbapnsEvent, SYNC);

        }else if(pbapnsEvent.getOp() == PBAPNSEvent.Ops.Sleep_VALUE){

            final Connection conn = buildConnection(ctx, pbapnsEvent);
            if (null != conn){
                conn.setStatusId(ClientStatus.Sleep);
            }

            MessageHandlerPoolTasks.instance.getExecutor().submit(new Runnable() {

                @Override
                public void run() {

                    if (logger.isDebugEnabled()) {
                        logger.debug("Client go to sleep and close connection. {}", pbapnsEvent);
                    }

                    ClientServiceImpl.instance.updateStatus(pbapnsEvent.getUserId(), ClientStatus.Sleep);

                }
            });

        }else if(pbapnsEvent.getOp() == PBAPNSEvent.Ops.Awake_VALUE){
            Connection conn = buildConnection(ctx, pbapnsEvent);

            if (logger.isDebugEnabled()) {
                logger.debug("Client awake and rebuild connection. {}", pbapnsEvent);
            }
            //记录客户端
            MessageHandlerPoolTasks.instance.getExecutor().submit(new OnNewlyAddThread(pbapnsEvent));
            //心跳
            ack(ctx, conn, pbapnsEvent, SYNC);

        }else if(pbapnsEvent.getOp() == PBAPNSEvent.Ops.PushAck_VALUE){
            //推送反馈
            if (pbapnsEvent.getRead() > 0){

                MessageHandlerPoolTasks.instance.getExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        ClientServiceImpl.instance.updateBadge(pbapnsEvent.getUserId(), pbapnsEvent.getRead() * -1);
                    }
                });

            }

            Connection conn = buildConnection(ctx, pbapnsEvent);
            ack(ctx, conn, pbapnsEvent, SYNC);

        }else if(pbapnsEvent.getOp() == PBAPNSEvent.Ops.Offline_VALUE) {
            //离线
            final Connection connection = ConnectionKeeper.remove(pbapnsEvent.getAppKey(), pbapnsEvent.getUserId());
            if (connection != null) {

                connection.close();

                if (logger.isDebugEnabled()) {
                    logger.debug("Client go offline and close connection. {}", pbapnsEvent);
                }

                MessageHandlerPoolTasks.instance.getExecutor().submit(new Runnable() {

                    @Override
                    public void run() {
                        ClientServiceImpl.instance.updateOfflineTs(pbapnsEvent.getUserId(), connection.getLastOpTime());
                    }

                });
            }

            ctx.close();

        }
    }

    private Connection buildConnection(ChannelHandlerContext ctx, PBAPNSEvent pbapnsEvent) {
        Connection conn = ConnectionKeeper.get(pbapnsEvent.getAppKey(), pbapnsEvent.getUserId());
        boolean newOne = true;
        if (null != conn){
            if (!conn.getDeviceId().equalsIgnoreCase(pbapnsEvent.getDeviceId())) {
                ConnectionKeeper.remove(pbapnsEvent.getAppKey(), pbapnsEvent.getUserId());
                //只有设备id不一样才算是重复登录
                logger.error("你已经在线了!. KickOff. current conn={}, conn={}", ctx, conn);
                ack(ctx, conn, pbapnsEvent, MULTI_CLIENTS);
                newOne = true;
            } else if (conn.getContext().channel().hashCode() != ctx.channel().hashCode()){
                InetSocketAddress ip = (InetSocketAddress)conn.getContext().channel().remoteAddress();
                InetSocketAddress ip2 = (InetSocketAddress)ctx.channel().remoteAddress();
                if (ip.getAddress().getHostAddress().equalsIgnoreCase(ip2.getAddress().getHostAddress())){
                    if (pbapnsEvent.getOp() == PBAPNSEvent.Ops.Online_VALUE){
                        // 新操作是登录, 就使用新的
                        logger.warn("Client is using same network. old={}, new={}", ip, ip);
                        ack(ctx, conn, pbapnsEvent, MULTI_CLIENTS);
                        newOne = true;
                    }else {
                        // 使用最新的
                        ConnectionKeeper.remove(pbapnsEvent.getAppKey(), pbapnsEvent.getUserId());
                        newOne = true;
                    }

                }else{
                     // 不是同一个IP的话，就使用新的
                    logger.warn("Client is using different channel. old={}, new={}", ip, ip2);
                    ConnectionKeeper.remove(pbapnsEvent.getAppKey(), pbapnsEvent.getUserId());
                    newOne = true;
                }

            }else{
                newOne = true;
                conn.setStatusId(ClientStatus.Online);
            }
        }

        if (newOne){
            logger.debug("Got Connection from. userId={}, ip={}",  pbapnsEvent.getUserId(), ctx.channel().remoteAddress());
            conn = new Connection(ctx);
            conn.setUserId(pbapnsEvent.getUserId());
            conn.setAppKey(pbapnsEvent.getAppKey());
            conn.setDeviceId(pbapnsEvent.getDeviceId());
            ConnectionKeeper.add(pbapnsEvent.getAppKey(), pbapnsEvent.getUserId(), conn);
        }

        return conn;
    }

    private void ack(final ChannelHandlerContext ctx, final Connection cc, final PBAPNSEvent event, final String result){
        if (cc==null || cc.getContext() == null){
            return;
        }

        PBAPNSMessage.Builder builder = PBAPNSMessage.newBuilder();
        builder.setAps(PBAPNSBody.newBuilder().setAlert("ack").setBadge(0));

        PBAPNSUserInfo.Builder infoBuilder = PBAPNSUserInfo.newBuilder().setKey("msg").setValue(result);
        builder.addUserInfo(infoBuilder);

        infoBuilder = PBAPNSUserInfo.newBuilder().setKey("kindId").setValue(SYNC);
        builder.addUserInfo(infoBuilder);

        byte[] bytes = builder.build().toByteArray();

        final ByteBuf data = ctx.alloc().buffer(bytes.length); // (2)
        data.writeBytes(bytes);

        final ChannelFuture cf = cc.getContext().writeAndFlush(data);
        cf.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(cf.cause() != null){
                    logger.error("Send Error.", cf.cause());
                    cc.close();
                }else{
                    logger.debug(String.format("Send Response %s to %s[%s][%s] Done", result, event.getUserId(), cc.getContext().channel().remoteAddress(), event.getOp()));
                }
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelReadComplete: {}", ctx.channel().hashCode());
        ctx.flush();
    }

    /**
     * 连接异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        lostConnection(ctx);
        logger.error("exceptionCaught: {}", ctx.channel().hashCode(), cause);
        ctx.close();
    }

    /**
     * 连接断开，移除连接影射，客户端发起重连
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
            throws Exception {
        logger.info("channelInactive: {}", ctx.channel().hashCode());
        lostConnection(ctx);
    }

    private void lostConnection(ChannelHandlerContext ctx) {
        logger.info("lost Connection: {}", ctx.channel());
        final Connection connection = ConnectionKeeper.get(ctx.channel().hashCode());
        if (null != connection){
            connection.close();
            ClientKeeper.remove(connection.getAppKey(), connection.getUserId());
            MessageHandlerPoolTasks.instance.getExecutor().submit(new Runnable() {
                @Override
                public void run() {
                    ClientServiceImpl.instance.updateStatus(connection.getUserId(), ClientStatus.Lost);
                }
            });

        }

        ConnectionKeeper.remove(ctx.channel().hashCode());
    }

}
