package com.qnkj.core.base.modules.lcdp.formdesign.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.TabField;

import java.util.Arrays;
import java.util.Collections;

/**
 * create by 徐雁
 * create date 2020/11/9
 */

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("lcdp").parent("developmentmanager").label("表单设计").icon("lcdp-icon-biaodansheji").order(3).url("/formdesign/listview");
        tabs.modulename("formdesign").modulelabel("表单设计").tabname("formdesigns");
        customViews = Collections.singletonList(
                new CustomView().viewname("默认").isdefault(true).columnlist(Arrays.asList("module", "parent", "program", "published", "author", "generate"))
        );

        fields = Arrays.asList(
                new TabField().fieldname("program").fieldlabel("菜单组").readonly(true),
                new TabField().fieldname("parent").fieldlabel("模块组").readonly(true),
                new TabField().fieldname("module").fieldlabel("模块名称").readonly(true),
                new TabField().fieldname("published").fieldlabel("创建日期").listwidth("172").readonly(true),
                new TabField().fieldname("author").fieldlabel("创建人").readonly(true),
                new TabField().fieldname("generate").fieldlabel("已生成代码").listwidth("100").uitype(5).align("center").readonly(true)
        );
    }
}
