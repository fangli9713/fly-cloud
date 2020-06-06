package com.fly.operate.task;

import com.fly.operate.service.AshareRecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Configuration
@EnableScheduling
@Slf4j
public class RecommendTask {

    @Resource
    AshareRecommendService ashareRecommendService;

    /**
     * 每日计算推荐 集群的时候应考虑分布式锁
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    protected void dayPrice() {
        ashareRecommendService.recommendEachDay();
    }

}
