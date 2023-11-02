package com.qnkj.common.entitys;

import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public class PickListEntity {
    public String id = "";

    public Integer deleted = 0;

    @ApiModelProperty("数据项字符串值")
    public String strval = "";

    @ApiModelProperty("数据项标签")
    public String label = "";

    @ApiModelProperty("数据项数值")
    public Integer intval = 0;

    @ApiModelProperty("数据项排序")
    public Integer sequence = 0;

    @ApiModelProperty("数据显示式样")
    public String styclass = "";

    public PickListEntity id(String id){
        this.id = id;
        return this;
    }
    public PickListEntity deleted(Integer del){
        this.deleted = del;
        return this;
    }

    /**
     * 数据项字符串值
     */
    public PickListEntity strval(String strval) {
        this.strval = strval;
        return this;
    }

    /**
     * 数据项标签
     */
    public PickListEntity label(String label) {
        this.label = label;
        return this;
    }

    /**
     * 数据项数值
     */
    public PickListEntity intval(Integer intval) {
        this.intval = intval;
        return this;
    }

    /**
     * 排序号
     */
    public PickListEntity sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    /**
     * 选项样式
     */
    public PickListEntity styclass(String styclass) {
        this.styclass = styclass;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new PickListEntity()");
        sb.append(".label(\"").append(Utils.replaceString(this.label)).append("\")");
        sb.append(".strval(\"").append(Utils.replaceString(this.strval)).append("\")");
        sb.append(".intval(").append(this.intval).append(")");
        if (this.sequence != 0) {
            sb.append(".sequence(").append(this.sequence).append(")");
        }
        if (!this.styclass.isEmpty()){
            sb.append(".styclass(\"").append(Utils.replaceString(this.styclass)).append("\")");
        }
        return sb.toString();
    }

    public List<Object> toList() {
        return Arrays.asList(this.strval,this.label,this.intval,this.styclass,this.sequence,this.id);
    }
}
