package com.fly.finance.util;

import com.fly.common.netty.ChannelHandlerUtil;
import com.fly.common.netty.dto.BaseMsgOuterClass;
import com.fly.finance.service.NettyServiceHandler;
import com.googlecode.protobuf.format.JsonFormat;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ProtoBufServerHandler extends ChannelInboundHandlerAdapter {
    private int count = 0;
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
    public void channelRead(ChannelHandlerContext ctx, Object obj)
            throws Exception {
        //消息会在这个方法接收到，msg就是经过解码器解码后得到的消息，框架自动帮你做好了粘包拆包和解码的工作
        BaseMsgOuterClass.BaseMsg msg = (BaseMsgOuterClass.BaseMsg)obj;
        log.info("msg====="+ new JsonFormat().printToString(msg));
        try {
            NettyServiceHandler.getInstance().handle(ctx,msg);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(obj);
        }
    }
}
