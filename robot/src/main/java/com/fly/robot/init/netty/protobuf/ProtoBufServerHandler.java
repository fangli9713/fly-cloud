package com.fly.robot.init.netty.protobuf;

import com.fly.robot.init.netty.ChannelHandlerUtil;
import com.fly.robot.init.netty.NettyConfig;
import com.fly.robot.init.netty.dto.BaseMsgOuterClass;
import com.googlecode.protobuf.format.JsonFormat;
import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class ProtoBufServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(ProtoBufServerHandler.class);
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
        System.out.println("ChatClient:" + incoming.remoteAddress() + "上线");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        super.channelInactive(ctx);
        Channel incoming = ctx.channel();
        System.out.println("ChatClient:" + incoming.remoteAddress() + "掉线");


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
        System.out.println("msg====="+ new JsonFormat().printToString(msg));
        try {
            ProtoBufServiceHandler.handle(ctx,msg);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(obj);
        }
    }
}
