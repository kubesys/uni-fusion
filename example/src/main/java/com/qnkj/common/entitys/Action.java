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

public class Action extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("按钮关建字")
    public String actionkey = "";

    @ApiModelProperty("角色")
    public String authorizes = "";

    @ApiModelProperty("按钮标签")
    public String actionlabel = "";

    @ApiModelProperty("是否需验证检查")
    public Boolean securitycheck = false;

    @ApiModelProperty("按钮应用场景：listview，operation，panel，editview")
    public String actiontype = "listview";

    @ApiModelProperty("按钮图标")
    public String icon = "";

    @ApiModelProperty("按钮执行方式：navtab，dialog，ajax，upload")
    public String toggle = "";

    @ApiModelProperty("toggle=ajax时，操作提示消息")
    public String confirm = "";

    @ApiModelProperty("选择操作时返回后台的参数名称，同时此属性代表是否勾选操作")
    public String group = "";

    @ApiModelProperty("自定义处理函数Service")
    public String funclass = "";

    @ApiModelProperty("自定义处理函数的验证参数值")
    public String funpara = "";

    @ApiModelProperty("自定义css样式")
    public String actionclass = "";

    @ApiModelProperty("自定义style样式")
    public String actionstyle = "";

    @ApiModelProperty("按钮的html字符串")
    public String element = "";

    @ApiModelProperty("toggle=dialog时，弹出窗口宽度，px或auto")
    public String digwidth = "";

    @ApiModelProperty("toggle=dialog时，弹出窗口高度，px或auto")
    public String digheight = "";

    @ApiModelProperty("toggle=ajax、dialog时，打开指定的链接，当未设置时，将生成默认链接打开。")
    public String url = "";

    @ApiModelProperty("submit=true时，提交指定的链接，当未设置时，将生成默认链接。")
    public String submiturl = "";

    @ApiModelProperty("是否多选操作")
    public Boolean multiselect = true;

    @ApiModelProperty("type=editview时，区块显示位置；panel，base，bottom")
    public String location = "";

    @ApiModelProperty("自定义事件名称")
    public String event = "";

    @ApiModelProperty("排序")
    public int order = 0;

    @ApiModelProperty("是否为提交操作")
    public Boolean submit = false;

    public Action() {
        this.id = "";
    }

    public Action(Object content){
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
    public Action modulename(String modulename){
        this.modulename = modulename;
        return this;
    }

    /**
     * 按钮关建字 已定义值：<br/>
     * EditView：通用编辑按钮<br/>Delete：通用删除按钮<br/>SuperDelete：通用超级删除按钮<br/>
     * 可替换
     */
    public Action actionkey(String actionkey){
        this.actionkey = actionkey;
        return this;
    }

    /**
     * 权限验证角色列表<br/>
     * 通用模块，取包含企业的所有角色，多选逗号分隔，为空或不选时，所有角色可见
     */
    public Action authorizes(String authorize){
        this.authorizes = authorize;
        return this;
    }


    /**
     * 按钮标签
     */
    public Action actionlabel(String actionlabel){
        this.actionlabel = actionlabel;
        return this;
    }

    /**
     * 是否需验证检查
     * 自定义处理函数需同时配置：funclass；funpara；securitycheck才生效
     */
    public Action securitycheck(Boolean securitycheck){
        this.securitycheck = securitycheck;
        return this;
    }

    /**
     * 按钮应用场景：<br>
     * listview：在列表页展示<br>
     * editview：在编辑页展示<br>
     * panel：在编辑页展示一个嵌入式页面<br>
     * operation：在列表页面记录后展示一个图标<br>
     */
    public Action actiontype(String actiontype){
        this.actiontype = actiontype;
        return this;
    }

    /**
     * 按钮图标
     */
    public Action icon(String icon){
        this.icon = icon;
        return this;
    }

    /**
     * 按钮执行方式：<br>
     * navtab：打开新的页面<br>
     * dialog：打开一个窗口<br>
     * dialogtab：打开一个窗口<br>
     * ajax：执行一个异步交互<br>
     * upload：上传文件<br>
     * export：列表导出<br>
     */
    public Action toggle(String toggle){
        this.toggle = toggle;
        return this;
    }

    /**
     * toggle=ajax时，操作提示消息
     */
    public Action confirm(String confirm){
        this.confirm = confirm;
        return this;
    }

    /**
     * 选择操作时返回后台的参数名称，同时此属性代表是否勾选操作
     */
    public Action group(String group){
        this.group = group;
        return this;
    }

    /**
     * 自定义处理函数的Service名称<br>
     * 自定义处理函数需同时配置：funclass；funpara；securitycheck才生效<br>
     * 在Service中接管getActionVerify处理函数<br>
     *
     * 注：funclass：默认IBaseService基类或指定自定义处理类名字符串
     */
    public Action funclass(String funclass){
        this.funclass = funclass;
        return this;
    }

    /**
     * 此属性为函数的参数值
     * 自定义处理函数需同时配置：funclass；funpara；securitycheck才生效
     */
    public Action funpara(String funpara){
        this.funpara = funpara;
        return this;
    }

    /**
     * 自定义css样式
     */
    public Action actionclass(String actionclass){
        this.actionclass = actionclass;
        return this;
    }

    /**
     * 自定义style样式
     */
    public Action actionstyle(String actionstyle){
        this.actionstyle = actionstyle;
        return this;
    }

    /**
     * 按钮的html字符串
     */
    public Action element(String element){
        this.element = element;
        return this;
    }

    /**
     * toggle=dialog时，弹出窗口宽度，px或auto
     */
    public Action digwidth(String digwidth){
        this.digwidth = digwidth;
        return this;
    }

    /**
     * toggle=dialog时，弹出窗口高度，px或auto
     */
    public Action digheight(String digheight){
        this.digheight = digheight;
        return this;
    }

    /**
     * toggle=ajax、dialog时，打开指定的链接，当未设置时，将生成默认链接打开
     */
    public Action url(String url){
        this.url = url;
        return this;
    }

    public Action submiturl(String url) {
        this.submiturl = url;
        return this;
    }

    /**
     * 是否多选操作
     */
    public Action multiselect(Boolean multiselect){
        this.multiselect = multiselect;
        return this;
    }

    /**
     * type=ajax时，区块显示位置；panel，base，bottom
     */
    public Action location(String location){
        this.location = location;
        return this;
    }

    public Action event(String event){
        this.event = event;
        return this;
    }

    public Action order(Integer order){
        this.order = order;
        return this;
    }

    /**
     * 如果是编辑页的按钮，指明这按钮是提交数据类型
     */
    public Action submit(Boolean submit) {
        this.submit = submit;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new Action()");
        sb.append(".actionkey(\"").append(Utils.replaceString(this.actionkey)).append("\")");
        sb.append(".actionlabel(\"").append(Utils.replaceString(this.actionlabel)).append("\")");
        if (!"listview".equals(this.actiontype)) {
            sb.append(".actiontype(\"").append(Utils.replaceString(this.actiontype)).append("\")");
        }
        if (!this.toggle.isEmpty()) {
            sb.append(".toggle(\"").append(Utils.replaceString(this.toggle)).append("\")");
        }
        if (!this.location.isEmpty()) {
            sb.append(".location(\"").append(Utils.replaceString(this.location)).append("\")");
        }
        if (Boolean.TRUE.equals(this.securitycheck)) {
            sb.append(".securitycheck(true)");
            if (!this.funclass.isEmpty()) {
                if(this.funclass.endsWith(".class.getName()")){
                    sb.append(".funclass(").append(Utils.replaceString(this.funclass)).append(")");
                }else {
                    sb.append(".funclass(\"").append(Utils.replaceString(this.funclass)).append("\")");
                }
            }
            if (!this.funpara.isEmpty()) {
                sb.append(".funpara(\"").append(Utils.replaceString(this.funpara)).append("\")");
            }
            if (!this.authorizes.isEmpty()) {
                sb.append(".authorizes(\"").append(Utils.replaceString(this.authorizes)).append("\")");
            }
        }
        if(!"panel".equals(this.actiontype)) {
            if (!this.group.isEmpty()) {
                sb.append(".group(\"").append(Utils.replaceString(this.group)).append("\")");
            }
            if (!this.icon.isEmpty()) {
                sb.append(".icon(\"").append(Utils.replaceString(this.icon)).append("\")");
            }
            if (!this.confirm.isEmpty()) {
                sb.append(".confirm(\"").append(Utils.replaceString(this.confirm)).append("\")");
            }
            if (!this.actionclass.isEmpty()) {
                sb.append(".actionclass(\"").append(Utils.replaceString(this.actionclass)).append("\")");
            }
            if (!this.actionstyle.isEmpty()) {
                sb.append(".actionstyle(\"").append(Utils.replaceString(this.actionstyle)).append("\")");
            }
            if (!this.element.isEmpty()) {
                sb.append(".element(\"").append(Utils.replaceString(this.element)).append("\")");
            }
            if (!this.digwidth.isEmpty()) {
                sb.append(".digwidth(\"").append(Utils.replaceString(this.digwidth)).append("\")");
            }
            if (!this.digheight.isEmpty()) {
                sb.append(".digheight(\"").append(Utils.replaceString(this.digheight)).append("\")");
            }
            if (Boolean.FALSE.equals(this.multiselect)) {
                sb.append(".multiselect(false)");
            }
            if (Boolean.TRUE.equals(this.submit)) {
                sb.append(".submit(true)");
                if(!this.submiturl.isEmpty()){
                    sb.append(".submiturl(\"").append(Utils.replaceString(this.submiturl)).append("\")");
                }
            }
        }
        if (!this.url.isEmpty()) {
            sb.append(".url(\"").append(Utils.replaceString(this.url)).append("\")");
        }
        if (!this.event.isEmpty()) {
            sb.append(".event(\"").append(Utils.replaceString(this.event)).append("\")");
        }
        return sb.toString();
    }
}
