package com.fly.common.config.mq.rabbitmq;


import com.fly.common.core.convert.DataResult;

/**
 * Created at 2018/12/04 by Yun
 */
public interface MessageConsumer {
    DataResult<?> consume(Object message);
}
