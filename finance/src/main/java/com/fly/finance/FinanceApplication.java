package com.fly.finance;

import com.fly.common.annotation.EnableCorsConfiguration;
import com.fly.common.annotation.EnableDruidSourceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
  * @description: TODO
  * @author fanglinan
  * @date 2019/6/6
  */
@SpringBootApplication
@EnableDiscoveryClient
//@MapperScan({"com.fly.finance"})
@EnableFeignClients
@EnableTransactionManagement
//@RefreshScope
@EnableAsync
@EnableCorsConfiguration
@Slf4j
public class FinanceApplication implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String active;

    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        log.error("finance服务启动成功,当前运行环境-------->" + active);
    }
}
