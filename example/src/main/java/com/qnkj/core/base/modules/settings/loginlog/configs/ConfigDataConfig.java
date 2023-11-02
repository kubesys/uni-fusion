package com.qnkj.core.base.modules.settings.loginlog.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;

import java.util.Arrays;

/**
 * create by 徐雁
 */
public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("generalmanager").label("登录日志").icon("lcdp-icon-denglurizhi").order(3).url("/loginlog/listview");

        tabs.modulename("loginlog").modulelabel("登录日志").tabname("loginlogs").datatype("YearMonthContent").hasoperator(false).defaultsection("ThisMonth").searchcolumn(3);

        customViews = Arrays.asList(
                new CustomView().viewname("默认").isdefault(true)
                        .columnlist(Arrays.asList("supplierid","profileid","logintime","location","ip","system","browser"))
        );

        fields = Arrays.asList(
                new TabField().fieldname("supplierid").fieldlabel("所属企业").uitype(11).readonly(true).typeofdata("V~M"),
                new TabField().fieldname("profileid").fieldlabel("账户").uitype(25).readonly(true).typeofdata("V~M"),
                new TabField().fieldname("ip").fieldlabel("登录IP").readonly(true).typeofdata("V~M"),
                new TabField().fieldname("logintime").fieldlabel("登录时间").readonly(true).typeofdata("V~M"),
                new TabField().fieldname("location").fieldlabel("登录地区").readonly(true).typeofdata("V~M"),
                new TabField().fieldname("system").fieldlabel("操作系统").readonly(true).typeofdata("V~M"),
                new TabField().fieldname("browser").fieldlabel("浏览器").readonly(true).typeofdata("V~M")
        );

        searchColumn = Arrays.asList(
                new SearchColumn().fieldname("published").fieldlabel("创建日期").searchtype("calendar").width(95).colspan(2).quickbtn(true)
        );

        outsideLinks = Arrays.asList(
                new OutsideLink().fieldname("supplierid").relmodule("supplier").serviceclass(ISupplierService.class.getName())
        );

    }
}
