package com.fly.common.config.cors;

import com.fly.common.config.property.CorsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
  * @description: 跨域访问的策略配置
  * @author fanglinan
  * @date 2019/5/29
  */
@Configuration
@EnableConfigurationProperties(CorsProperties.class)
@ConditionalOnProperty(name = "cors.enabled", havingValue = "true")
public class CorsConfig {
    @Resource
    CorsProperties corsProperties;

    private CorsConfiguration buildConfig() {
        List<String> allowAll = new ArrayList<>();
        allowAll.add("*");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1允许域名使用
        corsConfiguration.setAllowedOrigins(null == corsProperties.getAllowedOrigins() ? allowAll : corsProperties.getAllowedOrigins());
        // 2允许头
        corsConfiguration.setAllowedHeaders(null == corsProperties.getAllowedHeaders() ? allowAll : corsProperties.getAllowedHeaders());
        // 3允许HTTP方法（POST GET PUT DELETE方式）
        corsConfiguration.setAllowedMethods(null == corsProperties.getAllowedMethods() ? allowAll : corsProperties.getAllowedMethods());
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsProperties.getPath(), buildConfig());
        return new CorsFilter(source);
    }

}
