package com.fly.common.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig extends DefaultAutoRedisConfig {

    @Bean
    @Override
    public RedisTemplate defaultRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return super.defaultRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisUtil redisUtil(RedisTemplate defaultRedisTemplate) {
        return new RedisUtil(defaultRedisTemplate);
    }

}