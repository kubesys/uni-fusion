package com.qnkj.core.plugins.feign.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author oldhand
 * @date 2019-12-16
 */
@Data
@ApiModel("Token")
public class TokenEntity {

    @ApiModelProperty("令牌")
    private String token;

    @ApiModelProperty("公钥")
    private String publicKey;

    @ApiModelProperty("格林威治(GMT)时间戳，北京时间减8小时")
    private long timestamp;

    public TokenEntity(String token,String publicKey,long timestamp) {
        this.token = token;
        this.publicKey = publicKey;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("token=").append(token).append(",");
        sb.append("publicKey=").append(publicKey).append(",");
        sb.append("timestamp=").append(timestamp);
        sb.append("}");
        return sb.toString();
    }
}
