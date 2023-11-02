package com.qnkj.core.base.modules.lcdp.pagedesign.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.OutsideLink;
import com.qnkj.common.entitys.TabField;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;

import java.util.Arrays;
import java.util.Collections;

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        /*菜单配置*/
        moduleMenu = new ModuleMenu().builtin(true).program("lcdp").parent("developmentmanager").label("页面设计").icon("lcdp-icon-yemiansheji").order(4).url("/pagedesign/listview");

        /*模块配置*/
        tabs.modulename("pagedesign").modulelabel("页面设计").tabname("pagedesigns").iscreate(false);

        /*字段配置*/
        fields = Arrays.asList(
                new TabField().fieldname("program").fieldlabel("菜单组").sequence(1).readonly(true),
                new TabField().fieldname("parent").fieldlabel("模块组").sequence(2).readonly(true),
                new TabField().fieldname("module").fieldlabel("模块名称").sequence(3).readonly(true),
                new TabField().fieldname("generate").fieldlabel("已生成代码").sequence(7).listwidth("100").uitype(5).align("center").readonly(true),
                new TabField().fieldname("template_editor").fieldlabel("模板").displaytype(2),
                new TabField().fieldname("supplierid").fieldlabel("企业ID").uitype(11).displaytype(2)
        );

        /*视图配置*/
        customViews = Collections.singletonList(
                new CustomView().viewname("默认").isdefault(true).columnlist(Arrays.asList("module", "parent", "program", "author", "generate", "published", "updated"))
        );

        /*列表视图查询条件配置*/
        searchColumn = Collections.emptyList();

        /*外部关联配置*/
        outsideLinks = Collections.singletonList(
                new OutsideLink().fieldname("supplierid").relmodule("supplier").serviceclass(ISupplierService.class.getName())
        );
    }
}
