package com.fly.common.config.mq.rabbitmq;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created at 2018/12/06 by Yun
 */
public class DefaultRetryStrategy implements RetryStrategy {
    private final ConcurrentHashMap<String, Integer> counter = new ConcurrentHashMap<>();
    private final int threshold = 5;

    @Override
    public boolean tryAgain(String uniId) {
        Integer c = counter.get(uniId);
        if (c == null) {
            counter.putIfAbsent(uniId, new Integer(1));
            return true;
        } else if (c < threshold) {
            c++;
            counter.put(uniId, c);
            return true;
        }
        return false;
    }
}
