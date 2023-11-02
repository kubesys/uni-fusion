package com.qnkj.clouds.modules.VmPools.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;

import java.util.Arrays;

/**
* @author Auto Generator
* @date 2023-06-25
*/
public class ConfigActionsConfig extends BaseActionsConfig {
	public ConfigActionsConfig() {
		/*Actions 配置*/
		actions = Arrays.asList(
			new Action().actionkey("Active").actionlabel("启用").toggle("ajax").group("ids").icon("lcdp-icon-qiyong").confirm("确定要启用吗？"),
			new Action().actionkey("Inactive").actionlabel("停用").toggle("ajax").group("ids").icon("lcdp-icon-tingyong").confirm("确定要停用吗？").actionclass("layui-btn-grey"),
			new Action().actionkey("More").actionlabel("更多").actiontype("operation").toggle("nothing").icon("lcdp-icon-gengduowukuang").multiselect(false)

		);

	}
}
