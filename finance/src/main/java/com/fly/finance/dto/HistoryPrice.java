package com.fly.finance.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Fangln on 2018/11/7.
 */
@Data
public class HistoryPrice implements Serializable {

    private static final long serialVersionUID = -7067476107118259785L;
    private String code;
    private String alias;
    private Date date;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;
    private String psy_type;
    private BigDecimal psy;
    private BigDecimal psyma;
    private java.util.Date create_time;

    private Date date_gt;
}
