package com.fly.robot.init.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;

public class ChannelHandlerUtil {

    public static void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.channelMap.put(getChannelId(ctx), ctx);
        System.out.println("[CLIENT] - " +getChannelId(ctx) + "-"+ ctx.channel().remoteAddress() + " 连接过来\n");
    }

    public static void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        NettyConfig.channelMap.remove(getChannelId(ctx));
        System.out.println("[CLIENT] - " + getChannelId(ctx) + "-"+ incoming.remoteAddress() + " 离开\n");
        ctx.close();

    }


    public static void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            System.out.println("错误类型为：ReadTimeoutException");
        }
        Channel incoming = ctx.channel();
        System.out.println("ChatClient:" + incoming.remoteAddress() + "报错");
        Channel channel = ctx.channel();
        if(channel.isActive()) {
            ctx.close();
        }
        cause.printStackTrace();
    }

    public static String getChannelId(ChannelHandlerContext ctx){
        return ctx.channel().id().asLongText();
    }


}
