package com.fly.finance.task;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Fangln on 2018/11/7.
 */
@Configuration
public class DecisionQuartzConfig {

    @Bean
    public JobDetail teatQuartzDetail() {
        return JobBuilder.newJob(AshareSellDecisionTask.class).withIdentity("ashareDecisionTask").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(1000) //设置时间周期单位秒
            .repeatForever();
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 1 * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(teatQuartzDetail())
                .withIdentity("ashareDecisionTask")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
