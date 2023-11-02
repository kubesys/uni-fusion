package com.qnkj.core.base.modules.lcdp.formdesign.entity;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;

/**
 * create by 徐雁
 * create date 2021/4/30
 */

public class FormDesign extends BaseRecordConfig {
    @ApiModelProperty("菜单组")
    public String program = "";
    @ApiModelProperty("模块组")
    public String parent = "";
    @ApiModelProperty("模块名称")
    public String modulename = "";
    @ApiModelProperty("模块标签")
    public String module = "";
    @ApiModelProperty("是否生成模块代码")
    public Boolean generate = false;

    public FormDesign() { this.id = ""; }

    public FormDesign(Object content) {
        this.id = "";
        if(content instanceof HashMap){
            this.fromRequest(content);
        }else if(content instanceof Content) {
            this.fromContent(content);
        }
    }
}
