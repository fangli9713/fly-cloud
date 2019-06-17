package com.fly.robot.controller;

import com.alibaba.fastjson.JSON;
import com.fly.common.core.convert.DataResult;
import com.fly.robot.service.remote.AshareFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AshareController {

    @Resource
    AshareFeignClient ashareFeignClient;

    @GetMapping("/get")
    public DataResult get(){

        final DataResult extInfoById = ashareFeignClient.getExtInfoById();
        System.out.println("extInfoById==" + JSON.toJSONString(extInfoById));
        return extInfoById;
    }
}
