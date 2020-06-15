package com.fly.web.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "core",path = "core",fallbackFactory = ServiceFallbackFactory.class)
public interface AshareFeignClient {


    @GetMapping("/user/add/{str}")
    String add(@PathVariable(value = "str") String str);
}
