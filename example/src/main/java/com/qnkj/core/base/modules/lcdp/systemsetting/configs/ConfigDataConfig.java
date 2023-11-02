package com.qnkj.core.base.modules.lcdp.systemsetting.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;

/**
 * create by 徐雁
 * create date 2021/3/5
 */

public class ConfigDataConfig  extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("lcdp").parent("developmentmanager").label("系统配置").icon("lcdp-icon-xitongpeizhi").order(1).url("/systemsetting/listview");
        tabs.modulename("systemsetting").modulelabel("系统配置");
    }
}
