package com.qnkj.core.base.modules.settings.operationlog.entity;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * create by 徐雁
 */
@Data
public class Operationlog extends BaseRecordConfig {
    @ApiModelProperty("操作用户")
    public String profileid;

    @ApiModelProperty("描述")
    public String description;

    @ApiModelProperty("方法名")
    public String method;

    @ApiModelProperty("访问路径")
    public String uri;

    @ApiModelProperty("日志类型")
    public String log_type;

    @ApiModelProperty("请求ip")
    public String ip;

    @ApiModelProperty("地址")
    public String address;

    @ApiModelProperty("浏览器")
    public String browser;

    @ApiModelProperty("请求耗时")
    public Long time;

    @ApiModelProperty("异常详细")
    public String exception_detail;

    public Operationlog() {
        this.id = "";
    }

    public Operationlog(String logType, Long time) {
        this.log_type = logType;
        this.time = time;
    }

    public Operationlog(Object content) {
        this.id = "";
        this.fromContent(content);
    }

}
