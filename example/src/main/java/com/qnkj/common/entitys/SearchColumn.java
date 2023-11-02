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

public class SearchColumn extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("查询字段名")
    public String fieldname = "";

    @ApiModelProperty("查询字段标签")
    public String fieldlabel = "";

    @ApiModelProperty("占用列表")
    public Integer colspan = 1;

    @ApiModelProperty("显示宽度")
    public Integer width = 0;

    @ApiModelProperty("输入提示")
    public String tip = "";

    @ApiModelProperty("附加信息")
    public String aux = "";

    @ApiModelProperty("是否新启行")
    public Boolean newline = false;

    @ApiModelProperty("日期类型时，是否显示快捷键")
    public Boolean quickbtn = false;

    @ApiModelProperty("查询类型")
    public String searchtype = "search_input";

    @ApiModelProperty("查询默认值")
    public Object searchvalue = "";

    @ApiModelProperty("排序")
    public int order = 0;

    public SearchColumn() {
        this.id = "";
    }

    public SearchColumn(Object content) {
        this.id = "";
        if(content instanceof Content) {
            this.fromContent(content);
        }else if(content instanceof HashMap) {
            this.fromRequest(content);
        }
    }

    /**
     * 数据表名
     */
    public SearchColumn modulename(String modulename) {
        this.modulename = modulename;
        return this;
    }

    /**
     * 用于查询的字段名称
     */
    public SearchColumn fieldname(String fieldname) {
        this.fieldname = fieldname;
        return this;
    }

    /**
     * 查询界面上显示的标签
     */
    public SearchColumn fieldlabel(String fieldlabel) {
        this.fieldlabel = fieldlabel;
        return this;
    }

    /**
     * 指定占用列数
     */
    public SearchColumn colspan(Integer colspan) {
        this.colspan = colspan;
        return this;
    }

    /**
     * 指定互动元素的宽度（px），无指定时采用系统默认设置
     */
    public SearchColumn width(Integer width) {
        this.width = width;
        return this;
    }

    /**
     * 查询输入框为空时提示消息
     */
    public SearchColumn tip(String tip) {
        this.tip = tip;
        return this;
    }

    /**
     * 指定互动元素后跟随的信息，如数字后面的单位
     */
    public SearchColumn aux(String aux) {
        this.aux = aux;
        return this;
    }

    /**
     * 指定下一个查询字段是否新行显示
     */
    public SearchColumn newline(Boolean newline) {
        this.newline = newline;
        return this;
    }

    /**
     * 当type=calendar时，是否显示快捷按钮
     */
    public SearchColumn quickbtn(Boolean quickbtn) {
        this.quickbtn = quickbtn;
        return this;
    }

    /**
     * 查询界面显示模式：calendar，multi_input，vague_input，input，search_input，text，multitree，radiotree，select
     */
    public SearchColumn searchtype(String searchtype) {
        this.searchtype = searchtype;
        return this;
    }

    public SearchColumn order(Integer order) {
        this.order = order;
        return this;
    }

    /**
     * 默认值
     */
    public SearchColumn searchvalue(Object searchvalue) {
        this.searchvalue = searchvalue;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new SearchColumn()");
        sb.append(".fieldname(\"").append(Utils.replaceString(this.fieldname)).append("\")");
        sb.append(".fieldlabel(\"").append(Utils.replaceString(this.fieldlabel)).append("\")");
        if(this.order >= 0){
            sb.append(".order(").append(this.order).append(")");
        }
        if (this.colspan != 1) {
            sb.append(".colspan(").append(this.colspan).append(")");
        }
        if (this.width != 0) {
            sb.append(".width(").append(this.width).append(")");
        }
        if (!this.tip.isEmpty()) {
            sb.append(".tip(\"").append(Utils.replaceString(this.tip)).append("\")");
        }
        if (!this.aux.isEmpty()) {
            sb.append(".aux(\"").append(Utils.replaceString(this.aux)).append("\")");
        }
        if (!this.searchvalue.toString().isEmpty()){
            sb.append(".searchvalue(\"").append(Utils.replaceString(this.searchvalue.toString())).append("\")");
        }
        if (this.newline) {
            sb.append(".newline(true)");
        }
        if (this.quickbtn) {
            sb.append(".quickbtn(true)");
        }
        if (this.searchtype.compareTo("search_input") != 0) {
            sb.append(".searchtype(\"").append(this.searchtype).append("\")");
        }
        return sb.toString();
    }
}
