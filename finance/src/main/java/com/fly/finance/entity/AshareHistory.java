package com.fly.finance.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.sql.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AshareHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 代码
     */
    private String code;

    /**
     * 代码(带字母)
     */
    private String alias;

    /**
     * 日期
     */
    private Date date;

    /**
     * 开盘价
     */
    private BigDecimal open;

    /**
     * 收盘价
     */
    private BigDecimal close;

    /**
     * 最高
     */
    private BigDecimal high;

    /**
     * 最低
     */
    private BigDecimal low;

    /**
     * 交易量
     */
    private BigDecimal volume;

    /**
     * psy类型
     */
    private String psyType;

    /**
     * psy值
     */
    private BigDecimal psy;

    /**
     * psyma值
     */
    private BigDecimal psyma;

    /**
     * 创建时间
     */
    private java.util.Date createTime;


}
