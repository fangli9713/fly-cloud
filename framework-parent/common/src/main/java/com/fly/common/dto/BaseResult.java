package com.fly.common.dto;

import lombok.Data;

@Data
public class BaseResult<T> {

    private Integer code;
    private String data;
    private T method;
    private String message;

}
