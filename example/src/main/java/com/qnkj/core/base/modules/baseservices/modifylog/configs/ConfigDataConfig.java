package com.qnkj.core.base.modules.baseservices.modifylog.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2021-09-18
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("management").parent("baseservices").label("修改日志").icon("lcdp-icon-yundanzhifujilu").url("/modifylog/listview").order(2);

		/*模块配置*/
		tabs = new Tab().modulename("modifylog").modulelabel("修改日志").tabname("modifylog").datatype("YearContent").hasoperator(false).autolineheight(true);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("belongmodule","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("record","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("table","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("body",""))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("belongmodule").fieldlabel("所属模块").readonly(true).listwidth("20%").issort(true),
			new TabField().fieldname("record").fieldlabel("记录ID").readonly(true).issort(true),
			new TabField().fieldname("table").fieldlabel("表名").readonly(true).issort(true),
			new TabField().fieldname("body").fieldlabel("编辑内容").uitype(23).readonly(true).listwidth("50%"),
			new TabField().fieldname("supplierid").fieldlabel("企业ID").uitype(11).displaytype(2)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("belongmodule","body","author","published"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Collections.singletonList(
			new SearchColumn().fieldname("published").fieldlabel("创建时间").order(1).colspan(2).quickbtn(true).searchtype("calendar")
		);

		/*关联显示配置*/
		/*entityNames.fieldname("");*/

		/*外部关联配置*/
		outsideLinks = Collections.singletonList(
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
