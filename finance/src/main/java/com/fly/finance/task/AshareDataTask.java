package com.fly.finance.task;

import com.fly.finance.dao.AshareDao;
import com.fly.finance.dto.Ashare;
import com.fly.finance.dto.HistoryPrice;
import com.fly.finance.util.PSYUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Fangln on 2018/11/7.
 */
public class AshareDataTask extends QuartzJobBean {

    @Autowired
    private AshareDao ashareDao;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final List<Ashare> ashares = ashareDao.selectAshareList();
        if(CollectionUtils.isEmpty(ashares)){
            return;
        }
        for (Ashare a :ashares){
            final List<HistoryPrice> psyAndMA = PSYUtil.getPSYAndMA(a.getCode());
            if(CollectionUtils.isEmpty(psyAndMA)){
                continue;
            }
            for (HistoryPrice p:psyAndMA) {
                final List<HistoryPrice> historyPrices = ashareDao.selectAshareHistoryPriceList(p);
                if(!CollectionUtils.isEmpty(historyPrices))
                    continue;
                ashareDao.insertAshareHistoryPrice(p);
            }

        }
    }
}
