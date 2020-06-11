package com.fly.web.controller;

import com.fly.common.core.convert.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "我的接口模块")
@RestController
public class AshareController {


    @GetMapping("/get")
    @ApiOperation(value = "根据ID查询User")
    public DataResult get(){

        return null;
    }
}
