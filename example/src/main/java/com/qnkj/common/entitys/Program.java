package com.qnkj.common.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;

/**
 * create by 徐雁
 * create date 2020/11/6
 */

public class Program extends BaseRecordConfig {
    @ApiModelProperty("组名")
    public String group = "";
    @ApiModelProperty("标签")
    public String label = "";
    @ApiModelProperty("图标")
    public String icon = "";
    @ApiModelProperty("排序")
    public int order = 0;
    @ApiModelProperty("是否内置")
    public Boolean builtin = false;
    @ApiModelProperty("类型")
    public String authorize = "";
    @ApiModelProperty("组主页")
    public String mainpage = "";

    public Program() {
        this.id = "";
    }

    public Program(Object content){
        this.id = "";
        if(Utils.isEmpty(content)) {
            return;
        }
        if(content instanceof Content) {
            this.fromContent(content);
        }else if(content instanceof HashMap){
            this.fromRequest(content);
        }
    }

    public Program group(String group){
        this.group = group;
        return this;
    }

    public Program label(String label){
        this.label = label;
        return this;
    }

    public Program icon(String icon){
        this.icon = icon;
        return this;
    }

    public Program order(Integer order){
        this.order = order;
        return this;
    }

    public Program authorize(String authorize){
        this.authorize = authorize;
        return this;
    }

    public Program builtin(Boolean builtin) {
        this.builtin = builtin;
        return this;
    }
    public Program mainpage(String mainpage) {
        this.mainpage = mainpage;
        return this;
    }


    public String toConfigString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BaseMenuConfig.addProgram(new Program()");
        sb.append(".group(\"").append(Utils.replaceString(this.group)).append("\")");
        sb.append(".label(\"").append(Utils.replaceString(this.label)).append("\")");
        sb.append(".icon(\"").append(Utils.replaceString(this.icon)).append("\")");
        if (this.order != 0) {
            sb.append(".order(").append(this.order).append(")");
        }
        if (this.builtin) {
            sb.append(".builtin(true)");
        }
        if (!this.authorize.isEmpty()) {
            sb.append(".authorize(\"").append(Utils.replaceString(this.authorize)).append("\")");
        }
        if (!this.mainpage.isEmpty()) {
            sb.append(".mainpage(\"").append(Utils.replaceString(this.mainpage)).append("\"))");
        } else {
            sb.append(")");
        }
        return sb.toString();
    }
}
