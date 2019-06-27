package com.fly.finance.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.common.core.convert.DataResult;
import com.fly.common.core.convert.DataResultBuild;
import com.fly.finance.entity.AshareList;
import com.fly.finance.service.AshareListService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AshareController {

    @GetMapping(value = "/ashare/ext")
    public DataResult getExtInfoById() {
        return DataResultBuild.success();
    }

    @Resource
    AshareListService ashareListService;

    @GetMapping("/list")
    public DataResult<List<AshareList>> list(String code,String name){
        QueryWrapper<AshareList> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(code));{
            wrapper.lambda().eq(AshareList::getCode,code);
        }
        if(!StringUtils.isEmpty(name)){
            wrapper.lambda().like(AshareList::getName,name);
        }
        final List<AshareList> list = ashareListService.list(wrapper);
        return DataResultBuild.success(list);
    }


    @PostMapping("/add")
    public DataResult add(AshareList vo){
        vo.getCode();
        QueryWrapper<AshareList> query = new QueryWrapper<>();
        query.lambda().eq(AshareList::getCode,vo.getCode());
        final int count = ashareListService.count(query);
        if(count>0)
            ashareListService.save(vo);
        return DataResultBuild.success();
    }
    @DeleteMapping("/{id}")
    public DataResult delete(@PathVariable(value = "id") Integer id) {
        ashareListService.removeById(id);
        return DataResultBuild.success();
    }

}
