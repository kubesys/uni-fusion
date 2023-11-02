package com.qnkj.core.base.modules.management.notices.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Auto Generator
 * @date 2020-11-17
 */
public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        /*菜单配置*/
        moduleMenu = new ModuleMenu().builtin(true).program("management").parent("home").modulename("notices").label("通知").icon("lcdp-icon-xiaoxi").modulename("notices").url("/notices/listview").order(2);

        /*模块配置*/
        tabs = new Tab().modulename("notices").modulelabel("通知").tabname("notices").datatype("YearContent").defaultsection("YearContent").searchcolumn(3).isreadonly(true);

        /*视图配置*/
        customViews = Collections.singletonList(
                new CustomView().viewname("默认").columnlist(Arrays.asList("supplierid", "title", "publisher", "noticetype", "noticelevel", "profileid", "alreadyread", "published"))
        );

        /*字段配置*/
        fields = Arrays.asList(
                new TabField().fieldname("title").fieldlabel("标题").typeofdata("V~M").listwidth("30%").align("left"),
                new TabField().fieldname("publisher").fieldlabel("发布者").typeofdata("V~M").listwidth("8%"),
                new TabField().fieldname("supplierid").fieldlabel("企业").uitype(11).typeofdata("V~M").listwidth("20%").defaultvalue("0").displaytype(2),
                new TabField().fieldname("profileid").fieldlabel("被通知人").uitype(25).listwidth("10%").displaytype(2),
                new TabField().fieldname("noticetype").fieldlabel("通知类型").uitype(13).align("center").listwidth("8%"),
                new TabField().fieldname("noticelevel").fieldlabel("通知级别").uitype(13).align("center").listwidth("5%"),
                new TabField().fieldname("alreadyreads").fieldlabel("已读列表").uitype(11).listwidth("15%").displaytype(2).isarray(true),
                new TabField().fieldname("alreadyread").fieldlabel("是否已读").uitype(14).align("center").align("center").listwidth("15%").displaytype(2),
                new TabField().fieldname("body").fieldlabel("内容").uitype(23).merge_column(1).editwidth("75%"),
                new TabField().fieldname("md5").fieldlabel("唯一键").uitype(1).displaytype(2)
        );

        /*列表视图查询条件配置*/
        searchColumn = Arrays.asList(
                new SearchColumn().fieldname("published").fieldlabel("创建日期").colspan(2).width(95).quickbtn(true).searchtype("calendar")
        );

        /*外部关联配置*/
        outsideLinks = Arrays.asList(
                new OutsideLink().fieldname("supplierid").relmodule("supplier").serviceclass(ISupplierService.class.getName())
        );

    }
}
