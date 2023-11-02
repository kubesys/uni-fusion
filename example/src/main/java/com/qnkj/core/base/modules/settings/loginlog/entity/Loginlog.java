package com.qnkj.core.base.modules.settings.loginlog.entity;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

/**
 * create by 徐雁
 */
public class Loginlog extends BaseRecordConfig {
    @ApiModelProperty("账户")
    public String profileid = "";

    @ApiModelProperty("登录IP")
    public String ip  = "";

    @ApiModelProperty("登录时间")
    public String logintime = "";

    @ApiModelProperty("登录地区")
    public String location = "";

    @ApiModelProperty("操作系统")
    public String system = "";

    @ApiModelProperty("浏览器")
    public String browser = "";

    public Loginlog() {
        this.id = "";
    }

    public Loginlog(Object content) {
        this.id = "";
        this.fromContent(content);
    }

}
