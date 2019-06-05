package com.fly.common.config.mq.rabbitmq;


import com.fly.common.config.mq.rabbitmq.model.RabbitMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created at 2018/12/04 by Yun
 */
public class MemoryMessageCache implements MessageCache {

    private Map<Object, RabbitMessage> cache = new ConcurrentSkipListMap<>();

    private Map<Object, RabbitMessage> failedMessageCache = new ConcurrentSkipListMap<>();

    @Override
    public void needConfirmMessage(RabbitMessage val) {
        cache.put(val.getId(), val);
    }

    @Override
    public RabbitMessage confirmedMessage(Object key) {
        return cache.remove(key);
    }

    @Override
    public void sendFailedMessage(RabbitMessage val) {
        failedMessageCache.put(val.getId(), val);
    }

    @Override
    public List<RabbitMessage> getNeedConfirmMessages() {
        List<RabbitMessage> l = new ArrayList<>();
        for (Map.Entry<Object, RabbitMessage> entry : cache.entrySet()) {
            l.add(entry.getValue());
        }
        return l;
    }

    @Override
    public List<RabbitMessage> getSendFailedMessages() {
        List<RabbitMessage> l = new ArrayList<>();
        for (Map.Entry<Object, RabbitMessage> entry : failedMessageCache.entrySet()) {
            l.add(entry.getValue());
        }
        return l;
    }
}
