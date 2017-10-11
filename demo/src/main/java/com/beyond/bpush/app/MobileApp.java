package com.beyond.bpush.app;

import com.beyond.bpush.protobuf.PBAPNSEvent;
import com.beyond.bpush.protobuf.PBAPNSMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MobileApp {

    public static void main( String[] args )
    {
        new MobileApp().start();

        try {
            Thread.sleep(3600 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class MobileAppClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActive: " + ctx.channel());
            channels.addLast(ctx.channel());
        }

        protected void printMsg(ChannelHandlerContext ctx, Object msg){
            byte[] dd = (byte[])msg;
            System.out.println("message size: " + dd.length);
            try {
                PBAPNSMessage event = PBAPNSMessage.newBuilder().mergeFrom(dd).build();
                if (event != null && event.getAps() != null) {
                    System.out.println("channelRead: " + dd.length + " @ " + event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {

            System.out.println("channelRead: " + ctx.channel());

            printMsg(ctx, msg);

            ReferenceCountUtil.release(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            super.channelReadComplete(ctx);
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelInactive();
            System.out.println("channelInactive: " + ctx.channel());
        }
    }

    private final Bootstrap b = new Bootstrap(); // (1)
    private EventLoopGroup workerGroup;
    private ConcurrentLinkedDeque<Channel> channels = new ConcurrentLinkedDeque<Channel>();

    public void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        });

        thread.start();

        Thread pingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        ping();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        pingThread.start();

    }

    private void ping() throws IOException {
        int total = channels.size();
        if(total == 0){
            return;
        }
        Channel channel = channels.pop();
        PBAPNSEvent event = PBAPNSEvent.newBuilder()
                .setOp(PBAPNSEvent.Ops.Online_VALUE)
                .setAppKey("da6ae142e0e009149e4a365d")
                .setUserId("110")
                .setDeviceId("LL_WANG_DRIVE_DEVICE_04")
                .setToken("LL_WANG_TOKEN")
                .setTypeId(PBAPNSEvent.DeviceTypes.Android_VALUE).build();
        send(channel, event);
        channels.addLast(channel);
    }

    private void send(Channel c, PBAPNSEvent event) throws IOException {
        byte[] bytes = event.toByteArray();
        final ByteBuf data = c.config().getAllocator().buffer(bytes.length); // (2)
        data.writeBytes(bytes);
        ChannelFuture cf = c.writeAndFlush(data);
        cf.addListener(new GenericFutureListener<Future<? super Void>>() {

            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                System.out.println(future);
            }
        });

    }

    private void connect(){
        final int port = 9080;
        final int pool = 2;
        final String host = "172.16.100.130";

        workerGroup = new NioEventLoopGroup(pool);
        try {
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    pipeline.addLast("bytesDecoder",new ByteArrayDecoder());

                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(4, false));
                    pipeline.addLast("bytesEncoder", new ByteArrayEncoder());

                    pipeline.addLast("handler", new MobileAppClientHandler());
                }
            });

            final List<ChannelFuture> fs = new ArrayList<ChannelFuture>();
            // Start the client.
            for(int i=0; i<pool; i++){
                ChannelFuture f = b.connect(host, port); // (5)
                if(f.cause() != null){
                    f.cause().printStackTrace();
                    continue;
                }
                fs.add(f);
            }

            for (ChannelFuture f : fs){
                if (!f.isDone()) {
                    f.get();
                }
            }

            System.out.println("BPush server. connected.");

        } catch (Exception e){
            e.printStackTrace();
            workerGroup.shutdownGracefully();
        }
    }
}
