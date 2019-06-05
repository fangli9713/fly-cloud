package com.fly.common.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ftp")
@Getter
@Setter
public class FtpProperties {

    private String host;

    private int port;

    private String userName;

    private String passWord;

    private int timeOut;


}
