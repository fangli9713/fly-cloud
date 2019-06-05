package com.fly.finance.util;

import com.fly.finance.dao.AshareDao;
import com.fly.finance.dto.AshareTransaction;
import com.fly.finance.dto.HistoryPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * 依据PSY对股票进行买入/卖出的决策
 */
public class PSYDecision {

    @Autowired
    private AshareDao ashareDao;


    /**
     * 买入决策
     * 根据当前的数据 判断前两天的psy，
     */
    public  void buyDecision(HistoryPrice historyPrice){
        if(historyPrice.getPsy() == null || historyPrice.getPsy().doubleValue()>25){
            return;
        }
        //查询前一天的psy值
        HistoryPrice param = new HistoryPrice();
        param.setCode(historyPrice.getCode());
        Date date = historyPrice.getDate();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DAY_OF_YEAR,-1);
        java.util.Date time = cl.getTime();
        param.setDate((Date) time);


        List<HistoryPrice> historyPrices = ashareDao.selectAshareHistoryPriceList(param);
        if(CollectionUtils.isEmpty(historyPrices)){
            return;
        }
        HistoryPrice historyPrice1 = historyPrices.get(0);
        if(historyPrice1.getPsy() == null || historyPrice1.getPsy().doubleValue()>25){
            return;
        }

        //查询该股是否已经进行过买入操作
        AshareTransaction transaction = new AshareTransaction();
        transaction.setCode(historyPrice.getCode());
        transaction.setDate(historyPrice.getDate());
        List<AshareTransaction> ashareTransactions = ashareDao.selectAshareTransactionList(transaction);
        if(!CollectionUtils.isEmpty(ashareTransactions)){
            return;
        }
        //执行买入
        //买入的价格
        BigDecimal close = historyPrice.getClose();
        AshareTransaction ashareTransaction = new AshareTransaction();
        ashareTransaction.setCode(historyPrice.getCode());
        ashareTransaction.setDate(historyPrice.getDate());
        ashareTransaction.setPrice(close);
        ashareTransaction.setType(1);//买入
        ashareDao.insertAshareTransaction(ashareTransaction);
    }



}
