package com.fly.robot.init.netty.client;

import com.fly.robot.init.netty.dto.BaseResultOuterClass;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fangln on 2019/1/31.
 */
public class NettyClientProtoBufInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast( new IdleStateHandler(3, 3, 3, TimeUnit.SECONDS));
        // ----Protobuf处理器，这里的配置是关键----
        // 用于decode前解决半包和粘包问题（利用包头中的包含数组长度来识别半包粘包）
        channelPipeline.addLast(new ProtobufVarint32FrameDecoder());
        //配置Protobuf解码处理器，消息接收到了就会自动解码，ProtobufDecoder是netty自带的，Message是自己定义的Protobuf类
        channelPipeline.addLast(
                new ProtobufDecoder(BaseResultOuterClass.BaseResult.getDefaultInstance()));
        // 用于在序列化的字节数组前加上一个简单的包头，只包含序列化的字节长度。
//        channelPipeline.addLast(
//                new ProtobufVarint32LengthFieldPrepender());
        //配置Protobuf编码器，发送的消息会先经过编码
        channelPipeline.addLast( new ProtobufEncoder());
        // ----Protobuf处理器END----
        //自己定义的消息处理器，接收消息会在这个类处理
        channelPipeline.addLast( new NettyClientProtoBufHandler());

    }
}
