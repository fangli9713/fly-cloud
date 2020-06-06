package com.fly.web.vo;

import lombok.Data;

import java.util.List;

@Data
public class RobotResponse {

    private emotionGroup emotion;
    private Intent intent;
    private List<Result> results;



}
