package com.qnkj.core.base.modules.home.BuiltinNotices.configs;

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
		moduleMenu = new ModuleMenu().builtin(true).program("suppliermanagement").parent("shouye").modulename("builtinnotices").label("通知").icon("lcdp-icon-xiaoxi").url("/builtinnotices/index").order(2);
		/*模块配置*/
		tabs = new Tab().modulename("BuiltinNotices").modulelabel("通知").moduletype(1);
	}
}
