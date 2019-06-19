package com.fly.common.annotation;


import com.fly.common.config.datasource.DruidConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
  * @description: 统一配置数据源
  * @author fanglinan
  * @date 2019/5/30
  */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({DruidConfig.class})
public @interface EnableDruidSourceConfiguration {
}
