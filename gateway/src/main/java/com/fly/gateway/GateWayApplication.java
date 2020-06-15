package com.fly.gateway;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
  * @description: 网关启动入口
  * @author fanglinan
  * @date 2019/5/28
  */

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class GateWayApplication implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String active;

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.error("gateway网关服务启动成功,当前运行环境-------->"+active);
    }
}
