package com.fly.common.netty.websocket;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
  * @description: TODO
  * @author fanglinan
  * @date 2019/5/30
  */
public class SocketChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch){
        ChannelPipeline pipeline = ch.pipeline();
        // //此两项为添加心跳机制,60秒查看一次在线的客户端channel是否空闲
//        pipeline.addLast( new IdleStateHandler(10, 10, 10, TimeUnit.SECONDS));
        // 超时处理
//        pipeline.addLast(new SocketServerIdleStateTrigger());
        //超时handler
        pipeline.addLast(new ReadTimeoutHandler(60));
        //HttpServerCodec: 针对http协议进行编解码
        pipeline.addLast(new HttpServerCodec());
        //ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
        pipeline.addLast(new ChunkedWriteHandler());
        /**
         * 作用是将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse，那么具体的是什么
         * 取决于是请求还是响应, 该Handler必须放在HttpServerCodec后的后面
         */
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast( new SocketServerHandler());
        //用于处理websocket, /ws为访问websocket时的uri
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

    }
}
