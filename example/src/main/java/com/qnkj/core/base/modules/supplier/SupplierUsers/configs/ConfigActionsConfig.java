package com.qnkj.core.base.modules.supplier.SupplierUsers.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;

import java.util.Arrays;

/**
* @author Auto Generator
* @date 2021-03-31
*/
public class ConfigActionsConfig extends BaseActionsConfig {
	public ConfigActionsConfig() {
		/**
		Actions 配置
		 */
		actions = Arrays.asList(
				new Action().actionkey("Active").actionlabel("启用").group("ids").icon("lcdp-icon-qiyong").toggle("ajax").confirm("确定要启用吗？").funpara("Active").funclass(ISupplierusersService.class.getName()),
				new Action().actionkey("Inactive").actionlabel("停用").group("ids").icon("lcdp-icon-tingyong").actionclass("layui-btn-grey").toggle("ajax").confirm("确定要停用吗？").funpara("Inactive").funclass(ISupplierusersService.class.getName()),
				new Action().actionkey("ChanageAccount").actionlabel("修改账号").multiselect(false).group("ids").toggle("dialog").funpara("ChanageAccount").securitycheck(true).digwidth("500px").funclass(ISupplierusersService.class.getName()),
				new Action().actionkey("ModifyPassword").actionlabel("修改密码").multiselect(false).group("ids").toggle("dialog").funpara("ModifyPassword").securitycheck(true).digwidth("500px").funclass(ISupplierusersService.class.getName())

		);
	}
}
