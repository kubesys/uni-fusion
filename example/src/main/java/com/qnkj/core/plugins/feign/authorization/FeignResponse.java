package com.qnkj.core.plugins.feign.authorization;

import com.alibaba.fastjson.JSONObject;
import com.qnkj.common.utils.DESedeUtil;
import com.qnkj.common.utils.MD5Util;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author oldhand
 * @date 2019-12-16
 */
@Data
@ApiModel("接口返回数据结构")
public class FeignResponse {
    @ApiModelProperty(value = "状态码：{200:成功,700:数据为空,401:认证错误,400:一般错误}",required = true)
    private Integer code = 0;
    @ApiModelProperty(value = "处理消息")
    private String message = "";
    @ApiModelProperty(value = "请求返回的数据")
    private Object body= "";
    @ApiModelProperty("请求处理时间")
    private Long timestamp = 0L;

    public FeignResponse(Map<String, Object> maps,String publicKey) {
        if (maps.containsKey("code") && Utils.isNotEmpty(maps.get("code"))) {
            this.code = Integer.parseInt(maps.get("code").toString());
        }
        if (maps.containsKey("timestamp") && Utils.isNotEmpty(maps.get("timestamp"))) {
            this.timestamp = Long.parseLong(maps.get("timestamp").toString());
        }
        if (maps.containsKey("message") && Utils.isNotEmpty(maps.get("message"))) {
            this.message = maps.get("message").toString();
        }
        if (maps.containsKey("body") && Utils.isNotEmpty(maps.get("body"))) {
            String encryptBody = maps.get("body").toString();
            if (FeignUtils.isBase64(encryptBody)) {
                String key = publicKey + String.valueOf(this.timestamp);
                String plainText = DESedeUtil.decrypt(encryptBody, MD5Util.get(key));
                if (plainText.startsWith("{") && plainText.endsWith("}")) {
                    this.body = JSONObject.parseObject(plainText, Map.class);
                } else if (plainText.startsWith("[") && plainText.endsWith("]")) {
                    this.body = JSONObject.parseObject(plainText, List.class);
                } else {
                    this.body = plainText;
                }
            } else {
                this.body = encryptBody;
            }
        }
    }

}
