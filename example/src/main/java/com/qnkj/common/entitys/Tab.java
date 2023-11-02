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

public class Tab extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("模块标签")
    public String modulelabel = "";

    @ApiModelProperty("模块类型(0:标准模块，1:单页面，2:分类模块)")
    public Integer moduletype = 0;

    @ApiModelProperty("模块数据权限(0:全局可见，1:企业内部可见，2:指定授权可见)")
    public Integer datarole = 0;

    @ApiModelProperty("模块数据授权")
    public String dataauthorize = "";

    @ApiModelProperty("数据表名")
    public String tabname = "";

    @ApiModelProperty("数据类型，默认Content")
    public String datatype = "Content";

    @ApiModelProperty("列表是否显示操作图标")
    public Boolean hasoperator = true;

    @ApiModelProperty("数据查询默认区间")
    public String defaultsection = "ThisYear";

    @ApiModelProperty("数据查询界面列表")
    public Integer searchcolumn = 2;

    @ApiModelProperty("是否流布局,否则固定列布局")
    public Boolean searchcolumnflow = true;

    @ApiModelProperty("是否可新建")
    public Boolean iscreate = false;

    @ApiModelProperty("是否可删除")
    public Boolean isdelete = false;

    @ApiModelProperty("是否可导出")
    public Boolean isexport = false;

    @ApiModelProperty("是否可导入")
    public Boolean isimport = false;

    @ApiModelProperty("是否需审批")
    public Boolean isapproval = false;

    @ApiModelProperty("是否启用禁用")
    public Boolean isenabledisable = false;

    @ApiModelProperty("编辑页面是否只读")
    public Boolean isreadonly = false;

    @ApiModelProperty("记录专属")
    public Boolean isexclusive = false;

    @ApiModelProperty("自定义视图")
    public Boolean customview = true;

    @ApiModelProperty("列表分页")
    public Boolean pagelimit = true;

    @ApiModelProperty("自动行高")
    public Boolean autolineheight = false;

    @ApiModelProperty("是否入口隐藏")
    public Boolean ishidden = false;

    @ApiModelProperty("编辑页面按钮是否浮动")
    public Boolean editactionflow = false;

    @ApiModelProperty("必填项标签是左边还是右边")
    public Boolean requireleft = true;

    @ApiModelProperty("保存是否关闭编辑视图")
    public Boolean saveiscloseeditview = true;

    @ApiModelProperty("卡片式区块")
    public Boolean blockcard = true;

    @ApiModelProperty("是否可弹窗编辑")
    public Boolean popupeditview = false;

    @ApiModelProperty("是否可批量编辑")
    public Boolean batcheditview = false;

    @ApiModelProperty("是否行编辑")
    public Boolean islineedit = false;

    public Tab() {
        this.id = "";
    }

    public Tab(Object content) {
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
    public Tab modulename(String modulename) {
        this.modulename = modulename;
        return this;
    }

    /**
     * modulename指明此模块的标签(显示名称)
     */
    public Tab modulelabel(String modulelabel) {
        this.modulelabel = modulelabel;
        return this;
    }

    /**
     * moduletype 模块类型(0:标准模块，1:单页面，2:分类模块)
     */
    public Tab moduletype(Integer moduletype) {
        this.moduletype = moduletype;
        return this;
    }

    /**
     * 模块数据权限(0:全局可见，1:企业内部可见，2:指定角色可见)
     */
    public Tab datarole(Integer datarole) {
        this.datarole = datarole;
        return this;
    }

    /**
     * 模块数据授权
     */
    public Tab dataauthorize(String dataauthorize) {
        this.dataauthorize = dataauthorize;
        return this;
    }

    /**
     * tabName指明此模块的数据库表单的名字
     */
    public Tab tabname(String tabname) {
        this.tabname = tabname;
        return this;
    }

    /**
     * dataType指明此模块的数据保存模式;
     * 取值：Content常规模式；MainContent全局存储模式；BigContent：大字段存储模式，主要用于表在某些字段需要存储大量数据的类型（比如图片）；
     * YearContent按年存储模式；YearMonthContent按年和月存储模式；Mq消息队列存储模式，主要用于调用后台消息队列处理数据时使用；
     * Profile用户存储模式。
     */
    public Tab datatype(String datatype) {
        this.datatype = datatype;
        return this;
    }

    /***
     * hasOperator指明列表中的行是否有操作功能
     */
    public Tab hasoperator(Boolean hasoperator) {
        this.hasoperator = hasoperator;
        return this;
    }

    /**
     * defaultSection指明此模块在列表页面默认数据集区间；
     * 取值：ThisYear最近一年的数据；ThisQuater最近一季的数据；ThisMonth最近一月的数据；Recently：最近的数据
     */
    public Tab defaultsection(String defaultsection) {
        this.defaultsection = defaultsection;
        return this;
    }

    /**
     * 列表查询排列数
     */
    public Tab searchcolumn(Integer searchcolumn) {
        this.searchcolumn = searchcolumn;
        return this;
    }

    /**
     * 列表查询排列模式；默认流布局，false为固定布局
     */
    public Tab searchcolumnflow(Boolean searchcolumnflow) {
        this.searchcolumnflow = searchcolumnflow;
        return this;
    }

    /**
     * 模块是否可新建
     */
    public Tab iscreate(Boolean iscreate) {
        this.iscreate = iscreate;
        return this;
    }
    /**
     * 是否行编辑
     */
    public Tab islineedit(Boolean islineedit) {
        this.islineedit = islineedit;
        return this;
    }

    /**
     * 模块是否可删除
     */
    public Tab isdelete(Boolean isdelete) {
        this.isdelete = isdelete;
        return this;
    }

    /**
     * 模块是否可导入导出
     */
    public Tab isexport(Boolean isexport) {
        this.isexport = isexport;
        return this;
    }

    /**
     * 模块是否可导出
     */
    public Tab isimport(Boolean isimport) {
        this.isimport = isimport;
        return this;
    }

    /**
     * 模块是否需审批
     */
    public Tab isapproval(Boolean isapproval) {
        this.isapproval = isapproval;
        return this;
    }

    /**
     * 记录专属
     */
    public Tab isexclusive(Boolean isexclusive) {
        this.isexclusive = isexclusive;
        return this;
    }

    /**
     * 允许编辑视图
     */
    public Tab customview(Boolean customview) {
        this.customview = customview;
        return this;
    }

    /**
     * 列表分页
     */
    public Tab pagelimit(Boolean pagelimit) {
        this.pagelimit = pagelimit;
        return this;
    }


    /**
     *  自动行高
     */
    public Tab autolineheight(Boolean autolineheight) {
        this.autolineheight = autolineheight;
        return this;
    }

    /**
     * 入口隐藏
     */
    public Tab ishidden(Boolean ishidden) {
        this.ishidden = ishidden;
        return this;
    }

    /**
     * 模块是否启用禁用
     */
    public Tab isenabledisable(Boolean isenabledisable) {
        this.isenabledisable = isenabledisable;
        return this;
    }

    /**
     * 模块是否只读
     */
    public Tab isreadonly(Boolean isreadonly){
        this.isreadonly = isreadonly;
        return this;
    }

    /**
     * 编辑页面按钮是否浮动
     */
    public Tab editactionflow(Boolean editactionflow) {
        this.editactionflow = editactionflow;
        return this;
    }

    /**
     * 必填项标签在左边还是右边
     */
    public Tab requireleft(Boolean require) {
        this.requireleft = require;
        return this;
    }

    /**
     * 保存是否关闭编辑视图
     */
    public Tab saveiscloseeditview(Boolean require) {
        this.saveiscloseeditview = require;
        return this;
    }
    /**
     * 编辑页面区块是卡片式还是折叠式
     */
    public Tab blockcard(Boolean blockcard) {
        this.blockcard = blockcard;
        return this;
    }

    /**
     * 是否可以弹窗编辑
     */
    public Tab popupeditview(Boolean popupeditview) {
        this.popupeditview = popupeditview;
        return this;
    }

    /**
     * 是否可以批量编辑
     */
    public Tab batcheditview(Boolean batcheditview) {
        this.batcheditview = batcheditview;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new Tab()");
        sb.append(".modulename(\"").append(Utils.replaceString(this.modulename)).append("\")");
        sb.append(".modulelabel(\"").append(Utils.replaceString(this.modulelabel)).append("\")");
        if (!this.tabname.isEmpty()) {
            sb.append(".tabname(\"").append(Utils.replaceString(this.tabname)).append("\")");
        }
        if (this.moduletype != 0) {
            sb.append(".moduletype(").append(this.moduletype).append(")");
        }
        if (this.datarole != 0){
            sb.append(".datarole(").append(this.datarole).append(")");
        }
        if (!Utils.isEmpty(this.dataauthorize)){
            sb.append(".dataauthorize(\"").append(Utils.replaceString(this.dataauthorize)).append("\")");
        }
        if (!"Content".equals(this.datatype)) {
            sb.append(".datatype(\"").append(this.datatype).append("\")");
        }
        if (!this.hasoperator) {
            sb.append(".hasoperator(false)");
        }
        if (!"ThisYear".equals(this.defaultsection)) {
            sb.append(".defaultsection(\"").append(this.defaultsection).append("\")");
        }
        if (this.searchcolumn != 2) {
            sb.append(".searchcolumn(").append(this.searchcolumn).append(")");
        }
        if (this.iscreate) {
            sb.append(".iscreate(true)");
        }
        if (this.islineedit) {
            sb.append(".islineedit(true)");
        }
        if (this.isdelete) {
            sb.append(".isdelete(true)");
        }
        if (this.isexport) {
            sb.append(".isexport(true)");
        }
        if (this.isimport) {
            sb.append(".isimport(true)");
        }
        if (this.isapproval) {
            sb.append(".isapproval(true)");
        }
        if (this.isenabledisable) {
            sb.append(".isenabledisable(true)");
        }
        if (this.isreadonly) {
            sb.append(".isreadonly(true)");
        }
        if (this.isexclusive) {
            sb.append(".isexclusive(true)");
        }
        if (this.autolineheight) {
            sb.append(".autolineheight(true)");
        }
        if (!this.pagelimit) {
            sb.append(".pagelimit(false)");
        }
        if (!this.customview) {
            sb.append(".customview(false)");
        }
        if (this.ishidden) {
            sb.append(".ishidden(true)");
        }
        if (this.editactionflow){
            sb.append(".editactionflow(true)");
        }
        if(!this.searchcolumnflow){
            sb.append(".searchcolumnflow(false)");
        }
        if(!this.requireleft){
            sb.append(".requireleft(false)");
        }
        if(!this.saveiscloseeditview){
            sb.append(".saveiscloseeditview(false)");
        }
        if(!this.blockcard){
            sb.append(".blockcard(false)");
        }
        if(this.popupeditview) {
            sb.append(".popupeditview(true)");
        }
        if(this.batcheditview) {
            sb.append(".batcheditview(true)");
        }
        return sb.toString();
    }
}
