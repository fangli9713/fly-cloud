package com.fly.robot.init.netty.dto;

import lombok.Data;

@Data
public class BaseMsg {

    private String token;
    private String info;
    private String method;
    private Integer os;
}
