package com.fly.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.finance.entity.AshareHistory;
import com.fly.finance.mapper.AshareHistoryMapper;
import com.fly.finance.service.AshareHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
@Service("ashareHistoryService")
public class AshareHistoryServiceImpl extends ServiceImpl<AshareHistoryMapper, AshareHistory> implements AshareHistoryService {

    @Resource AshareHistoryMapper ashareHistoryMapper;

    @Override
    public  AshareHistory selectOneByCodeAndDate(String code, Date date){
        final QueryWrapper<AshareHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AshareHistory::getCode,code).eq(AshareHistory::getDate,date);

        return ashareHistoryMapper.selectOne(queryWrapper);
    }
}
