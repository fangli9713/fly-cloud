package com.fly.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.common.core.convert.DataResult;
import com.fly.common.util.DateUtil;
import com.fly.common.util.redis.RedisUtil;
import com.fly.finance.entity.AshareHistory;
import com.fly.finance.entity.AshareList;
import com.fly.finance.mapper.AshareHistoryMapper;
import com.fly.finance.mapper.AshareListMapper;
import com.fly.finance.service.AshareListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.finance.util.PSYUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
@Service
public class AshareListServiceImpl extends ServiceImpl<AshareListMapper, AshareList> implements AshareListService {

    @Resource
    RedisUtil redisUtil;

    @Resource
    AshareListMapper ashareListMapper;

    @Resource
    AshareHistoryMapper ashareHistoryMapper;

    @Override
    public DataResult today() {
        return null;
    }

    @Override
    public void pullData() {
        final QueryWrapper<AshareList> wrapper = new QueryWrapper<>();
        final List<AshareList> ashareLists = ashareListMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(ashareLists)) {
            return;
        }
        for (AshareList a : ashareLists) {
            System.out.println(DateUtil.dateToString(new Date()));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final List<AshareHistory> psyAndMA = PSYUtil.getPSYAndMA(a.getCode());
            if (CollectionUtils.isEmpty(psyAndMA)) {
                continue;
            }
            System.out.println("redisUtil开始" + DateUtil.dateToString(new Date()));

            ashareHistoryMapper.insertOnDuplicate(psyAndMA);
            for (AshareHistory p : psyAndMA) {
//                redisUtil.setNX(p.getCode() + ":" + p.getDate(), JSON.toJSONString(p));
            }
            System.out.println("redisUtil结束" + DateUtil.dateToString(new Date()));

        }
    }
}
