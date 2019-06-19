package com.fly.common.config.oss;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.fly.common.core.properties.OSSFolder;
import com.fly.common.core.properties.OSSProperties;
import com.fly.common.util.oss.OSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OSSProperties.class, OSSFolder.class})
public class OSSConfig {

    @Autowired
    private OSSProperties properties;

    private ClientBuilderConfiguration conf;

    @Bean
    public ClientConfiguration clientConfiguration() {
        // 创建ClientConfiguration实例
        conf = new ClientBuilderConfiguration();

        // 配置代理为本地8080端口
        if (properties.getProxyHost() != null) {
            conf.setProxyHost(properties.getProxyHost());
            conf.setProxyPort(properties.getProxyPort());
        }
        if (properties.getUsername() != null) {
            // 设置用户名和密码
            conf.setProxyUsername(properties.getUsername());
            conf.setProxyPassword(properties.getPassword());
        }

        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(200);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(10000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(10000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(10000);
        // 设置最大的重试次数为3
        conf.setMaxErrorRetry(properties.getMaxErrorRetry() != 0 ? properties.getMaxErrorRetry() : 3);


        return conf;

    }

    public OSS OSSClient() {
        // 初始化一个OSSClient
        OSS ossClient = new OSSClientBuilder().build(properties.getEndpoint(), properties.getAccessKeyId(),
                properties.getAccessKeySecret(), conf);
        if (!ossClient.doesBucketExist(properties.getBucketName())) {
            ossClient.createBucket(properties.getBucketName());
        }
        return ossClient;

    }

    @Bean
    public OSSUtil ossUtil() {
        OSSUtil ossUtil = new OSSUtil();
        return ossUtil;
    }

}
