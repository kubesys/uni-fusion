package com.qnkj.core.base.modules.baseservices.ask.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;
import com.qnkj.core.base.services.IBaseService;

import java.util.Arrays;

/**
* @author Auto Generator
* @date 2021-08-18
*/
public class ConfigActionsConfig extends BaseActionsConfig {
	public ConfigActionsConfig() {
		/*Actions 配置*/
		actions = Arrays.asList(
				new Action().actionkey("reply").actionlabel("回复咨询者").toggle("dialog").group("ids").multiselect(false).digwidth("500px"),
				new Action().actionkey("PopupEditView").actionlabel("修改回复状态").toggle("dialog").funclass(IBaseService.class.getName()).funpara("PopupEditview").group("ids").multiselect(false),
				new Action().actionkey("askMessage").actionlabel("消息记录").actiontype("panel").toggle("ajax").location("panel")
		);
	}
}
