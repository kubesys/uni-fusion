package com.qnkj.core.base.modules.supplier.Supplierpicklists.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;

public class ConfigDataEntity extends BaseDataConfig {
    public ConfigDataEntity() {
        moduleMenu = new ModuleMenu().builtin(true).program("supplier").parent("suppliergeneralmanager").label("数据字典").icon("lcdp-icon-shujuzidian").order(8).url("/supplierpicklists/listview");
        tabs.modulename("Supplierpicklists").modulelabel("数据字典").tabname("supplierpicklists").moduletype(1);
    }
}
