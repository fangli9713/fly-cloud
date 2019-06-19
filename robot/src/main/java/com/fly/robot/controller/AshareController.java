package com.fly.robot.controller;

import com.alibaba.fastjson.JSON;
import com.fly.common.core.convert.DataResult;
import com.fly.robot.service.remote.AshareFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "我的接口模块")
@RestController
public class AshareController {

    @Resource
    AshareFeignClient ashareFeignClient;

    @GetMapping("/get")
    @ApiOperation(value = "根据ID查询User")
    public DataResult get(){

        final DataResult extInfoById = ashareFeignClient.getExtInfoById();
        System.out.println("extInfoById==" + JSON.toJSONString(extInfoById));
        return extInfoById;
    }
}
