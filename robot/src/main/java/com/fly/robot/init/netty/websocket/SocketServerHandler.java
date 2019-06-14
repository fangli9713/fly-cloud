package com.fly.robot.init.netty.websocket;

import com.fly.robot.init.netty.ChannelHandlerUtil;
import com.fly.robot.init.netty.NettyConfig;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
@Slf4j
public class SocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ChannelHandlerUtil.handlerAdded(ctx);
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ChannelHandlerUtil.handlerRemoved(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        super.channelActive(ctx);
        Channel incoming = ctx.channel();
        log.info("ChatClient:" + incoming.remoteAddress() + "上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        super.channelInactive(ctx);
        Channel incoming = ctx.channel();
        log.info("ChatClient:" + incoming.remoteAddress() + "掉线");
    }

    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
 
    /**
     * 工程出现异常的时候调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ChannelHandlerUtil.exceptionCaught(ctx,cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame obj) throws Exception {
        final String text = obj.text();
        log.info("收到客户端消息=="+text);
      if("heartbeat".equalsIgnoreCase(text)){
          SocketServiceHandler.writeChannel(ctx.channel(),0,"1","heart");
          return;
      }
      //调用机器人

        try {
        //    SocketHandler.handle(ctx,msg);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
          //  ReferenceCountUtil.release(obj);
        }
    }
}
