package com.fly.robot.init.netty;

import com.fly.robot.init.netty.dto.BaseMsgOuterClass;
import com.googlecode.protobuf.format.JsonFormat;
import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private int count = 0;
    @Override  
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        ChannelId id = incoming.id();
        String channelId = id.asLongText();
        NettyConfig.channelMap.put(channelId, ctx);
        System.out.println("[SERVER] - " + incoming.remoteAddress() + " 连接过来\n");  
          
    }  


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        NettyConfig.channelMap.remove(incoming.id());
        System.out.println("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
        ctx.close();
        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
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
        if (cause instanceof ReadTimeoutException) {
            // do something
            System.out.println("错误类型为：ReadTimeoutException");
        }
        Channel incoming = ctx.channel();
        System.out.println("ChatClient:" + incoming.remoteAddress() + "报错");
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        //……
        if(channel.isActive()) {
            ctx.close();
        }

        cause.printStackTrace();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj)
            throws Exception {
        //消息会在这个方法接收到，msg就是经过解码器解码后得到的消息，框架自动帮你做好了粘包拆包和解码的工作
        BaseMsgOuterClass.BaseMsg msg = (BaseMsgOuterClass.BaseMsg)obj;
        System.out.println("msg====="+ new JsonFormat().printToString(msg));
        try {
            SocketHandler.handle(ctx,msg);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(obj);
        }
    }
}
