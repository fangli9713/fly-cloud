package com.fly.robot.service.impl;


import com.alibaba.fastjson.JSON;
import com.fly.common.dto.BaseResult;
import com.fly.common.util.http.HttpUtil;
import com.fly.robot.service.TuRingRobotService;
import com.fly.robot.vo.RobotRequest;

public class TuRingRobotServiceImpl implements TuRingRobotService {

    public static final String apiUrl = "http://openapi.tuling123.com/openapi/api/v2";

    @Override
    public BaseResult talk(String word) {
        RobotRequest request = new RobotRequest();
        final RobotRequest.Perception perception = request.getPerception();
        perception.inputText.put("text",word);
        try {

            final String s = HttpUtil.doJsonRquest(apiUrl, null, JSON.toJSONString(request));

//            {"emotion":{"robotEmotion":{"a":0,"d":0,"emotionId":0,"p":0},"userEmotion":{"a":0,"d":0,"emotionId":21500,"p":0}},"intent":{"actionName":"","code":10004,"intentName":""},"results":[{"groupType":1,"resultType":"text","values":{"text":"特朗普是最爱发推特的总统。"}}]}
        }catch (Exception e){

        }
        return null;

    }
}
