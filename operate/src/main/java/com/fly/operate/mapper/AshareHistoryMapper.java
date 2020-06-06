package com.fly.operate.mapper;

import com.fly.operate.entity.AshareHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
@Mapper
public interface AshareHistoryMapper extends BaseMapper<AshareHistory> {

    int insertOnDuplicate(List<AshareHistory> list);

}
