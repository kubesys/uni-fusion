package com.qnkj.core.base.modules.supplier.SupplierRoles.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;

import java.util.Arrays;

/**
* @author Auto Generator
* @date 2021-04-02
*/
public class ConfigActionsConfig extends BaseActionsConfig {
	public ConfigActionsConfig() {
		/*Actions 配置*/
		actions = Arrays.asList(
			new Action().actionkey("EditView").actionlabel("新建").toggle("dialog").digwidth("500px")
		);

	}
}