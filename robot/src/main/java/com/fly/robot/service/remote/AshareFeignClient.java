package com.fly.robot.service.remote;

import com.fly.common.core.convert.DataResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "finance")
public interface AshareFeignClient {

    @GetMapping(value = "/finance/ashare/ext")
    DataResult getExtInfoById();
}
