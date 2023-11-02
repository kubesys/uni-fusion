package com.qnkj.core.base.modules.home.BuiltinAnnouncements.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.Tab;

/**
* @author Auto Generator
* @date 2020-11-17
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("suppliermanagement").parent("shouye").modulename("builtinannouncements").label("公告").icon("lcdp-icon-gonggao1").url("/builtinannouncements/index").order(1);

		/*模块配置*/
		tabs = new Tab().modulename("BuiltinAnnouncements").modulelabel("公告").moduletype(1);
	}
}
