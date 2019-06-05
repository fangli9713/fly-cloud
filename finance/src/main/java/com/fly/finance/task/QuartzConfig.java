package com.fly.finance.task;

import org.quartz.*;
import org.springframework.context.annotation.Bean;

/**
 * Created by Fangln on 2018/11/7.
 */
//@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail teatQuartzDetail() {
        return JobBuilder.newJob(AshareDataTask.class).withIdentity("ashareDataTask").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger() {
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//            .withIntervalInSeconds(2000) //设置时间周期单位秒
//            .repeatForever();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 1 * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(teatQuartzDetail())
                .withIdentity("ashareDataTask")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
