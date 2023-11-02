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

public class OutsideLink extends BaseRecordConfig {
    @ApiModelProperty("当前模块名称")
    public String modulename = "";

    @ApiModelProperty("字段名称")
    public String fieldname = "";

    @ApiModelProperty("关联服务类型名")
    public String serviceclass = "";

    @ApiModelProperty("关联模块名称")
    public String relmodule = "";

    @ApiModelProperty("弹窗类型页面URL")
    public String url = "";

    @ApiModelProperty("提示消息")
    public String placeholder = "";

    public OutsideLink() {
        this.id = "";
    }

    public OutsideLink(Object content) {
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

    /**
     * 数据表名
     */
    public OutsideLink modulename(String modulename){
        this.modulename = modulename;
        return this;
    }

    /**
     * 数据表字段名
     */
    public OutsideLink fieldname(String fieldname){
        this.fieldname = fieldname;
        return this;
    }

    /**
     * 外部模块数据模型类名
     */
    public OutsideLink serviceclass(String serviceclass){
        this.serviceclass = serviceclass;
        return this;
    }

    /**
     * 外部模块名
     */
    public OutsideLink relmodule(String relmodule){
        this.relmodule = relmodule;
        return this;
    }

    /**
     * 外部模块找开的链接
     */
    public OutsideLink url(String url){
        this.url = url;
        return this;
    }

    /**
     * 本模块对此字段的提示信息
     */
    public OutsideLink placeholder(String placeholder){
        this.placeholder = placeholder;
        return this;
    }
    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(Boolean isClass) {
        StringBuilder sb = new StringBuilder();
        sb.append("new OutsideLink()");
        sb.append(".fieldname(\"").append(this.fieldname).append("\")");
        if(!this.relmodule.isEmpty()) {
            sb.append(".relmodule(\"").append(this.relmodule).append("\")");
        }
        if(!this.serviceclass.isEmpty()) {
            if (isClass) {
                String[] service = this.serviceclass.split("\\.");
                sb.append(".serviceclass(").append(service[service.length - 1]).append(".class.getName())");
            } else {
                sb.append(".serviceclass(\"").append(this.serviceclass).append("\")");
            }
        }
        if (!this.placeholder.isEmpty()) {
            sb.append(".placeholder(\"").append(Utils.replaceString(this.placeholder)).append("\")");
        }
        if (!this.url.isEmpty()) {
            sb.append(".url(\"").append(Utils.replaceString(this.url)).append("\")");
        }
        return sb.toString();
    }

    @Override
    public OutsideLink clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ignored) {}
        OutsideLink outsidelink = new OutsideLink();
        outsidelink.setValues(this);
        return outsidelink;
    }

    public void setValues(OutsideLink outsideLink) {
        super.setValues(outsideLink);
        modulename(outsideLink.modulename);
        fieldname(outsideLink.fieldname);
        serviceclass(outsideLink.serviceclass);
        relmodule(outsideLink.relmodule);
        url(outsideLink.url);
        placeholder(outsideLink.placeholder);
    }
}
