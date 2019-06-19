package com.fly.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: longhai
 * @CreateDate: 2018/11/22 9:46
 */
@Data
@Component
@ConfigurationProperties("asyncpool")
public class AsyncPoolProperties {
    /**
     * 核心线程数
     */
    private int corePoolSize = 1;
    /**
     * 最大线程数
     */
    private int maxPoolSize = 100;
    /**
     * 队列最大长度
     */
    private int queueCapacity = 100;
    /**
     * 线程池维护线程所允许的空闲时间
     */
    private int keepAliveSeconds = 60;
    /***/
    private String threadNamePrefix = "thread-";
    /***/
    private boolean allowCoreThreadTimeOut = false;


}
