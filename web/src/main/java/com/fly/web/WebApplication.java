package com.fly.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
  * @description: robot工程的启动入口
  * @author fanglinan
  * @date 2019/5/30
  */

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class WebApplication implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String active;


    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("web服务启动成功,当前运行环境-------->" + active);
    }

//    @Bean
//    public ExitCodeGenerator exitCodeGenerator(){
//
//    }
}
