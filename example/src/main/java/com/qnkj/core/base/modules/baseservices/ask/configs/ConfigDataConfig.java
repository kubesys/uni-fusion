package com.qnkj.core.base.modules.baseservices.ask.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.users.service.IUsersService;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2021-08-18
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("management").parent("baseservices").label("在线咨询").icon("lcdp-icon-kefu1").url("/ask/listview").order(1);

		/*模块配置*/
		tabs = new Tab().modulename("ask").modulelabel("在线咨询").tabname("ask").isreadonly(true).blockcard(true);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("profileid","askstatus")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("lastasktime","lastreplytime")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("replied","respondent")),
			new Layout().block(1).columns(Arrays.asList("12")).fields(Arrays.asList("lastaskbody"))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("profileid").fieldlabel("咨询人").uitype(25).issort(true),
			new TabField().fieldname("lastaskbody").fieldlabel("最后咨询内容").uitype(23).issort(true),
			new TabField().fieldname("lastasktime").fieldlabel("最后咨询时间").issort(true),
			new TabField().fieldname("lastreplytime").fieldlabel("最后回复时间").issort(true),
			new TabField().fieldname("replied").fieldlabel("已回复").uitype(19).picklist("yesno").issort(true),
			new TabField().fieldname("respondent").fieldlabel("回复人").uitype(25).issort(true),
			new TabField().fieldname("askstatus").fieldlabel("状态").uitype(19).issort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("profileid","lastaskbody","lastasktime","lastreplytime","replied","respondent","askstatus","published"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Arrays.asList(
			new SearchColumn().fieldname("lastasktime").fieldlabel("最后咨询时间").order(1).colspan(2).quickbtn(true).searchtype("calendar"),
			new SearchColumn().fieldname("replied").fieldlabel("已回复").order(2).searchtype("text"),
			new SearchColumn().fieldname("askstatus").fieldlabel("状态").order(4).colspan(2).searchtype("text")
		);

		/*关联显示配置*/
		/*entityNames.fieldname("");*/

		/*外部关联配置*/
		outsideLinks = Collections.singletonList(
			new OutsideLink().fieldname("profileid").relmodule("users").serviceclass(IUsersService.class.getName()).url("users/popupview/users")
		);

		/*弹出对话框配置*/
		/*popupDialog.search(Arrays.asList(

		)).columns(Arrays.asList(

		));*/

		/*编号配置*/
		/*modentityNums.modulename("").prefix("");*/

	}
}
