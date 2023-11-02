package com.qnkj.core.base.modules.lcdp.developments.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;

/**
 * create by 徐雁
 * create date 2020/11/9
 */

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("lcdp").parent("developmentmanager").label("菜单设计").icon("lcdp-icon-caidanguanli").order(2).url("/developments/listview");
        tabs.modulename("developments").modulelabel("菜单设计");
    }
}
