package com.fly.operate.service;

import com.fly.operate.entity.AshareTransaction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
public interface AshareTransactionService extends IService<AshareTransaction> {

    AshareTransaction selectOneByCodeAndDate(String code, Date date);
}
