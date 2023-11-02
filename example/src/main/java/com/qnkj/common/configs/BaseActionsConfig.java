package com.qnkj.common.configs;

import com.qnkj.common.entitys.Action;
import com.qnkj.common.entitys.Tab;

import java.util.ArrayList;
import java.util.List;

public class BaseActionsConfig {

    public Tab tabs = new Tab().iscreate(false).isdelete(false).isexport(false).isimport(false);

    /**
     * actions：模块按钮或数据区块
     * 取值：HashMap<String, Object>()
     *      actionlabel：按钮显示标签
     *      actionkey：关键字符串；已定义值：EditView、Delete、SuperDelete、SendApprove、ExportExcel
     *      securitycheck：是否进行权限验证
     *      fun：自定义处理函数；可重写此类中的increase无参数或increasepara有参数函数
     *      funpara：当fun采用有参数时，此属性为函数的参数值
     *      icon：按钮的矢量图标
     *      type：定义类型；取值：listview模块列表页面的按钮、ajax模块编辑页面的面版、editview模板编辑页面的按钮
     *      class：自定义css样式
     *      style：自定义style样式
     *      当type=listview时，以下数据生效：
     *          element：按钮的html字符串
     *          toggle：按钮执行方式：
     *              当navtab时，在导航页面打开新的网页
     *              当dialog时，以下数据生效：
     *                  digwidth：弹出窗口宽度，px或auto
     *                  digheight：弹出窗口高度，px或auto
     *              当ajax时，以下数据生效
     *                  confirm：设置操作时的确认消息，此属性无时，不进行确认操作
     *          url：打开指定的链接，当toggle=ajax、dialog有效，当未设置时，将生成默认链接打开。
     *          group：选择操作时返回后台的参数名称，同时此属性代表是否勾选操作。
     *          multiselect：是否多选操作
     *      当type=ajax时，以下数据生效：
     *          location：区块显示位置；取值：
     *              panel：为tab标签页
     *              base：在基本信息内显示
     *              bottom：在页面底部显示
     *
     * */
    public List<Action> actions = new ArrayList<>();

}
