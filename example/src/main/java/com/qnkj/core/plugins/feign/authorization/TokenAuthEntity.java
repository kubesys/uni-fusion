package com.qnkj.core.plugins.feign.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author oldhand
 * @date 2019-12-16
 */
@Data
@ApiModel("Token认证请求参数")
public class TokenAuthEntity {

    @ApiModelProperty("应用名称")
    private String application;

    @ApiModelProperty("访问者ID")
    private String appid;

    @ApiModelProperty("密钥，32位字符串")
    private String secret;


    @ApiModelProperty("格林威治(GMT)时间戳，北京时间减8小时")
    private long timestamp;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("application=").append(application).append(",");
        sb.append("appid=").append(appid).append(",");
        sb.append("timestamp=").append(timestamp).append(",");
        sb.append("secret=").append("*****");
        sb.append("}");
        return sb.toString();
    }
}
