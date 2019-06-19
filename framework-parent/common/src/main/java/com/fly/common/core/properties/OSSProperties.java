package com.fly.common.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "ailiyun.oss")
@Getter
@Setter
public class OSSProperties {
    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;

    private String username;

    private String password;

    private String proxyHost;

    private int proxyPort;

    private int maxConnections;

    private int connectionTimeout;

    private int maxErrorRetry;

    private int socketTimeout;

    private String bucketName;

}
