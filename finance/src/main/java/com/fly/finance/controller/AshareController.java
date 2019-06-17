package com.fly.finance.controller;

import com.fly.common.core.convert.DataResult;
import com.fly.common.core.convert.DataResultBuild;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AshareController {

    @GetMapping(value = "/ashare/ext")
    public DataResult getExtInfoById() {
        return DataResultBuild.success();
    }
}
