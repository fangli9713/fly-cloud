package com.fly.gateway;


import com.fly.common.annotation.EnableCorsConfiguration;
import com.fly.common.annotation.EnableDruidSourceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
  * @description: 网关启动入口
  * @author fanglinan
  * @date 2019/5/28
  */

@SpringBootApplication
@ServletComponentScan
@EnableCaching
@EnableZuulProxy
@EnableDiscoveryClient
@EnableFeignClients
//@EnableOAuth2Sso
@RefreshScope
@Slf4j
@EnableCorsConfiguration
@EnableDruidSourceConfiguration
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
