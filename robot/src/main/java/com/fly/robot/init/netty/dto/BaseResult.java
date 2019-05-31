package com.fly.robot.init.netty.dto;

import lombok.Data;

@Data
public class BaseResult {

    private Integer code;
    private String data;
    private String method;
    private String message;

}
