package com.fly.robot.init.netty.client;

import com.fly.robot.init.netty.dto.BaseMsgOuterClass;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Fangln on 2019/1/30.
 */
public class NettyClient {

    protected final int PORT = 8031;

    private static class SingletonHolder {
        static final NettyClient instance = new NettyClient();
    }

    public static NettyClient getInstance() {
        return SingletonHolder.instance;
    }

    private EventLoopGroup group;
    private Bootstrap b;
    ChannelFuture cf;

    public NettyClient() {
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000).
                group(group).
                channel(NioSocketChannel.class).
                handler(new LoggingHandler(LogLevel.INFO)).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                //客户端业务处理类
                sc.pipeline().addLast(new NettyClientProtoBufInitializer());
            }
        });
    }

    ReentrantLock lock = new ReentrantLock();
    public synchronized void  connect() throws Exception{
        try {
            if(!lock.isLocked()){
                lock.lock();
                if(this.cf == null || !this.cf.channel().isActive()) {
                    this.cf = b.connect("192.168.3.45", PORT).sync();
                    System.out.println(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date()) + "远程服务器已经连接, 可以进行数据交换..");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public ChannelFuture getChannelFuture(){
        //如果管道没有被开启或者被关闭了，那么重连
        try {
            if(this.cf == null){
                this.connect();
            }
            if(!this.cf.channel().isActive()){
                this.connect();
            }
        }catch (Exception e){
            System.out.println("客户端建立连接失败--"+e.getMessage());
        }
        return this.cf;
    }

    public static void main(String[] args) {
        NettyClient client = NettyClient.getInstance();
        client.getChannelFuture();
        if(client.cf != null){
            BaseMsgOuterClass.BaseMsg.Builder builder = BaseMsgOuterClass.BaseMsg.newBuilder();
            builder.setToken("111111111");
            client.cf.channel().writeAndFlush(builder);
        }

    }
}
