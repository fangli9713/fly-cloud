package com.fly.operate.config.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fangln
 * @description 用于swagger 配置
 * @date 2019/12/30 9:34
 */

@Profile({"dev", "test"})
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private static final String packge = "com.fly.operate.controller";

    @Bean
    public Docket createRestApi() {
        //添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Authorization").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        //添加head参数end
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //加了ApiOperation注解的类，生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //包下的类，生成接口文档
                .apis(RequestHandlerSelectors.basePackage(packge))
                .paths(PathSelectors.any())
                .build()
                //在header中加入token
                .globalOperationParameters(pars)
                ;

    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("韬客APP-API文档")
                //创建人
                // .contact(new Contact("", "xxx@qq.com", "xxx@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("</br>" +
                        "APP接口文档    " + "</br>")
                .build();
    }

}