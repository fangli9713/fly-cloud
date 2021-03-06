package com.fly.operate.task;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.common.util.ThreadUtil;
import com.fly.common.util.redis.RedisUtil;
import com.fly.operate.entity.AshareHistory;
import com.fly.operate.entity.AshareTransaction;
import com.fly.operate.mapper.AshareHistoryMapper;
import com.fly.operate.mapper.AshareTransactionMapper;
import com.fly.operate.service.AshareHistoryService;
import com.fly.operate.service.AshareListService;
import com.fly.operate.service.AshareTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    AshareHistoryMapper ashareHistoryMapper;

    @Resource
    AshareHistoryService ashareHistoryService;

    @Resource
    AshareTransactionService ashareTransactionService;

    @Resource
    AshareTransactionMapper ashareTransactionMapper;

    @Resource
    AshareListService ashareListService;


//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 0 0/6 * * ?")
    protected void dayPrice() {
        ashareListService.pullData();
    }

//    @Scheduled(cron = "0 0 0/6 * * ?")
    @Scheduled(cron = "0 0/5 * * * ?")
    protected void buyAshare() {
        System.out.println("进入AshareDecisionTask:");
        AshareHistory param = new AshareHistory();
        param.setPsy(new BigDecimal(16.7));
        QueryWrapper<AshareHistory> queryMapper = new QueryWrapper<>();
        queryMapper.le("psy","16.7");
        java.util.Calendar cl = java.util.Calendar.getInstance();
        cl.add(Calendar.DAY_OF_YEAR,-1);

        queryMapper.lambda().eq(AshareHistory::getDate,new Date(cl.getTime().getTime()));
        List<AshareHistory> AshareHistorys1 = ashareHistoryMapper.selectList(queryMapper);
        System.out.println("有"+AshareHistorys1.size()+"条记录");
        for (AshareHistory a:AshareHistorys1){
            //查询前一天的psy值
            AshareHistory param1 = new AshareHistory();
            param1.setCode(a.getCode());
            Date date = a.getDate();
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
