package com.fly.web.service.remote;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author fangln
 * @description 用于
 * @date 2020/6/13 13:44
 */
@Component
public class ServiceFallbackFactory implements FallbackFactory<AshareFeignClient> {
    @Override
    public AshareFeignClient create(Throwable throwable) {
        return str -> "服务器出故障了,请稍后再试.";
    }
}
