package com.qnkj.core.base.modules.settings.operationlog.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;

import java.util.Arrays;

/**
 * create by 徐雁
 */
public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("generalmanager").label("操作日志").icon("lcdp-icon-caozuorizhi").order(2).url("/operationlog/listview");

        tabs.modulename("operationlog").modulelabel("操作日志").tabname("operlogs").datatype("YearMonthContent").hasoperator(false).defaultsection("ThisMonth").searchcolumn(3);

        customViews = Arrays.asList(
                new CustomView().columnlist(Arrays.asList("log_type", "supplierid", "profileid", "description", "uri", "ip", "address", "browser", "time", "exception_detail","published"))
        );

        fields = Arrays.asList(
                new TabField().fieldname("supplierid").fieldlabel("所属企业").uitype(11).readonly(true),
                new TabField().fieldname("profileid").fieldlabel("操作用户").uitype(25).readonly(true).listwidth("6%"),
                new TabField().fieldname("description").fieldlabel("描述").readonly(true),
                new TabField().fieldname("uri").fieldlabel("访问路径").readonly(true),
                new TabField().fieldname("method").fieldlabel("方法名").readonly(true),
                new TabField().fieldname("log_type").fieldlabel("日志类型").readonly(true).listwidth("6%"),
                new TabField().fieldname("ip").fieldlabel("请求ip").readonly(true).listwidth("8%"),
                new TabField().fieldname("address").fieldlabel("地址").readonly(true),
                new TabField().fieldname("browser").fieldlabel("浏览器").readonly(true).listwidth("6%"),
                new TabField().fieldname("time").fieldlabel("请求耗时").readonly(true).listwidth("6%").align("center"),
                new TabField().fieldname("exception_detail").fieldlabel("异常详细").readonly(true)
        );

        searchColumn = Arrays.asList(
                new SearchColumn().fieldname("published").fieldlabel("创建日期").searchtype("calendar").width(95).colspan(2).quickbtn(true)
        );

        outsideLinks = Arrays.asList(
                new OutsideLink().fieldname("supplierid").relmodule("supplier").serviceclass(ISupplierService.class.getName())
        );
    }
}
