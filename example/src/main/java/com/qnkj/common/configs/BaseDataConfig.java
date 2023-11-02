package com.qnkj.common.configs;

import com.google.common.collect.ImmutableSet;
import com.qnkj.common.entitys.*;

import java.util.ArrayList;
import java.util.List;

public class BaseDataConfig {
    /**
     * 构建菜单信息
     */
    public ModuleMenu moduleMenu = new ModuleMenu();

    public Tab tabs = new Tab().searchcolumn(2);

    /**
     * blocks定义此模块编辑界面中分块区；
     * 取值：HashMap<String, String>；
     * blocklabel：区块标签字符串；
     * sequence：显示排序，值越小越在前；数字字符。
     */
    public List<Block> blocks = new ArrayList<>(ImmutableSet.of(
            new Block().blocklabel("基本信息").blockid(1).columns(2)
    ));

    public List<Layout> layouts = new ArrayList<>();

    /**
     * fields定义此模块所有数据字段信息；
     * 取值：HashMap<String, String>；
     * fieldname：字段名称；
     * fieldlabel：字段显示字符串；
     * uitype：字段数据类型；取值：
     * 1：字符串类型;
     * 2：新建时可编辑的文本框;
     * 3：密码类型输入框;
     * 4：编号型字段文本框;
     * 5：日期型文本框;
     * 6：日期带时分型文本框;
     * 7：时间型文本框;
     * 8：颜色选择框;
     * 9：部门选择类型;
     * 10：弹窗选择类型（单行）;
     * 11：弹窗选择类型（多行）;
     * 12：单项选择类型;
     * 13：单项选择取值类型;
     * 14：多选框类型;
     * 15：多选框取值类型;
     * 16：图片类型;
     * 17：附件类型;
     * 18：下拉选择类型;
     * 19：下拉选择取值类型;
     * 20：下拉树结构类型;
     * 21：上下分隔线类型;
     * 22：多行文本框;
     * 23：文本编辑器;
     * 24：用户选择类型;
     * 25：地址区域选择类型;
     * readOnly：字段是否只读；取值：true只读、false非只读；
     * block：编辑界面中所属的块区；
     * typeofdata：编辑界面中数据验证规则；取值：[V|NN|MO|EM|PH|QQ|PHMO|IN|MONEY]~[O|M]~[0]~[100]；
     * V字符串;NN数字型；MO手机号；EM邮箱；PH座机；QQ号；PHMO座机或手机号；IN数字型；MONEY货币型、O非必填；M必填；
     * 当NN或IN时，最后两项为最大最小取值区间，无选项时无限制。
     * displaytype：各界面中是否显示处理规则：取值：1：新建可见，列表和编辑不可见。2：列表可见，编辑不可见
     * deputy_column：（可选）编辑界面中，此字段是否为与上一个字段合并显示，取值：true是合并、false非合并；
     * merge_column：（可选）编辑界面中，此字段是否独占行显示，取值：1是新启行独占、2是新启行非独占、0正常处理；
     * show_title：编辑界面中，此字段是否显示标签fieldlabel值，取值：true是显示、false非显示；
     * editwidth：（可选）编辑界面中，此字段显示宽度px;
     * listwidth：（可选）列表界面中，此字段显示宽度百分比；
     * align：列表界面中，此字段列显示时对齐方式；取值：left、center、right
     * aux：指定互动元素后跟随的信息，如数字后面的单位
     * pickList：uitype=12,14,18,19时，显示的字典集
     * multiselect：多项选择模式
     * clearbtn：是否显示清除当前字段值的按钮
     * defaultvalue：当新建时字段默认值
     * remoteverify：远程验证方法（url链接)
     * relation：字段的关联字段名称，用于本字段发生改变时，关联字段做相关处理
     * 当uitype=10,22时，可选以下值：
     * digwidth：弹出窗口宽度，px或auto
     * digheight：弹出窗口高度，px或auto
     * 当uitype=23时，relation字段指定编辑界面上显示的字段名称。
     * regionlevel：设置联动选的级别；取值：province,city,district。默认：district
     * maxlength：输入框类型时，最大输入字符数量
     */
    public List<TabField> fields = new ArrayList<>();
    /**
     * customViews列表页具体显示列表；
     * 取值：HashMap<String, String>；
     * viewname：视图名称字符串；
     * isdefault：是否默认视图；取值：true默认、false非默认；
     * columnlist：具体显示字体集。
     * authorize：授权类型，不为空时，此列表将只用于指定权限的人员显示
     */
    public List<CustomView> customViews = new ArrayList<>();
    /**
     * entityNames：数据表关联信息。
     * 当连表查询时，别的表需查本表的对应字段信息
     * entityfield：外部模块传入值所对应的本模块字段名
     * fieldname：返回本模块数据的字段名
     */
    public EntityLink entityNames = new EntityLink();
    /**
     * outsideLinks：数据字段关联别的表信息
     * 取值：HashMap<String, Object>()
     * Key：为本地数据库字段名；
     * Value：为外部模块信息配置：
     * 取值：HashMap<String, Object>()
     * serviceclass：（必选）外部模块数据模型类名;
     * dataentityclass：（必选）外部模块数据配置类名；
     * url：外部模块找开的链接；
     * placeholder：本模块对此字段的提示信息
     */
    public List<OutsideLink> outsideLinks = new ArrayList<>();
    /**
     * searchColumn：列表界面查询
     * column：界面上显示列数；默认2列
     * config
     * 取值：HashMap<String, Object>()
     * fieldname：用于查询的字段名称
     * fieldlabel：查询界面上显示的标签
     * type：查询界面显示模式；取值：
     * calendar：日期选择；
     * multi_input：多字段联合查询，fieldname中每个字段名称以”,“分开；
     * vague_input：模糊匹配；
     * input或search_input：精准匹配；
     * text：文本方法选择；
     * multitree：树型多选匹配；
     * radiotree：树型单选匹配。
     * tip：查询输入框为空时提示消息
     * quickbtn：当type=calendar时，是否显示快捷按钮
     * width：指定互动元素的宽度（px），无指定时采用系统默认设置
     * aux：指定互动元素后跟随的信息，如数字后面的单位
     * newline：指定下一个查询字段是否新行显示；取值：true/false
     * colspan：指定占用列数
     */
    public List<SearchColumn> searchColumn = new ArrayList<>();

    /**
     * 弹窗设置
     */
    public PopupDialog popupDialog = new PopupDialog();

    /**
     * modentityNums：模块自动编号设置
     * modulename：显示名称
     * prefix：编号前缀
     * start_id：开始索引
     * cur_id：当前索引
     * length：编码长度
     * include_date：编码是否包含日期
     */
    public ModentityNum modentityNums = new ModentityNum();

    /**
     * 模块角色数据验证信息
     */
    public DataPermission dataPermission = new DataPermission();

    /**
     * 模块开放接口配置
     */
    public Api api = new Api();
}
