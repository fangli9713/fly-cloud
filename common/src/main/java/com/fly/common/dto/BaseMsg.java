package com.fly.common.dto;

import lombok.Data;

@Data
public class BaseMsg {

    private String token;
    private String info;
    private String method;
    private Integer os;
}
