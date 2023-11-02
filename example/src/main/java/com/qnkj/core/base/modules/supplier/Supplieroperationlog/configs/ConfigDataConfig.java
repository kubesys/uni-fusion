package com.qnkj.core.base.modules.supplier.Supplieroperationlog.configs;

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
		moduleMenu = new ModuleMenu().builtin(true).program("supplier").parent("suppliergeneralmanager").label("操作日志").icon("lcdp-icon-caozuorizhi").url("/supplieroperationlog/listview").order(7);

		/*模块配置*/
		tabs = new Tab().modulename("Supplieroperationlog").modulelabel("操作日志").tabname("operlogs").datatype("YearMonthContent").hasoperator(false).datarole(1).defaultsection("ThisMonth").searchcolumn(3);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("supplierid","profileid")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("description","uri")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("method","log_type")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("ip","address")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("browser","time")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("exception_detail"))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("supplierid").fieldlabel("所属企业").uitype(11).readonly(true).align("left"),
			new TabField().fieldname("profileid").fieldlabel("操作用户").uitype(25).readonly(true).listwidth("6%").align("left"),
			new TabField().fieldname("description").fieldlabel("描述").readonly(true).align("left"),
			new TabField().fieldname("uri").fieldlabel("访问路径").readonly(true).align("left"),
			new TabField().fieldname("method").fieldlabel("方法名").readonly(true).align("left"),
			new TabField().fieldname("log_type").fieldlabel("日志类型").readonly(true).listwidth("6%").align("left"),
			new TabField().fieldname("ip").fieldlabel("请求ip").readonly(true).listwidth("8%").align("left"),
			new TabField().fieldname("address").fieldlabel("地址").readonly(true).align("left"),
			new TabField().fieldname("browser").fieldlabel("浏览器").readonly(true).listwidth("6%").align("left"),
			new TabField().fieldname("time").fieldlabel("请求耗时").readonly(true).listwidth("6%"),
			new TabField().fieldname("exception_detail").fieldlabel("异常详细").readonly(true).align("left")
		);

		/*视图配置*/
		customViews = Arrays.asList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("log_type","supplierid","profileid","description","uri","ip","address","browser","time","exception_detail","published"))
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
