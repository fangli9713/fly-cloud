package com.fly.common.util.oss;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 郭勇
 * @time 2019/5/22 14:17
 * @description
 */
@Data
@ApiModel(value = "文件属性")
public class FileModel {

    @ApiModelProperty(value = "文件大小")
    private long fileSzie;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "文件夹路径")
    private String folder;

    @ApiModelProperty(value = "文件原始名称")
    private String originalFilename;

    @ApiModelProperty(value = "文件存储全路径")
    private String url;
}
