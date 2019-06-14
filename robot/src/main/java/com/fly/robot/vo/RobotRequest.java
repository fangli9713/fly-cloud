package com.fly.robot.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RobotRequest {

    private Integer reqType = 0;
    private Perception perception;

    private static Map<String,String> userInfo = new HashMap<>();
    static {
        userInfo.put("apiKey","2439b272298e49ccbb085aeb1abc6529");
        userInfo.put("userId","1001");
    }

    public static class Perception{
        public Map<String,Object> inputText = new HashMap<>();
    }

}
