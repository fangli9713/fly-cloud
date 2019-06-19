package com.fly.common.annotation;


import com.fly.common.config.cors.CorsConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
  * @description: 开启允许跨域访问的设置
  * @author fanglinan
  * @date 2019/5/29
  */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({CorsConfig.class})
public @interface EnableCorsConfiguration {
}
