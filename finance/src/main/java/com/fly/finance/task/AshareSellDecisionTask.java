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
import java.util.List;

/**
 * Created by Fangln on 2018/11/7.
 */
public class AshareSellDecisionTask extends QuartzJobBean {

    @Autowired
    private AshareDao ashareDao;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("AshareSellDecisionTask:");
        List<AshareTransaction> ashareTransactions = ashareDao.selectAshareTransactionList(new AshareTransaction());
        System.out.println("有"+ashareTransactions.size()+"条AshareSellDecisionTask");

        for (AshareTransaction a:ashareTransactions){
            //查询购买后，收盘价离于今天最近的一天
            HistoryPrice param = new HistoryPrice();
            param.setCode(a.getCode());
            param.setDate_gt(a.getDate());
            param.setClose(a.getPrice().multiply(new BigDecimal(1.1)));
            List<HistoryPrice> historyPrices = ashareDao.selectAshareHistoryPriceList(param);
            if(CollectionUtils.isEmpty(historyPrices)){
                continue;
            }
            HistoryPrice historyPrice = historyPrices.get(0);

            Date date = historyPrice.getDate();
            int day = (int)((date.getTime() - a.getDate().getTime()) / (24 * 60 * 60 * 1000));
            a.setWin_day(day);
            ashareDao.updateAshareTransaction(a);
        }

        System.out.println("任务结束");
    }
}
