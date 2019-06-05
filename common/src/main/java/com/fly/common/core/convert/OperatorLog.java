package com.fly.common.core.convert;

import lombok.Data;

@Data
public class OperatorLog {

    private String beanName;

    private String methodName;

    private String curUser;

    private String params;

    private String remoteAddr;

    private String uri;

    private long requestTime;

    private long beginTime;

    private String result;

}
