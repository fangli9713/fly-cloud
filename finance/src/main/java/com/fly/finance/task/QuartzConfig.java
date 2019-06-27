package com.fly.finance.task;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.finance.entity.AshareHistory;
import com.fly.finance.entity.AshareList;
import com.fly.finance.entity.AshareTransaction;
import com.fly.finance.mapper.AshareHistoryMapper;
import com.fly.finance.mapper.AshareListMapper;
import com.fly.finance.mapper.AshareTransactionMapper;
import com.fly.finance.service.AshareHistoryService;
import com.fly.finance.service.AshareTransactionService;
import com.fly.finance.util.PSYUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Fangln on 2018/11/7.
 */
@Component
@Configuration
@EnableScheduling
@Slf4j
public class QuartzConfig {

    @Resource
    AshareListMapper ashareListMapper;

    @Resource
    AshareHistoryMapper ashareHistoryMapper;

    @Resource
    AshareHistoryService ashareHistoryService;

    @Resource
    AshareTransactionService ashareTransactionService;

    @Resource
    AshareTransactionMapper ashareTransactionMapper;


    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0 0/10 * * * ?")
    protected void dayPrice() {
        final QueryWrapper<AshareList> wrapper = new QueryWrapper<>();
        final List<AshareList> ashareLists = ashareListMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(ashareLists)){
            return;
        }
        for (AshareList a :ashareLists){
            //先判断该票今天跑了没
            QueryWrapper<AshareList> query = new QueryWrapper<>();
            query.lambda().eq(AshareList::getCode,a.getCode());
            final Integer count = ashareListMapper.selectCount(query);
            if(count>0){
                continue;
            }
            final List<AshareHistory> psyAndMA = PSYUtil.getPSYAndMA(a.getCode());
            if(CollectionUtils.isEmpty(psyAndMA)){
                continue;
            }
            for (AshareHistory p:psyAndMA) {

                final AshareHistory historyPrices = ashareHistoryService.selectOneByCodeAndDate(p.getCode(),p.getDate());
                if(historyPrices != null)
                    continue;
                ashareHistoryMapper.insert(p);
            }

        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    protected void buyAshare() {
        System.out.println("进入AshareDecisionTask:");
        AshareHistory param = new AshareHistory();
        param.setPsy(new BigDecimal(16.7));
        QueryWrapper<AshareHistory> queryMapper = new QueryWrapper<>();
        queryMapper.lt("psy","16.7");
        List<AshareHistory> AshareHistorys1 = ashareHistoryMapper.selectList(queryMapper);
        System.out.println("有"+AshareHistorys1.size()+"条记录");
        for (AshareHistory a:AshareHistorys1){
            //查询前一天的psy值
            AshareHistory param1 = new AshareHistory();
            param1.setCode(a.getCode());
            Date date = a.getDate();
            java.util.Calendar cl = java.util.Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.DAY_OF_YEAR,-1);
            java.util.Date time = cl.getTime();
            param1.setDate(new Date(time.getTime()));

            AshareHistory h = ashareHistoryService.selectOneByCodeAndDate(a.getCode(),a.getDate());
            if(h != null){
                continue;
            }
            if(h.getPsy() == null || h.getPsy().doubleValue()>25){
                continue;
            }

            //查询该股是否已经进行过买入操作
            AshareTransaction t = ashareTransactionService.selectOneByCodeAndDate(a.getCode(),a.getDate());
            if(t != null){
                continue;
            }
            //执行买入
            //买入的价格
            BigDecimal close = a.getClose();
            AshareTransaction ashareTransaction = new AshareTransaction();
            ashareTransaction.setCode(a.getCode());
            ashareTransaction.setDate(a.getDate());
            ashareTransaction.setPrice(close);
            ashareTransaction.setType(1);//买入
            ashareTransactionMapper.insert(ashareTransaction);
            System.out.println("插入一条买入记录>>>>>>>>>>>>>>>>>"+ashareTransaction);

        }
        System.out.println("任务结束");
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    protected void winDay() {
        System.out.println("AshareSellDecisionTask:");
        Wrapper<AshareTransaction> query = new QueryWrapper<>();
        List<AshareTransaction> ashareTransactions = ashareTransactionMapper.selectList(query);
        System.out.println("有"+ashareTransactions.size()+"条AshareSellDecisionTask");

        for (AshareTransaction a:ashareTransactions){
            //查询购买后，收盘价高于今天最近的一天
            QueryWrapper<AshareHistory> hQuery = new QueryWrapper<>();
            hQuery.lambda().eq(AshareHistory::getCode,a.getCode()).ge(AshareHistory::getDate,a.getDate()).ge(AshareHistory::getClose,a.getPrice().multiply(new BigDecimal(1.1)));
            List<AshareHistory> AshareHistorys = ashareHistoryMapper.selectList(hQuery);
            if(com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(AshareHistorys)){
                continue;
            }
            AshareHistory AshareHistory = AshareHistorys.get(0);

            Date date = AshareHistory.getDate();
            int day = (int)((date.getTime() - a.getDate().getTime()) / (24 * 60 * 60 * 1000));
            a.setWinDay(day);
            ashareTransactionMapper.updateById(a);
        }

        System.out.println("任务结束");
    }

}
