package com.fly.robot.init.netty.client;

import com.fly.robot.init.netty.dto.BaseMsgOuterClass;
import com.fly.robot.init.netty.dto.BaseResultOuterClass;
import com.googlecode.protobuf.format.JsonFormat;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
  * @description: 客户端处理类
  * @author fanglinan
  * @date 2019/5/30
  */
public class NettyClientProtoBufHandler  extends ChannelInboundHandlerAdapter {

    final private Random random = new Random();
    final private int baseRandom = 5;
    private  int retryNum = 1;
    private NettyClient client;
    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        super.channelActive(ctx);
       // this.channel = ctx.channel();
        //发送心跳包
        //ping();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //出现异常,启动重连
        ping();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                System.out.println(ctx.channel().id()+">>>sending heart beat to the server......");
                ctx.channel().writeAndFlush(NettyClientProtoBufHandler.heartMsg());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj)
            throws Exception {
        BaseResultOuterClass.BaseResult msg = ( BaseResultOuterClass.BaseResult)obj;
        //消息会在这个方法接收到，msg就是经过解码器解码后得到的消息，框架自动帮你做好了粘包拆包和解码的工作
        System.out.println("服务器返回的data数据==="+ new JsonFormat().printToString(msg));
        handle(ctx,msg);
    }

    private void ping() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(client == null){
                    client = NettyClient.getInstance();
                    try {
                        while (client.cf == null){
                            Thread.sleep(1000);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                channel = client.cf.channel();
                if (channel.isActive()) {
                    if(retryNum>1){
                        retryNum = 1;
                    }
                    timer.cancel();
                } else {
                    //状态为不活跃 将进行重连操作
                    client.getChannelFuture();
                    System.err.println(channel.id()+">>>与服务端的连接已断开,正在进行第"+retryNum+"次重连..");
                    retryNum ++;
                }
            }
        },5000,3000);
    }

    /**
     * 对服务端下发的数据进行处理的方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void handle(ChannelHandlerContext ctx, BaseResultOuterClass.BaseResult msg) throws Exception{
        System.out.println(new JsonFormat().printToString(msg));

        final String method = msg.getMethod();
        if(method.equals("heart") ){
            ctx.writeAndFlush(heartMsg());
        }
    }

    public static BaseMsgOuterClass.BaseMsg.Builder heartMsg(){
        BaseMsgOuterClass.BaseMsg.Builder baseMsg = BaseMsgOuterClass.BaseMsg.newBuilder();
        baseMsg.setMethod("heart");
        return baseMsg;
    }
}
