package com.fly.finance.service.impl;

import com.fly.common.core.convert.DataResult;
import com.fly.finance.entity.AshareList;
import com.fly.finance.mapper.AshareListMapper;
import com.fly.finance.service.AshareListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
@Service
public class AshareListServiceImpl extends ServiceImpl<AshareListMapper, AshareList> implements AshareListService {

    @Override
    public DataResult today() {
        return null;
    }
}
