package com.qnkj.core.base.modules.supplier.SupplierRoles.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2021-04-02
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("supplier").parent("suppliersettings").label("权限管理").icon("lcdp-icon-quanxian").modulename("supplierroles").url("/supplierroles/listview").order(3);

		/*模块配置*/
		tabs = new Tab().modulename("SupplierRoles").modulelabel("权限管理").tabname("supplier_roles").datarole(1).iscreate(true).isdelete(true);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("supplierid").fieldlabel("企业名称").uitype(11).readonly(true).displaytype(2).align("left"),
			new TabField().fieldname("rolename").fieldlabel("权限名称").unique(true).typeofdata("NS~M").editwidth("100").listwidth("15%").align("left").issort(true),
			new TabField().fieldname("author").fieldlabel("创建人").typeofdata("V~M").editwidth("100").listwidth("15%").align("left").issort(true),
			new TabField().fieldname("published").fieldlabel("创建时间").typeofdata("V~M").editwidth("300").listwidth("15%").align("left").issort(true),
			new TabField().fieldname("description").fieldlabel("备注").merge_column(2).editwidth("300").listwidth("50%").align("left")
		);

		/*视图配置*/
		customViews = Arrays.asList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("rolename","author","published","description"))
		);

		/*
		列表视图查询条件配置
		searchColumn = Arrays.asList(
			new SearchColumn().fieldname("").fieldlabel("")...
		);
		*/
		/*关联显示配置*/
		entityNames.fieldname("rolename");

		/*
		外部关联配置
		outsideLinks = Arrays.asList(
			new OutsideLink().fieldname("").relmodule("")...
		);
		*/
		/*弹出对话框配置*/
		popupDialog.search(Arrays.asList("rolename","author","description")).columns(Arrays.asList("rolename","description","author","published"));

		/*
		编号配置
		modentityNums.modulename("").prefix("");
		*/
	}
}
