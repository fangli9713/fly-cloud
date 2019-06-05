package com.fly.common.config.mq.rabbitmq;


import com.fly.common.core.convert.DataResult;

/**
 * Created at 2018/12/04 by Yun
 */
public interface MessageSender {
    DataResult<?> send(Object message);
}
