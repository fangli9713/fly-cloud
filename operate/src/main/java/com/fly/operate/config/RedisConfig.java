package com.fly.operate.config;

import com.fly.common.config.redis.DefaultAutoRedisConfig;
import com.fly.common.util.redis.RedisUtil;
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