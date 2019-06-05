package com.fly.finance;

import com.fly.common.annotation.EnableCorsConfiguration;
import com.fly.common.annotation.EnableDruidSourceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
//@EnableOAuth2Sso
@RefreshScope
@Slf4j
@EnableCorsConfiguration
@EnableDruidSourceConfiguration
public class FinanceApplication implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String active;

    @Override
    public void run(String... args) throws Exception {
        log.error("finance服务启动成功,当前运行环境-------->" + active);
    }
}
