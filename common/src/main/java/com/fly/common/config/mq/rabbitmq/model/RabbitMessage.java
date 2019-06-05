package com.fly.common.config.mq.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created at 2018/12/04 by Yun
 */
@Data
@AllArgsConstructor
public class RabbitMessage implements Serializable {
    static final long serialVersionUID = 1L;
    private String id;
    private long time;
    private String exchange;
    private String routeKey;
    private Object message;
}
