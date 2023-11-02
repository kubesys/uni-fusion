package com.qnkj.common.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public class CustomView extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("视图名称")
    public String viewname = "默认";

    @ApiModelProperty("视图权限")
    public String authorize = "";

    @ApiModelProperty("排序字段")
    public String orderby = "";

    @ApiModelProperty("排序(A：升序，D：降序,A_N：数字升序，D_N：数字降序)")
    public String order = "";

    @ApiModelProperty("是否默认")
    public Boolean isdefault = true;

    @ApiModelProperty("私有用户")
    public String privateuser = "";

    @ApiModelProperty("字段列表")
    public List<String> columnlist = new ArrayList<>();

    public CustomView() {
        this.id = "";
    }

    public CustomView(Object content) {
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
     * 数据库表名
     */
    public CustomView modulename(String modulename){
        this.modulename = modulename;
        return this;
    }

    /**
     * 视图名称
     */
    public CustomView viewname(String viewname){
        this.viewname = viewname;
        return this;
    }

    /**
     * 视图授权
     */
    public CustomView authorize(String authorize){
        this.authorize = authorize;
        return this;
    }

    /**
     * 是否默认视图
     */
    public CustomView isdefault(Boolean isdefault){
        this.isdefault = isdefault;
        return this;
    }

    /**
     * 视图展示字段列表
     */
    public CustomView columnlist(List<String> columnlist){
        this.columnlist = columnlist;
        return this;
    }

    public CustomView orderby(String fieldname){
        this.orderby = fieldname;
        return this;
    }

    public CustomView order(String order){
        if (Arrays.asList("ASC","A","D","DESC","A_N","D_N").contains(order)) {
            this.order = order;
        }
        return this;
    }

    public CustomView privateuser(String privateuser) {
        this.privateuser = privateuser;
        return this;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new CustomView()");
        sb.append(".viewname(\"").append(Utils.replaceString(this.viewname)).append("\")");
        if (!this.isdefault) {
            sb.append(".isdefault(false)");
        }
        if (!this.orderby.isEmpty()) {
            sb.append(".orderby(\"").append(this.orderby.replaceAll("my.", "")).append("\")");
        }
        if (!this.order.isEmpty()) {
            sb.append(".order(\"").append(this.order).append("\")");
        }
        if (!this.columnlist.isEmpty()) {
            sb.append(".columnlist(Arrays.asList(\"").append(String.join("\",\"", this.columnlist)).append("\"))");
        } else {
            sb.append(".columnlist(Arrays.asList())");
        }
        return sb.toString();
    }
}
