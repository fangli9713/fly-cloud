package com.fly.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
  * @description:
  * @author fanglinan
  * @date 2019/5/29
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
