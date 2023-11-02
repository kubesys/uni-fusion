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

public class ModuleMenu extends BaseRecordConfig {
    @ApiModelProperty("菜单组")
    public String program = "";
    @ApiModelProperty("父节点")
    public String parent = "";
    @ApiModelProperty("父节点ID")
    public String parentid = "";
    @ApiModelProperty("节点名")
    public String label = "";
    @ApiModelProperty("图标")
    public String icon = "";
    @ApiModelProperty("排序")
    public int order = 0;
    @ApiModelProperty("模块名称")
    public String modulename = "";
    @ApiModelProperty("模块数据库表名")
    public String tabname = "";
    @ApiModelProperty("模块菜单URL")
    public String url = "";
    @ApiModelProperty("是否内置")
    public Boolean builtin = false;

    public ModuleMenu() {
        this.id = "";
    }

    public ModuleMenu(Object obj) {
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

    public ModuleMenu modulename(String modulename) {
        this.modulename = modulename;
        return this;
    }

    public ModuleMenu tabname(String tabname) {
        this.tabname = tabname;
        return this;
    }

    public ModuleMenu program(String program) {
        this.program = program;
        return this;
    }

    public ModuleMenu parent(String parent) {
        this.parent = parent;
        return this;
    }

    public ModuleMenu label(String label) {
        this.label = label;
        return this;
    }

    public ModuleMenu icon(String icon) {
        this.icon = icon;
        return this;
    }

    public ModuleMenu order(Integer order) {
        this.order = order;
        return this;
    }

    public ModuleMenu url(String url) {
        this.url = url;
        return this;
    }

    public ModuleMenu builtin(Boolean builtin){
        this.builtin = builtin;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new ModuleMenu()");
        sb.append(".program(\"").append(Utils.replaceString(this.program)).append("\")");
        sb.append(".parent(\"").append(Utils.replaceString(this.parent)).append("\")");
        sb.append(".label(\"").append(Utils.replaceString(this.label)).append("\")");
        sb.append(".icon(\"").append(Utils.replaceString(this.icon)).append("\")");
        sb.append(".url(\"").append(Utils.replaceString(this.url)).append("\")");
        if (this.order != 0) {
            sb.append(".order(").append(this.order).append(")");
        }
        return sb.toString();
    }
}
