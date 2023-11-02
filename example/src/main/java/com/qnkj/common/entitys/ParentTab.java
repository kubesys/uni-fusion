package com.qnkj.common.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public class ParentTab extends BaseRecordConfig {
    @ApiModelProperty("菜单组名")
    public String program = "";
    @ApiModelProperty("菜单组ID")
    public String programid = "";
    @ApiModelProperty("父菜单名")
    public String name = "";
    @ApiModelProperty("父菜单标签")
    public String label = "";
    @ApiModelProperty("图标")
    public String icon = "";
    @ApiModelProperty("排序")
    public int order = 0;
    @ApiModelProperty("是否内置")
    public Boolean builtin = false;

    public ParentTab() {
        this.id = "";
    }

    public ParentTab(Object obj) {
        this.id = "";
        if(Utils.isEmpty(obj)) {
            return;
        }
        if(obj instanceof Content) {
            this.fromContent(obj);
        }else if(obj instanceof HashMap){
            this.fromRequest(obj);
        }
    }

    public ParentTab program(String program) {
        this.program = program;
        return this;
    }

    public ParentTab name(String name) {
        this.name = name;
        return this;
    }

    public ParentTab label(String label) {
        this.label = label;
        return this;
    }

    public ParentTab icon(String icon) {
        this.icon = icon;
        return this;
    }

    public ParentTab order(Integer order) {
        this.order = order;
        return this;
    }

    public ParentTab builtin(Boolean builtin) {
        this.builtin = builtin;
        return this;
    }

    public String toConfigString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BaseMenuConfig.addParentMenu(new ParentTab()");
        sb.append(".program(\"").append(Utils.replaceString(this.program)).append("\")");
        sb.append(".name(\"").append(Utils.replaceString(this.name)).append("\")");
        sb.append(".label(\"").append(Utils.replaceString(this.label)).append("\")");
        sb.append(".icon(\"").append(Utils.replaceString(this.icon)).append("\")");
        if (this.builtin) {
            sb.append(".builtin(true)");
        }
        if (this.order != 0) {
            sb.append(".order(").append(this.order).append(")");
        }
        sb.append(")");
        return sb.toString();
    }
}
