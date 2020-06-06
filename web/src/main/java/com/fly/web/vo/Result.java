package com.fly.web.vo;

import lombok.Data;

import java.util.Map;

@Data
public class Result{
    private Integer groupType;
    private String resultType;
    private Map values;
}
