package com.fly.robot.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RobotRequest {

    private Integer reqType = 0;
    private Perception perception = new Perception();

    private Map<String,String> userInfo = new HashMap<>();

    public RobotRequest() {
        userInfo.put("apiKey","2439b272298e49ccbb085aeb1abc6529");
        userInfo.put("userId","1001");
    }

    static {

    }

    public static class Perception{
        public Map<String,Object> inputText = new HashMap<>();
    }

}
