package com.fly.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.finance.entity.AshareTransaction;
import com.fly.finance.mapper.AshareTransactionMapper;
import com.fly.finance.service.AshareTransactionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
@Service
public class AshareTransactionServiceImpl extends ServiceImpl<AshareTransactionMapper, AshareTransaction> implements AshareTransactionService {

    @Resource
    AshareTransactionMapper ashareTransactionMapper;

    @Override
    public AshareTransaction selectOneByCodeAndDate(String code, Date date) {

        QueryWrapper<AshareTransaction> query = new QueryWrapper<>();
        query.lambda().eq(AshareTransaction::getCode,code).eq(AshareTransaction::getDate,date);
        return ashareTransactionMapper.selectOne(query);
    }
}
