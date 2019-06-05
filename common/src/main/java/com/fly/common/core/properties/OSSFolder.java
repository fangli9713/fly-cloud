package com.fly.common.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ailiyun.oss")
@Getter
@Setter
public class OSSFolder {

    private String folder;

    private String format;

    private String folderVideo;

}
