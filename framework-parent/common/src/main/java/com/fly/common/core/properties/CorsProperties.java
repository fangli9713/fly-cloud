package com.fly.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: longhai
 * @CreateDate: 2018/11/20 10:33
 */
@Data
@ConfigurationProperties("cors")
@Component
public class CorsProperties {
    /***/
    private List<String> allowedOrigins;
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private String path;
}
