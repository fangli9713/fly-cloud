package com.fly.common.config.mq.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Created at 2018/12/04 by Yun
 */
public class MessageAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    RabbitManager rabbitManager(@Autowired RabbitProperties properties, @Autowired MessageCache messageCache,
                                @Autowired RetryStrategy strategy) {
        return new RabbitManager(properties, messageCache, strategy);
    }

    @Bean
    @ConditionalOnMissingBean
    MessageCache messageCache() {
        return new MemoryMessageCache();
    }

    @Bean
    @ConditionalOnMissingBean
    RetryStrategy retryStrategy() {
        return new DefaultRetryStrategy();
    }
}
