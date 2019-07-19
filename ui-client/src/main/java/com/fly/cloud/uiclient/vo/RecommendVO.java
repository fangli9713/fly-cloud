package com.fly.cloud.uiclient.vo;

import lombok.Data;

import java.sql.Date;

@Data
public class RecommendVO {

    private String code;
    private String name;
    private Date date;
    private String reason;

}
