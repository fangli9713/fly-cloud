package com.fly.finance.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class AshareTransaction {

    private Integer id  ;//                 int(11) not null auto_increment,
    private String code;//                 varchar(10),
    private Date date;//                 date,
    private Integer type;//                 tinyint(4),
    private BigDecimal price;  //              decimal(8,3),
    private Integer win_day;
    private java.util.Date create_time;//          datetime,
}
