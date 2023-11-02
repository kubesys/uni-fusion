package com.qnkj.clouds.modules.VmInstances.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;

import java.util.Arrays;

/**
* @author Auto Generator
* @date 2023-06-27
*/
public class ConfigActionsConfig extends BaseActionsConfig {
	public ConfigActionsConfig() {
		/*Actions 配置*/
		actions = Arrays.asList(
			new Action().actionkey("More").actionlabel("更多").actiontype("operation").toggle("nothing").icon("lcdp-icon-gengduowukuang").multiselect(false)
		);
	}
}
