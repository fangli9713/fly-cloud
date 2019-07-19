package com.fly.finance.service;

import com.fly.finance.entity.AshareRecommend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.finance.vo.RecommendVO;

import java.util.List;

/**
 * <p>
 * 股票推荐表 服务类
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-26
 */
public interface AshareRecommendService extends IService<AshareRecommend> {

    boolean recommendEachDay();

    List<RecommendVO> selectTodayRecommend();
}
