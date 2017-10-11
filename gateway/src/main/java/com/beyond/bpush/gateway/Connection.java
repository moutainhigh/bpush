package com.beyond.bpush.gateway;

import com.beyond.bpush.core.entity.ClientStatus;
import com.beyond.bpush.core.entity.Payload;
import com.beyond.bpush.core.entity.PushStatus;
import com.beyond.bpush.core.service.ClientServiceImpl;
import com.beyond.bpush.core.service.PayloadServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by admin on 14-8-6.
 */
public class Connection {

    public static long epoch = 1420041600L;

    protected Logger logger = null;

    private final ChannelHandlerContext context;
    private String appKey;
    private String userId;
    private String deviceId;
    private int lastOpTime;
    private int statusId;
    private int deviceType;

    public Connection(ChannelHandlerContext context) {
        this.context = context;
        logger = LoggerFactory.getLogger(Connection.class.getName() + ".Channel." + context.channel().hashCode());
        lastOpTime = (int) (new Date().getTime() / 1000 - epoch);
        statusId = ClientStatus.Online;
    }

    public int getLastOpTime() {
        return lastOpTime;
    }

    /**
     * 更新最后发送时间
     */
    public void updateOpTime(){
        lastOpTime = (int) (new Date().getTime() / 1000 - epoch);
    }

    /**
     * 发送消息
     * @param message
     */
    public void send(final Payload message){
        // 组装消息包
        if(isWritable()){
            try {
                byte[] msg = message.asAPNSMessage().toByteArray();
                send(message, msg);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                message.setStatus(this.userId, new PushStatus(PushStatus.UnKnown, e.getMessage()));
            }
        }else{
            message.setStatus(this.userId, new PushStatus(PushStatus.ChannelClosed, null));
            logger.error("Send Error. Channel is closed. {}, {}", context, message);
        }
    }

    public boolean isWritable(){
        return context.channel().isWritable();
    }

    public boolean isOK(){
        return context.channel().isOpen() && context.channel().isWritable() && context.channel().isActive();
    }

    /**
     * 发送消息
     * @param message
     * @param msg
     */
    public void send(final Payload message, final byte[] msg) {
        try {

            final ByteBuf data = context.alloc().buffer(msg.length); // (2)
            data.writeBytes(msg);

            final ChannelFuture cf = context.writeAndFlush(data);
            cf.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if(cf.cause() != null){
                        logger.error("{}, Send Error.", context, cf.cause());
                        PayloadServiceImpl.instance.updateSendStatus(message, userId, new PushStatus(PushStatus.WriterError, cf.cause().getMessage()));
                    }else {
                        updateOpTime();
                        PayloadServiceImpl.instance.updateSendStatus(message, userId, new PushStatus(PushStatus.Success));
                        ClientServiceImpl.instance.updateBadge(userId, 1);
                        if (logger.isDebugEnabled()){
                            logger.debug("Send Done, userId={}, messageId={}", userId, message.getId());
                        }
                    }
                }
            });


        } catch (Exception e) {
            message.setStatus(userId, new PushStatus(PushStatus.UnKnown, e.getMessage()));
            logger.error(e.getMessage(), e);
        }
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void close(){
        context.close();
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    @Override
    public int hashCode() {
        return 31 * userId.hashCode() * deviceId.hashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Connection{");
        sb.append("appKey='").append(appKey).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", deviceId='").append(deviceId).append('\'');
        sb.append(", lastOpTime=").append(lastOpTime);
        sb.append(", statusId=").append(statusId);
        sb.append(", deviceType=").append(deviceType);
        sb.append('}');
        return sb.toString();
    }
}
