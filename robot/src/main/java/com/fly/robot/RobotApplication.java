package com.fly.robot;

import com.fly.common.annotation.EnableCorsConfiguration;
import com.fly.common.annotation.EnableDruidSourceConfiguration;
import com.fly.robot.init.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
  * @description: robot工程的启动入口
  * @author fanglinan
  * @date 2019/5/30
  */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableTransactionManagement
@RefreshScope
@EnableAsync
//@EnableCorsConfiguration
@EnableDruidSourceConfiguration
@Slf4j
public class RobotApplication implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(RobotApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        log.error("robot服务启动成功,当前运行环境-------->" + active);
        nettyServer.start();
    }
}
