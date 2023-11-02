package com.qnkj.core.base.modules.supplier.SupplierUsers.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;
import com.qnkj.core.base.modules.supplier.SupplierDepartments.services.ISupplierdepartmentsService;
import com.qnkj.core.base.modules.supplier.SupplierRoles.services.ISupplierrolesService;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2021-03-31
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("supplier").parent("suppliersettings").label("用户管理").icon("lcdp-icon-yonghu").modulename("supplierusers").url("/supplierusers/listview").order(1);

		/*模块配置*/
		tabs = new Tab().modulename("SupplierUsers").modulelabel("用户管理").tabname("supplier_users").datarole(1).iscreate(true).isexport(true);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("supplierid").fieldlabel("企业名称").uitype(11).readonly(true).displaytype(2),
			new TabField().fieldname("account").fieldlabel("账号名称").unique(true).uitype(2).typeofdata("NS~M"),
			new TabField().fieldname("username").fieldlabel("姓名").typeofdata("NS~M"),
			new TabField().fieldname("password").fieldlabel("账号密码").uitype(3).typeofdata("V~M").displaytype(1),
			new TabField().fieldname("confirm_password").fieldlabel("密码确认").uitype(3).typeofdata("V~M").relation("password").displaytype(1),
			new TabField().fieldname("departmentid").fieldlabel("部门").uitype(10),
			new TabField().fieldname("roleid").fieldlabel("权限").uitype(11),
			new TabField().fieldname("directsuperior").fieldlabel("直接上级").uitype(25),
			new TabField().fieldname("sequence").fieldlabel("排序号").typeofdata("IN~O").defaultvalue("100").issort(true).isnumsort(true),
			new TabField().fieldname("mobile").fieldlabel("手机号码").typeofdata("MO~M").issort(true).isnumsort(true),
			new TabField().fieldname("mailbox").fieldlabel("邮箱").typeofdata("EM~O").issort(true),
			new TabField().fieldname("status").fieldlabel("启用").uitype(13).picklist("status").align("center").defaultvalue("Active")
		);

		/*视图配置*/
		customViews = Arrays.asList(
			new CustomView().viewname("默认").orderby("status").order("D").columnlist(Arrays.asList("username","account","departmentid","roleid","directsuperior","mobile","status","sequence","published"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Arrays.asList(
			new SearchColumn().fieldname("account").fieldlabel("账号名称").searchtype("vague_input"),
			new SearchColumn().fieldname("username").fieldlabel("姓名").searchtype("vague_input"),
			new SearchColumn().fieldname("mobile").fieldlabel("手机号码").searchtype("vague_input"),
			new SearchColumn().fieldname("status").fieldlabel("启用").searchtype("text")
		);

		/*关联显示配置*/
		entityNames.fieldname("username");

		/*外部关联配置*/
		outsideLinks = Arrays.asList(
			new OutsideLink().fieldname("supplierid").relmodule("supplier").serviceclass(ISupplierService.class.getName()).placeholder("选择企业").url("supplier/popupview"),
			new OutsideLink().fieldname("departmentid").relmodule("supplierdepartments").serviceclass(ISupplierdepartmentsService.class.getName()).placeholder("选择部门").url("supplierdepartments/popupview/tree"),
			new OutsideLink().fieldname("directsuperior").relmodule("supplierdepartments").serviceclass(ISupplierusersService.class.getName()).placeholder("选择上级").url("supplierdepartments/popupview/users"),
			new OutsideLink().fieldname("roleid").relmodule("supplierroles").serviceclass(ISupplierrolesService.class.getName()).placeholder("选择权限").url("supplierroles/popupview")
		);

		/*弹出对话框配置*/
		popupDialog.search(Arrays.asList("mobile","username")).columns(Arrays.asList("username","mobile","departmentid","status"));

		/*
		编号配置
		modentityNums.modulename("").prefix("");
		*/
	}
}
