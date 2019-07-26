package com.fly.cloud.uiclient.service.netty.client;

import com.alibaba.fastjson.JSON;
import com.fly.cloud.uiclient.service.MethodConstant;
import com.fly.cloud.uiclient.service.RootPanel;
import com.fly.cloud.uiclient.service.netty.dto.BaseMsgOuterClass;
import com.fly.cloud.uiclient.service.netty.dto.BaseResultOuterClass;
import com.fly.cloud.uiclient.vo.RecommendVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author fanglinan
 * @description: 客户端处理类
 * @date 2019/5/30
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    public static final String HEART_METHOD = "heart";
    final private Random random = new Random();
    final private int baseRandom = 5;
    private int retryNum = 1;
    private NettyClient client;
    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //出现异常,启动重连
        ping();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                // System.out.println(ctx.channel().id()+">>>sending heart beat to the server......");
                ctx.channel().writeAndFlush(heartMsg());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj)
            throws Exception {
        BaseResultOuterClass.BaseResult msg = (BaseResultOuterClass.BaseResult) obj;
        //消息会在这个方法接收到，msg就是经过解码器解码后得到的消息，框架自动帮你做好了粘包拆包和解码的工作
        handle(ctx, msg);
    }

    private void ping() {

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (client == null) {
                    client = NettyClient.getInstance();
                    try {
                        while (client.cf == null) {
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                channel = client.cf.channel();
                if (channel.isActive()) {
                    if (retryNum > 1) {
                        retryNum = 1;
                    }
                    timer.cancel();
                } else {
                    //状态为不活跃 将进行重连操作
                    client.getChannelFuture();
                    System.err.println(channel.id() + ">>>与服务端的连接已断开,正在进行第" + retryNum + "次重连..");
                    retryNum++;
                }
            }
        }, 5000, 3000);
    }

    /**
     * 对服务端下发的数据进行处理的方法
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void handle(ChannelHandlerContext ctx, BaseResultOuterClass.BaseResult msg) throws Exception {

        final String method = msg.getMethod();
        if (method.equals(HEART_METHOD)) {
            ctx.writeAndFlush(heartMsg());
        }
        final String data = msg.getData();
        if (method.equals(MethodConstant.RECOMMEND_TODAY)) {
            List<RecommendVO> list = JSON.parseArray(data, RecommendVO.class);
            RootPanel.getInstance().reloadPanel1(list);
        }
        //拿到数据后 的处理

    }

    public static BaseMsgOuterClass.BaseMsg.Builder heartMsg() {
        return build(null, null, HEART_METHOD, 1);
    }

    public static BaseMsgOuterClass.BaseMsg.Builder buildMethod(String method) {
        return build(null, null, method, 1);
    }

    public static BaseMsgOuterClass.BaseMsg.Builder build(String token, String info, String method, int os) {
        BaseMsgOuterClass.BaseMsg.Builder baseMsg = BaseMsgOuterClass.BaseMsg.newBuilder();
        if (null != token) {
            baseMsg.setToken(token);
        }
        if (null != info) {
            baseMsg.setInfo(info);
        }
        if (null != method) {
            baseMsg.setMethod(method);
        }
        baseMsg.setOs(os);
        return baseMsg;
    }
}
