package com.fly.web.service.impl;


import com.fly.common.dto.BaseResult;
import com.fly.web.service.TuRingRobotService;
import org.springframework.stereotype.Service;

@Service("tuRingRobotService")
public class TuRingRobotServiceImpl implements TuRingRobotService {

    public static final String apiUrl = "http://openapi.tuling123.com/openapi/api/v2";

    @Override
    public BaseResult talk(String word) {
       return null;

    }
}
