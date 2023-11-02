package com.qnkj.common.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;



public class Api extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("是否有ID查询接口")
    public Boolean isload = false;

    @ApiModelProperty("是否有新建接口")
    public Boolean iscreate = false;

    @ApiModelProperty("是否有删除接口")
    public Boolean isdelete = false;

    @ApiModelProperty("是否有更新接口")
    public Boolean isupdate = false;

    @ApiModelProperty("是否有单记录更新接口")
    public Boolean isupdaterecord = false;

    @ApiModelProperty("是否有列表查询接口")
    public Boolean islist = false;

    @ApiModelProperty("列表接口是否分页")
    public Boolean ispagelimit = false;

    @ApiModelProperty("新建字段列表")
    public List<String> createfields = new ArrayList<>();

    @ApiModelProperty("更新字段列表")
    public List<String> updatefields = new ArrayList<>();

    @ApiModelProperty("单记录更新字段列表")
    public List<String> updaterecordfields = new ArrayList<>();

    @ApiModelProperty("查询字段列表")
    public List<String> listfields = new ArrayList<>();

    @ApiModelProperty("筛选字段列表")
    public List<FilterField> filterfields = new ArrayList<>();

    public Api() {
        this.id = "";
    }

    public Api(Object content) {
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
     * modulename指明此模块的名字
     */
    public Api modulename(String modulename) {
        this.modulename = modulename;
        return this;
    }
    /**
     * 是否有ID查询接口
     */
    public Api isload(Boolean value) {
        this.isload = value;
        return this;
    }

    /**
     * 是否有新建接口
     */
    public Api iscreate(Boolean value) {
        this.iscreate = value;
        return this;
    }
    /**
     * 是否有删除接口
     */
    public Api isdelete(Boolean value) {
        this.isdelete = value;
        return this;
    }
    /**
     * 是否有更新接口
     */
    public Api isupdate(Boolean value) {
        this.isupdate = value;
        return this;
    }
    /**
     * 是否有单记录更新接口
     */
    public Api isupdaterecord(Boolean value) {
        this.isupdaterecord = value;
        return this;
    }
    /**
     * 是否有列表查询接口
     */
    public Api islist(Boolean value) {
        this.islist = value;
        return this;
    }
    /**
     * 列表接口是否分页
     */
    public Api ispagelimit(Boolean value) {
        this.ispagelimit = value;
        return this;
    }
    /**
     * 新建字段列表
     */
    public Api createfields(List<String> value) {
        this.createfields = value;
        return this;
    }
    /**
     * 更新字段列表
     */
    public Api updatefields(List<String> value) {
        this.updatefields = value;
        return this;
    }

    /**
     * 单记录更新字段列表
     */
    public Api updaterecordfields(List<String> value) {
        this.updaterecordfields = value;
        return this;
    }

    /**
     * 查询字段列表
     */
    public Api listfields(List<String> value) {
        this.listfields = value;
        return this;
    }
    /**
     * 筛选字段列表
     */
    public Api filterfields(List<FilterField> value) {
        this.filterfields = value;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("api");
        sb.append(".modulename(\"").append(Utils.replaceString(this.modulename)).append("\")");
        if(this.isload) {
            sb.append(".isload(true)");
        }
        if(this.iscreate) {
            sb.append(".iscreate(true)");
        }
        if(this.isdelete) {
            sb.append(".isdelete(true)");
        }
        if(this.isupdate) {
            sb.append(".isupdate(true)");
        }
        if(this.isupdaterecord) {
            sb.append(".isupdaterecord(true)");
        }
        if(this.islist) {
            sb.append(".islist(true)");
        }
        if(this.ispagelimit) {
            sb.append(".ispagelimit(true)");
        }
        if (this.createfields.size() > 0) {
            sb.append("\n\t\t\t.createfields(Arrays.asList(\"").append(String.join("\",\"", this.createfields)).append("\"))");
        }
        if (this.updatefields.size() > 0) {
            sb.append("\n\t\t\t.updatefields(Arrays.asList(\"").append(String.join("\",\"", this.updatefields)).append("\"))");
        }
        if (this.updaterecordfields.size() > 0) {
            sb.append("\n\t\t\t.updaterecordfields(Arrays.asList(\"").append(String.join("\",\"", this.updaterecordfields)).append("\"))");
        }
        if (this.listfields.size() > 0) {
            sb.append("\n\t\t\t.listfields(Arrays.asList(\"").append(String.join("\",\"", this.listfields)).append("\"))");
        }
        if (this.filterfields.size() > 0) {
            sb.append("\n\t\t\t.filterfields(Arrays.asList(");
            if (this.filterfields.size() == 1) {
                sb.append(this.filterfields.get(0).toString());
                sb.append("))");
            } else {
                sb.append("\n\t\t\t\t\t");
                sb.append(String.join(",\n\t\t\t\t\t", this.filterfields.stream().map( v -> v.toString() ).collect(Collectors.toList())));
                sb.append("\n\t\t\t))");
            }

        }
        return sb.toString();
    }
}
