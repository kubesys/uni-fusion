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

public class TabField extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";
    @ApiModelProperty("字段名称")
    public String fieldname = "";
    @ApiModelProperty("字段标签")
    public String fieldlabel = "";
    @ApiModelProperty("组合标签")
    public String grouplabel = "";
    @ApiModelProperty("HTML显示")
    public String html = "";
    @ApiModelProperty("数据类型ID")
    public Integer uitype = 1;
    @ApiModelProperty("字段是否只读")
    public Boolean readonly = false;
    @ApiModelProperty("字段所属区块ID")
    public Integer block = 1;
    @ApiModelProperty("数据验证类型")
    public String typeofdata = "V~O";
    @ApiModelProperty("各界面中是否显示处理规则：取值：1：新建可见，列表和编辑不可见。2：列表可见，编辑不可见")
    public Integer displaytype = 0;
    @ApiModelProperty("是否附加上一个字段")
    public Boolean deputy_column = false;
    @ApiModelProperty("是否合并行字段")
    public Integer merge_column = 0;
    @ApiModelProperty("是否显示标签")
    public Boolean show_title = true;
    @ApiModelProperty("编辑时占用宽度")
    public String editwidth = "";
    @ApiModelProperty("编辑时占用高度")
    public String editheight = "";
    @ApiModelProperty("列表时占用宽度")
    public String listwidth = "";
    @ApiModelProperty("列表显示时对齐方式：left,center,right")
    public String align = "left";
    @ApiModelProperty("编辑时附加后面的信息，如单位")
    public String aux = "";
    @ApiModelProperty("引用字段字典")
    public String picklist = "";
    @ApiModelProperty("是否多选")
    public Boolean multiselect = false;
    @ApiModelProperty("新建时的默认值")
    public String defaultvalue = "";
    @ApiModelProperty("选择类型时，是否显示清除图标")
    public Boolean clearbtn = false;
    @ApiModelProperty("弹窗类型时，窗口宽度")
    public String digwidth = "";
    @ApiModelProperty("弹窗类型时，窗口高度")
    public String digheight = "";
    @ApiModelProperty("输入类型时，最大输入长度")
    public Integer maxlength = 100;
    @ApiModelProperty("图片类型时，裁剪的最大宽度")
    public Integer cutwidth = 0;
    @ApiModelProperty("图片类型时，裁剪的最大高度")
    public Integer cutheight = 0;
    @ApiModelProperty("关联字段")
    public String relation = "";
    @ApiModelProperty("地区类型时，可选择级别：province,city,district。默认：district")
    public String regionlevel = "district";
    @ApiModelProperty("远程验证函数")
    public String remoteverify = "";
    @ApiModelProperty("排序号")
    public Integer sequence = 0;
    @ApiModelProperty("唯一")
    public Boolean unique = false;
    @ApiModelProperty("是否有事件")
    public Boolean event = false;
    @ApiModelProperty("是否可排序")
    public Boolean issort = false;
    @ApiModelProperty("是否数值排序")
    public Boolean isnumsort = false;
    @ApiModelProperty("是否统计")
    public Boolean isstatistic = false;
    @ApiModelProperty("是否数组字段")
    public Boolean isarray = false;
    @ApiModelProperty("是否只能选择")
    public Boolean onlyselect = false;
    @ApiModelProperty("货币类型时的比率，用于列表显示时进行换算;如界面输入1元，存入数据为分则比率为100")
    public Integer ratio = 1;
    @ApiModelProperty("星级等级")
    public Integer scorelevel = 5;
    @ApiModelProperty("启用流程时。流程设置的数据")
    protected Object flowvalue;
    @ApiModelProperty("启用流程时。流程设置是否可编辑")
    protected Object flowreadonly;

    public TabField() {
        this.id = "";
    }

    public TabField(Object content) {
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
    public TabField modulename(String modulename) {
        this.modulename = modulename;
        return this;
    }

    /**
     * 字段名称
     */
    public TabField fieldname(String fieldname) {
        this.fieldname = fieldname;
        return this;
    }

    /**
     * 字段标签
     */
    public TabField fieldlabel(String fieldlabel) {
        this.fieldlabel = fieldlabel;
        return this;
    }

    /**
     * 组合标签
     */
    public TabField grouplabel(String grouplabel) {
        this.grouplabel = grouplabel;
        return this;
    }

    /**
     * HTML
     */
    public TabField html(String html) {
        this.html = html;
        return this;
    }


    /**
     * 数据类型ID<br>
     * * 1：字符串类型;<br>
     * * 2：新建时可编辑的文本框;<br>
     * * 3：密码类型输入框;<br>
     * * 4：编号型字段文本框;<br>
     * * 5: 是否选择框<br>
     * * 6：日期型文本框;<br>
     * * 7：日期带时分型文本框;<br>
     * * 8：时间型文本框;<br>
     * * 9：颜色选择框;<br>
     * * 10：部门选择类型;<br>
     * * 11：弹窗选择类型（单行）;<br>
     * * 12：弹窗选择类型（多行）;<br>
     * * 13：单项选择类型;(字典)<br>
     * * 14：单项选择取值类型;(字典)<br>
     * * 15：多选框类型;(字典)<br>
     * * 16：多选框取值类型;(字典)<br>
     * * 17：图片类型;<br>
     * * 18：附件类型;<br>
     * * 19：下拉选择类型;(字典)<br>
     * * 20：下拉选择取值类型;(字典)<br>
     * * 21：下拉树结构类型;<br>
     * * 22：上下分隔线类型;<br>
     * * 23：多行文本框;<br>
     * * 24：文本编辑器;<br>
     * * 25：用户选择类型;<br>
     * * 26：地址区域选择类型;<br>
     * * 27：货币型类型;<br>
     * * 28: 星级评分;<br>
     * * 29: 自定义组件;<br>
     * * 30: 普通文本行（只用于显示）<br>
     * * 31：HTML文本域（只用于显示）<br>
     */
    public TabField uitype(Integer uitype) {
        this.uitype = uitype;
        return this;
    }

    /**
     * 字段是否只读
     * 字段是否只读；取值：true只读、false非只读；
     */
    public TabField readonly(Boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    /**
     * 字段所属区块ID
     */
    public TabField block(Integer block) {
        this.block = block;
        return this;
    }

    /**
     * 数据验证类型
     * 编辑界面中数据验证规则；取值：[V|NS|SN|L|LN|NN|MO|EM|PH|QQ|PHMO|IN|MONEY]~[O|M]~[0]~[100]；
     * V字符串;NN数字型；MO手机号；EM邮箱；PH座机；QQ号；PHMO座机或手机号；IN数字型；MONEY货币型
     * NS标准字符串(不包含特殊字符);SN字母、下划线、数字;L纯字母;LN字母加数字
     * O非必填；M必填；
     * 当NN或IN时，最后两项为最大最小取值区间，无选项时无限制。
     */
    public TabField typeofdata(String typeofdata) {
        this.typeofdata = typeofdata;
        return this;
    }

    /**
     *
     * 字段显示类型
     * 各界面中是否显示处理规则：取值：1：新建可见，列表和编辑不可见。2：列表可见，编辑不可见
     */
    public TabField displaytype(Integer displaytype) {
        this.displaytype = displaytype;
        return this;
    }

    /**
     * 是否附加上一个字段
     * （可选）编辑界面中，此字段是否为与上一个字段合并显示，取值：true是合并、false非合并；
     */
    public TabField deputy_column(Boolean deputy_column) {
        this.deputy_column = deputy_column;
        return this;
    }

    /**
     * 是否合并行字段
     * （可选）编辑界面中，此字段是否独占行显示，取值：1是新启行独占、2是新启行非独占、0正常处理；
     */
    public TabField merge_column(Integer merge_column) {
        this.merge_column = merge_column;
        return this;
    }

    /**
     * 是否显示标签
     * 编辑界面中，此字段是否显示标签fieldlabel值，取值：true是显示、false非显示；
     */
    public TabField show_title(Boolean show_title) {
        this.show_title = show_title;
        return this;
    }

    /**
     * 编辑时占用宽度
     * （可选）编辑界面中，此字段显示宽度;
     */
    public TabField editwidth(String editwidth) {
        this.editwidth = editwidth;
        return this;
    }

    /**
     * 编辑时占用高度
     */
    public TabField editheight(String editheight) {
        this.editheight = editheight;
        return this;
    }

    /**
     * 列表时占用宽度
     * （可选）列表界面中，此字段显示宽度百分比；
     */
    public TabField listwidth(String listwidth) {
        this.listwidth = listwidth;
        return this;
    }

    public TabField listwidth(Integer listwidth) {
        this.listwidth = Integer.toString(listwidth);
        return this;
    }

    /**
     * 列表显示时对齐方式：left,center,right
     */
    public TabField align(String align) {
        this.align = align;
        return this;
    }

    /**
     * 编辑时附加后面的信息，如单位
     */
    public TabField aux(String aux) {
        this.aux = aux;
        return this;
    }

    /**
     * 引用字段字典
     * uitype=12,13,14,15,18,19时，显示的字典集
     */
    public TabField picklist(String picklist) {
        this.picklist = picklist;
        return this;
    }

    /**
     * 多项选择模式
     */
    public TabField multiselect(Boolean multiselect) {
        this.multiselect = multiselect;
        return this;
    }

    /**
     * 新建时的默认值
     */
    public TabField defaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
        return this;
    }

    /**
     * 选择类型时，是否显示清除图标
     */
    public TabField clearbtn(Boolean clearbtn) {
        this.clearbtn = clearbtn;
        return this;
    }

    /**
     * 弹窗类型时，窗口宽度
     * uitype=10,11,24
     */
    public TabField digwidth(String digwidth) {
        this.digwidth = digwidth;
        return this;
    }

    /**
     * 弹窗类型时，窗口高度
     * uitype=10,11,24
     */
    public TabField digheight(String digheight) {
        this.digheight = digheight;
        return this;
    }

    /**
     * 输入类型时，最大输入长度
     */
    public TabField maxlength(Integer maxlength) {
        this.maxlength = maxlength;
        return this;
    }

    /**
     * 图片类型时，裁剪的宽度
     * uitype = 16
     */
    public TabField cutwidth(Integer cutwidth){
        this.cutwidth = cutwidth;
        return this;
    }

    /**
     * 图片类型时，裁剪的高度
     * uitype = 16
     */
    public TabField cutheight(Integer cutheight){
        this.cutheight = cutheight;
        return this;
    }

    /**
     * 关联字段
     */
    public TabField relation(String relation) {
        this.relation = relation;
        return this;
    }

    /**
     * 地区类型时，可选择级别：province,city,district。默认：district
     * uitype=25
     */
    public TabField regionlevel(String regionlevel) {
        this.regionlevel = regionlevel;
        return this;
    }

    /**
     * 远程验证函数
     */
    public TabField remoteverify(String remoteverify) {
        this.remoteverify = remoteverify;
        return this;
    }

    /**
     * 字段排序号
     */
    public TabField sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    /**
     * 是否可排序
     */
    public TabField issort(Boolean issort) {
        this.issort = issort;
        return this;
    }
    /**
     * 唯一性判断
     */
    public TabField unique(Boolean unique) {
        this.unique = unique;
        return this;
    }

    /**
     * 是否有事件
     */
    public TabField event(Boolean event) {
        this.event = event;
        return this;
    }
    /**
     * 是否数值排序
     */
    public TabField isnumsort(Boolean isnumsort) {
        this.isnumsort = isnumsort;
        return this;
    }

    /**
     * 是否为统计字段
     */
    public TabField isstatistic(Boolean isstatistic) {
        this.isstatistic = isstatistic;
        return this;
    }

    /**
     * 是否数组字段
     */
    public TabField isarray(Boolean isarray) {
        this.isarray = isarray;
        return this;
    }

    /**
     * 货币类型时的比率，用于列表显示时进行换算;默认单位是元，即ratio=1
     * 如：数据需存”分“的单位，则ratio=100,即1：100
     * 如：数据需存”万“为单位，则ratio=10000,即1：10000
     */
    public TabField ratio(Integer ratio){
        if(ratio <= 0) {
            this.ratio = 1;
        } else {
            this.ratio = ratio;
        }
        return this;
    }

    /**
     * 星级评分最高等级
     * @param scorelevel 3~10
     */
    public TabField scorelevel(Integer scorelevel) {
        if(scorelevel < 3) {
            this.scorelevel = 3;
        } else {
            this.scorelevel = scorelevel;
        }
        return this;
    }

    /**
     * 是否只能选择，用于扩展字段类型
     */
    public TabField onlyselect(Boolean onlyselect) {
        this.onlyselect = onlyselect;
        return this;
    }

    public TabField setFlowvalue(Object value) {
        this.flowvalue = value;
        return this;
    }
    public Object getFlowvalue() {
        return this.flowvalue;
    }

    public TabField setFlowReadonly(Object value) {
        this.flowreadonly = value;
        return this;
    }
    public Object getFlowReadonly() {
        return this.flowreadonly;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new TabField()");
        sb.append(".fieldname(\"").append(Utils.replaceString(this.fieldname)).append("\")");
        sb.append(".fieldlabel(\"").append(Utils.replaceString(this.fieldlabel)).append("\")");
        if (this.uitype != 1) {
            sb.append(".uitype(").append(this.uitype).append(")");
        }
        if(this.uitype != 30 && this.uitype != 31) {
            if (!this.grouplabel.isEmpty()) {
                sb.append(".grouplabel(\"").append(Utils.replaceString(this.grouplabel)).append("\")");
            }
            if (this.readonly) {
                sb.append(".readonly(true)");
            }
            if (!"V~O".equals(this.typeofdata)) {
                sb.append(".typeofdata(\"").append(this.typeofdata).append("\")");
            }
            if (this.displaytype != 0) {
                sb.append(".displaytype(").append(this.displaytype).append(")");
            }
            if (!this.show_title) {
                sb.append(".show_title(false)");
            }
            if (!this.editwidth.isEmpty()) {
                sb.append(".editwidth(\"").append(this.editwidth).append("\")");
            }
            if (!this.editheight.isEmpty()) {
                sb.append(".editheight(\"").append(this.editheight).append("\")");
            }
            if (!this.listwidth.isEmpty()) {
                sb.append(".listwidth(\"").append(this.listwidth).append("\")");
            }
            if (!"left".equals(this.align)) {
                sb.append(".align(\"").append(this.align).append("\")");
            }
            if (!this.aux.isEmpty()) {
                sb.append(".aux(\"").append(Utils.replaceString(this.aux)).append("\")");
            }
            if (!this.picklist.isEmpty() && !this.fieldname.equals(this.picklist)) {
                sb.append(".picklist(\"").append(this.picklist).append("\")");
            }
            if (this.multiselect) {
                sb.append(".multiselect(true)");
            }
            if (!this.defaultvalue.isEmpty()) {
                sb.append(".defaultvalue(\"").append(Utils.replaceString(this.defaultvalue)).append("\")");
            }
            if (this.clearbtn) {
                sb.append(".clearbtn(true)");
            }
            if (!this.digwidth.isEmpty()) {
                sb.append(".digwidth(\"").append(this.digwidth).append("\")");
            }
            if (!this.digheight.isEmpty()) {
                sb.append(".digheight(\"").append(this.digheight).append("\")");
            }
            if (this.maxlength != 100) {
                sb.append(".maxlength(").append(this.maxlength).append(")");
            }
            if (this.cutwidth > 0) {
                sb.append(".cutwidth(").append(this.cutwidth).append(")");
            }
            if (this.cutheight > 0) {
                sb.append(".cutheight(").append(this.cutheight).append(")");
            }
            if (!this.relation.isEmpty()) {
                sb.append(".relation(\"").append(this.relation).append("\")");
            }
            if (!this.relation.isEmpty() && !this.regionlevel.isEmpty() && !"district".equals(this.regionlevel)) {
                sb.append(".regionlevel(\"").append(this.regionlevel).append("\")");
            }
            if (!this.remoteverify.isEmpty()) {
                sb.append(".remoteverify(\"").append(Utils.replaceString(this.remoteverify)).append("\")");
            }
            if (this.issort) {
                sb.append(".issort(true)");
            }
            if (this.unique) {
                sb.append(".unique(true)");
            }
            if (this.event) {
                sb.append(".event(true)");
            }
            if (this.isnumsort) {
                sb.append(".isnumsort(true)");
            }
            if (this.isstatistic) {
                sb.append(".isstatistic(true)");
            }
            if (this.isarray) {
                sb.append(".isarray(true)");
            }
            if (this.uitype == 27 && this.ratio > 1) {
                sb.append(".ratio(").append(this.ratio).append(")");
            }
            if (this.uitype == 28 && this.scorelevel != 5) {
                sb.append(".scorelevel(").append(this.scorelevel).append(")");
            }
            if (this.uitype == 29 && this.onlyselect) {
                sb.append(".onlyselect(true)");
            }
        } else {
            if (!this.html.isEmpty()) {
                sb.append(".html(\"").append(Utils.replaceString(this.html)).append("\")");
            }
            if (!this.show_title) {
                sb.append(".show_title(false)");
            }
        }
        return sb.toString();
    }

    @Override
    public TabField clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ignored) {}
        TabField field = new TabField();
        field.setValues(this);
        return field;
    }

    private void setValues(TabField tabField) {
        super.setValues(tabField);
        modulename(tabField.modulename);
        fieldname(tabField.fieldname);
        unique(tabField.unique);
        fieldlabel(tabField.fieldlabel);
        grouplabel(tabField.grouplabel);
        html(tabField.html);
        uitype(tabField.uitype);
        readonly(tabField.readonly);
        block(tabField.block);
        typeofdata(tabField.typeofdata);
        displaytype(tabField.displaytype);
        deputy_column(tabField.deputy_column);
        merge_column(tabField.merge_column);
        show_title(tabField.show_title);
        editwidth(tabField.editwidth);
        editheight(tabField.editheight);
        listwidth(tabField.listwidth);
        align(tabField.align);
        aux(tabField.aux);
        picklist(tabField.picklist);
        multiselect(tabField.multiselect);
        defaultvalue(tabField.defaultvalue);
        clearbtn(tabField.clearbtn);
        digwidth(tabField.digwidth);
        digheight(tabField.digheight);
        maxlength(tabField.maxlength);
        cutwidth(tabField.cutwidth);
        cutheight(tabField.cutheight);
        relation(tabField.relation);
        regionlevel(tabField.regionlevel);
        remoteverify(tabField.remoteverify);
        sequence(tabField.sequence);
        event(tabField.event);
        issort(tabField.issort);
        isnumsort(tabField.isnumsort);
        isstatistic(tabField.isstatistic);
        isarray(tabField.isarray);
        ratio(tabField.ratio);
        scorelevel(tabField.scorelevel);
        onlyselect(tabField.onlyselect);
    }
}
