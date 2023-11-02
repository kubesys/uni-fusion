package com.qnkj.core.base.modules.supplier.SupplierDepartments.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.OutsideLink;
import com.qnkj.common.entitys.Tab;
import com.qnkj.common.entitys.TabField;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;
import com.qnkj.core.base.modules.supplier.SupplierDepartments.services.ISupplierdepartmentsService;

import java.util.Arrays;

/**
* @author Auto Generator
* @date 2021-03-31
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("supplier").parent("suppliersettings").label("部门管理").icon("lcdp-icon-bumen").modulename("supplierdepartments").url("/supplierdepartments/listview").order(2);

		/*模块配置*/
		tabs = new Tab().modulename("SupplierDepartments").modulelabel("部门管理").tabname("supplier_departments").moduletype(2).datarole(1);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("parentid").fieldlabel("上级部门").uitype(11).readonly(true).align("left"),
			new TabField().fieldname("name").fieldlabel("部门名称").unique(true).typeofdata("NS~M").align("left"),
			new TabField().fieldname("leadership").fieldlabel("部门领导").uitype(25).clearbtn(true).isarray(true).multiselect(true),
			new TabField().fieldname("supplierid").fieldlabel("企业名称").uitype(11).displaytype(2),
			new TabField().fieldname("mainleadership").fieldlabel("主管领导").uitype(25).clearbtn(true),
			new TabField().fieldname("sequence").fieldlabel("排序号").align("left")
		);

		/*关联显示配置*/
		entityNames.fieldname("name");

		/*外部关联配置*/
		outsideLinks = Arrays.asList(
			new OutsideLink().fieldname("supplierid").relmodule("supplier").serviceclass(ISupplierService.class.getName()).placeholder("选择企业").url("supplier/popupview"),
			new OutsideLink().fieldname("parentid").relmodule("supplierdepartments").serviceclass(ISupplierdepartmentsService.class.getName()),
			new OutsideLink().fieldname("leadership").relmodule("supplierdepartments").serviceclass(ISupplierdepartmentsService.class.getName()).url("supplierdepartments/popupview/users"),
			new OutsideLink().fieldname("mainleadership").relmodule("supplierdepartments").serviceclass(ISupplierdepartmentsService.class.getName()).url("supplierdepartments/popupview/users")
		);

		/*弹出对话框配置*/
		popupDialog.search(Arrays.asList("name")).columns(Arrays.asList("supplierid","name","parentid"));

	}
}
