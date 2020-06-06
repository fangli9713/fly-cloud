package com.fly.operate.util;

import com.fly.operate.entity.AshareHistory;
import com.fly.operate.entity.AshareTransaction;
import com.fly.operate.mapper.AshareHistoryMapper;
import com.fly.operate.mapper.AshareTransactionMapper;
import com.fly.operate.service.AshareHistoryService;
import com.fly.operate.service.AshareTransactionService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;

/**
 * 依据PSY对股票进行买入/卖出的决策
 */
public class PSYDecision {

    @Resource
    AshareHistoryMapper ashareHistoryMapper;

    @Resource
    AshareHistoryService ashareHistoryService;

    @Resource
    AshareTransactionService ashareTransactionService;

    @Resource
    AshareTransactionMapper ashareTransactionMapper;

    /**
     * 买入决策
     * 根据当前的数据 判断前两天的psy，
     */
    public  void buyDecision(AshareHistory AshareHistory){
        if(AshareHistory.getPsy() == null || AshareHistory.getPsy().doubleValue()>25){
            return;
        }
        //查询前一天的psy值
        AshareHistory param = new AshareHistory();
        param.setCode(AshareHistory.getCode());
        Date date = AshareHistory.getDate();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DAY_OF_YEAR,-1);
        java.util.Date time = cl.getTime();
        param.setDate((Date) time);


        AshareHistory a = ashareHistoryService.selectOneByCodeAndDate(AshareHistory.getCode(),param.getDate());
        if(a == null){
            return;
        }
        if(a.getPsy() == null || a.getPsy().doubleValue()>25){
            return;
        }

        //查询该股是否已经进行过买入操作
        AshareTransaction transaction = new AshareTransaction();
        transaction.setCode(AshareHistory.getCode());
        transaction.setDate(AshareHistory.getDate());
        AshareTransaction ashareTransactions =ashareTransactionService.selectOneByCodeAndDate(AshareHistory.getCode(),AshareHistory.getDate());
        if(ashareTransactions != null){
            return;
        }
        //执行买入
        //买入的价格
        BigDecimal close = AshareHistory.getClose();
        AshareTransaction ashareTransaction = new AshareTransaction();
        ashareTransaction.setCode(AshareHistory.getCode());
        ashareTransaction.setDate(AshareHistory.getDate());
        ashareTransaction.setPrice(close);
        ashareTransaction.setType(1);//买入
        ashareTransactionMapper.insert(ashareTransaction);
    }



}
