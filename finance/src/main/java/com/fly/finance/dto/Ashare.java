package com.fly.finance.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Fangln on 2018/11/7.
 */
@Data
public class Ashare implements Serializable {


    private static final long serialVersionUID = 3063956448306021138L;

    private Integer id;
    private String code;
    private String alias;
    private String name;
}
