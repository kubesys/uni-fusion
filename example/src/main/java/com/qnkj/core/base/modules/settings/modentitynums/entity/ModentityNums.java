package com.qnkj.core.base.modules.settings.modentitynums.entity;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

/**
 * create by 徐雁
 */
public class ModentityNums extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String semodule = "";

    @ApiModelProperty("使用前缀")
    public String prefix = "";

    @ApiModelProperty("是否包含日期")
    public String include_date = "";

    @ApiModelProperty("流水号长度")
    public int length = 3;

    @ApiModelProperty("当前编号")
    public int cur_id = 1;

    @ApiModelProperty("起始编号")
    public int start_id = 1;

    @ApiModelProperty("日期前缀")
    public String date = "";

    public ModentityNums() {
        this.id = "";
    }

    public ModentityNums(Object content) {
        this.id = "";
        this.fromContent(content);
    }
}
