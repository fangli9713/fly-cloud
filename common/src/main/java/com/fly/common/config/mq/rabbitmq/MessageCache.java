package com.fly.common.config.mq.rabbitmq;


import com.fly.common.config.mq.rabbitmq.model.RabbitMessage;

import java.util.List;


/**
 * Created at 2018/12/04 by Yun
 */
public interface MessageCache {

    /**
     * 待确认消息
     *
     * @param val
     */
    void needConfirmMessage(RabbitMessage val);

    /**
     * 已经被确认过的消息
     *
     * @param key
     * @return
     */
    RabbitMessage confirmedMessage(Object key);

    /**
     * 发送失败的消息
     *
     * @param val
     */
    void sendFailedMessage(RabbitMessage val);

    List<RabbitMessage> getNeedConfirmMessages();

    List<RabbitMessage> getSendFailedMessages();
}
