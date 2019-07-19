package com.fly.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.finance.entity.AshareHistory;
import com.fly.finance.entity.AshareRecommend;
import com.fly.finance.mapper.AshareRecommendMapper;
import com.fly.finance.service.AshareHistoryService;
import com.fly.finance.service.AshareListService;
import com.fly.finance.service.AshareRecommendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.finance.vo.RecommendVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * 股票推荐表 服务实现类
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-26
 */
@Service
public class AshareRecommendServiceImpl extends ServiceImpl<AshareRecommendMapper, AshareRecommend> implements AshareRecommendService {

    @Resource
    AshareRecommendMapper ashareRecommendMapper;

    @Resource
    AshareListService ashareListService;
    @Resource
    AshareHistoryService ashareHistoryService;

    /**
     * 每日推荐任务
     *
     * @return
     */
    @Override
    public boolean recommendEachDay() {
        //确定推荐的算法逻辑 层层筛选数据
        //1:PSY 小于等于25
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        QueryWrapper<AshareHistory> wrapper = new QueryWrapper<>();
        wrapper.lambda().le(AshareHistory::getPsy, 25).eq(AshareHistory::getDate, new Date(calendar.getTimeInMillis()));
        final List<AshareHistory> list = ashareHistoryService.list(wrapper);

        //2:进一步根据其他条件进行筛选

        for (AshareHistory his : list) {
            try {
                AshareRecommend t = new AshareRecommend();
                t.setCode(his.getCode());
                t.setDate(new Date(System.currentTimeMillis()));

                QueryWrapper<AshareRecommend> query = new QueryWrapper<>();
                query.lambda().eq(AshareRecommend::getCode,his.getCode()).eq(AshareRecommend::getDate,t.getDate());
                if(ashareRecommendMapper.selectCount(query)>0){
                    continue;
                }
                ashareRecommendMapper.insert(t);
            }catch (Exception e){

            }
        }

        return true;
    }

    @Override
    public List<RecommendVO> selectTodayRecommend() {
        QueryWrapper<AshareRecommend> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AshareRecommend::getDate,new Date(System.currentTimeMillis()));

        final List<AshareRecommend> ashareRecommends = ashareRecommendMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(ashareRecommends)){
            return Lists.newArrayList();
        }
        List<RecommendVO> list = new ArrayList<>();
        for (AshareRecommend a:ashareRecommends) {
            final String code = a.getCode();
            RecommendVO vo = new RecommendVO();
            vo.setCode(code);
            vo.setDate(a.getDate());
            list.add(vo);
        }
        return list;
    }
}
