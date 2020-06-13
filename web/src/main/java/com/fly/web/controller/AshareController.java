package com.fly.web.controller;

import com.fly.common.core.convert.DataResult;
import com.fly.common.core.convert.DataResultBuild;
import com.fly.web.service.remote.AshareFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "我的接口模块")
@RestController
public class AshareController {

    @Autowired
    AshareFeignClient ashareFeignClient;

    @GetMapping("/get")
    @ApiOperation(value = "根据ID查询User")
    public DataResult get(){

        return DataResultBuild.success(ashareFeignClient.add("123"));
    }
}
