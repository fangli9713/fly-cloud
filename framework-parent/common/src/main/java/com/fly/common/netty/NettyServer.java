package com.fly.common.netty;

import com.fly.common.netty.protobuf.ProtoBufChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class NettyServer {

    public static class single {
        final static NettyServer instance = new NettyServer();
    }

    public static NettyServer getInstance() {
        return single.instance;
    }

    private Channel channel;
    /**
     * 连接处理group
     */
    private final EventLoopGroup boss = new NioEventLoopGroup();
    //事件处理group
    private final EventLoopGroup worker = new NioEventLoopGroup();
    //@PostConstruct
    public void start(int port, ChannelHandler handler) {
        bind(port, handler);
    }

    @PreDestroy
    public void stop() throws Exception {
        log.info("PreDestroy stop netty");
        if (channel != null) {
            channel.close();
        }
        NettyConfig.group.close();
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

    private void bind(int serverPort, ChannelHandler handler) {
        //服务端要建立两个group，一个负责接收客户端的连接，一个负责处理数据传输
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 绑定处理group
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                //保持连接数
                .option(ChannelOption.SO_BACKLOG, 5120)
                //有数据立即发送
                .childOption(ChannelOption.TCP_NODELAY, true)
                //保持连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                //处理新连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        // 增加任务处理
                        ChannelPipeline p = sc.pipeline();
                        p.addLast(new ProtoBufChannelHandler());
                        //自定义消息处理器
                        p.addLast(handler);
                    }
                });

        //绑定端口，同步等待成功
        ChannelFuture future;
        try {
            future = bootstrap.bind(serverPort).sync();
            if (future.isSuccess()) {
                channel = future.channel();
                log.info("服务端开启成功");
            } else {
                log.info("服务端开启失败");
            }
            //等待服务监听端口关闭,就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅地退出，释放线程池资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
