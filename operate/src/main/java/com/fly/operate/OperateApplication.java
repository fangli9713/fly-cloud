package com.fly.operate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
  * @description: TODO
  * @author fanglinan
  * @date 2019/6/6
  */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
@Slf4j
public class OperateApplication implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String active;

    public static void main(String[] args) {
        SpringApplication.run(OperateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("operate服务启动成功,当前运行环境-------->" + active);
    }
}
