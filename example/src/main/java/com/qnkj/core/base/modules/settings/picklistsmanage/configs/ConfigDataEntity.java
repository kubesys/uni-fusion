package com.qnkj.core.base.modules.settings.picklistsmanage.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;

public class ConfigDataEntity extends BaseDataConfig {
    public ConfigDataEntity() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("generalmanager").label("数据字典").icon("lcdp-icon-shujuzidian").order(5).url("/picklistsmanage/listview");

        tabs.modulename("picklistsmanage").modulelabel("数据字典").tabname("picklists").moduletype(1);
    }
}
