package com.fly.finance.controller;


import com.fly.common.core.convert.DataResult;
import com.fly.common.core.convert.DataResultBuild;
import com.fly.finance.service.AshareHistoryService;
import com.fly.finance.service.AshareListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author fanglinan
 * @since 2019-06-12
 */
@RestController
@RequestMapping("/api/data/public")
public class AshareHistoryController {

    @Resource
    AshareListService ashareListService;

    @GetMapping("/today")
    public DataResult today() {
        return ashareListService.today();
    }

    @GetMapping("/recommend/list")
    public DataResult recList() {
        return ashareListService.today();
    }


    @GetMapping("/pull")
    public DataResult pullData() {
        ashareListService.pullData();
        return DataResultBuild.success();
    }

}

