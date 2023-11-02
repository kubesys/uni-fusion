package com.qnkj.core.base.modules.settings.backupmanage.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.TabField;

import java.util.Arrays;

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("generalmanager").label("备份管理").icon("lcdp-icon-beifenguanli").order(1).url("/backupmanage/listview");

        tabs.modulename("backupmanage").modulelabel("备份管理").tabname("backup").hasoperator(false).iscreate(true).isdelete(false);

        customViews = Arrays.asList(
                new CustomView().viewname("默认").isdefault(true).columnlist(Arrays.asList("backupname", "published", "style"))
        );

        fields = Arrays.asList(
                new TabField().fieldname("backupname").fieldlabel("备份名称").readonly(true).align("left"),
                new TabField().fieldname("published").fieldlabel("备份日期").readonly(true),
                new TabField().fieldname("style").fieldlabel("类型").readonly(true).listwidth("20%")
        );
    }
}
