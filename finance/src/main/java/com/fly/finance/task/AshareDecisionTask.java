package com.fly.finance.task;

import com.fly.finance.dao.AshareDao;
import com.fly.finance.dto.AshareTransaction;
import com.fly.finance.dto.HistoryPrice;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Fangln on 2018/11/7.
 */
public class AshareDecisionTask extends QuartzJobBean {

    @Autowired
    private AshareDao ashareDao;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("进入AshareDecisionTask:");
        HistoryPrice param = new HistoryPrice();
        param.setPsy(new BigDecimal(16.7));
        List<HistoryPrice> historyPrices1 = ashareDao.selectAshareHistoryPriceList(param);
        System.out.println("有"+historyPrices1.size()+"条记录");
        for (HistoryPrice historyPrice:historyPrices1){
            //查询前一天的psy值
            HistoryPrice param1 = new HistoryPrice();
            param1.setCode(historyPrice.getCode());
            Date date = historyPrice.getDate();
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.DAY_OF_YEAR,-1);
            java.util.Date time = cl.getTime();
            param1.setDate(new Date(time.getTime()));


            List<HistoryPrice> historyPrices = ashareDao.selectAshareHistoryPriceList(param1);
            if(CollectionUtils.isEmpty(historyPrices)){
                continue;
            }
            HistoryPrice historyPrice1 = historyPrices.get(0);
            if(historyPrice1.getPsy() == null || historyPrice1.getPsy().doubleValue()>25){
                continue;
            }

            //查询该股是否已经进行过买入操作
            AshareTransaction transaction = new AshareTransaction();
            transaction.setCode(historyPrice.getCode());
            transaction.setDate(historyPrice.getDate());
            List<AshareTransaction> ashareTransactions = ashareDao.selectAshareTransactionList(transaction);
            if(!CollectionUtils.isEmpty(ashareTransactions)){
                continue;
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
            System.out.println("插入一条买入记录>>>>>>>>>>>>>>>>>"+ashareTransaction);

        }
        System.out.println("任务结束");
    }
}
