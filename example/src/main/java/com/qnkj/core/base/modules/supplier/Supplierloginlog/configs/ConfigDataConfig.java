package com.qnkj.core.base.modules.supplier.Supplierloginlog.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2021-05-23
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("supplier").parent("suppliergeneralmanager").label("登录日志").icon("lcdp-icon-denglurizhi").url("/supplierloginlog/listview").order(6);

		/*模块配置*/
		tabs = new Tab().modulename("Supplierloginlog").modulelabel("登录日志").tabname("loginlogs").datatype("YearMonthContent").hasoperator(false).datarole(1).defaultsection("ThisMonth").searchcolumn(3);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("supplierid","profileid")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("ip","logintime")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("location","system")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("browser"))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("supplierid").fieldlabel("所属企业").uitype(11).readonly(true).typeofdata("V~M").align("left"),
			new TabField().fieldname("profileid").fieldlabel("账户").uitype(25).readonly(true).typeofdata("V~M").align("left"),
			new TabField().fieldname("ip").fieldlabel("登录IP").readonly(true).typeofdata("V~M").align("left"),
			new TabField().fieldname("logintime").fieldlabel("登录时间").readonly(true).typeofdata("V~M").align("left"),
			new TabField().fieldname("location").fieldlabel("登录地区").readonly(true).typeofdata("V~M").align("left"),
			new TabField().fieldname("system").fieldlabel("操作系统").readonly(true).typeofdata("V~M").align("left"),
			new TabField().fieldname("browser").fieldlabel("浏览器").readonly(true).typeofdata("V~M").align("left")
		);

		/*视图配置*/
		customViews = Arrays.asList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("supplierid","profileid","logintime","location","ip","system","browser"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Arrays.asList(
			new SearchColumn().fieldname("published").fieldlabel("创建日期").order(1).colspan(2).width(95).quickbtn(true).searchtype("calendar")
		);

		/*关联显示配置*/
		/*entityNames.fieldname("");*/

		/*外部关联配置*/
		outsideLinks = Arrays.asList(
			new OutsideLink().fieldname("supplierid").relmodule("supplier").serviceclass(ISupplierService.class.getName())
		);

		/*弹出对话框配置*/
		/*popupDialog.search(Arrays.asList(

		)).columns(Arrays.asList(

		));*/

		/*编号配置*/
		/*modentityNums.modulename("").prefix("");*/

	}
}
