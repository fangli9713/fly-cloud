package com.fly.finance.service;

import com.fly.common.core.convert.DataResult;
import com.fly.finance.entity.AshareList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
public interface AshareListService extends IService<AshareList> {

    DataResult today();

    void pullData();
}
