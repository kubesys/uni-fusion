package com.qnkj.core.base.modules.settings.onlinedevelopments.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;

/**
 * create by 徐雁
 * create date 2020/11/9
 */

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("onlinedevelopmentmanager").label("在线开发").icon("lcdp-icon-caidanguanli").order(2).url("/onlinedevelopments/listview");
        tabs.modulename("onlinedevelopments").modulelabel("在线开发").ishidden(true);
    }
}
