package com.fly.common.config.mq.rabbitmq.model;

/**
 * Created at 2018/12/05 by Yun
 */
public enum SenderType {
    DIRECT("direct"), FANOUT("fanout"), TOPIC("topic"), HEADERS("headers");

    private final String type;

    SenderType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
