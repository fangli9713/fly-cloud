package com.fly.common.config.mq.rabbitmq;

public interface RetryStrategy {
    boolean tryAgain(String uniId);
}
