package com.qnkj.core.base.modules.baseservices.feedback.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2021-12-17
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("management").parent("baseservices").label("申请反馈").icon("lcdp-icon-denglurizhi1").url("/feedback/listview").order(3);

		/*模块配置*/
		tabs = new Tab().modulename("feedback").modulelabel("申请反馈").tabname("feedback").hasoperator(false).isexport(true).isreadonly(true);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("name","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("mobile","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("company","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("industry","")),
			new Layout().block(1).columns(Collections.singletonList("12")).fields(Collections.singletonList("memo"))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("name").fieldlabel("姓名").readonly(true).typeofdata("V~M").align("center").issort(true),
			new TabField().fieldname("mobile").fieldlabel("手机号码").readonly(true).typeofdata("V~M").align("center").issort(true),
			new TabField().fieldname("company").fieldlabel("公司名称").readonly(true).align("center").issort(true),
			new TabField().fieldname("industry").fieldlabel("行业").readonly(true).align("center").issort(true),
			new TabField().fieldname("memo").fieldlabel("备注").uitype(23).readonly(true).issort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("name","mobile","company","industry","published","memo"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Collections.singletonList(
			new SearchColumn().fieldname("published").fieldlabel("创建时间").order(1).colspan(2).quickbtn(true).searchtype("calendar")
		);

		/*关联显示配置*/
		/*entityNames.fieldname("");*/

		/*外部关联配置*/
		/*outsideLinks = Arrays.asList(
			new OutsideLink().fieldname("").relmodule("")...
		);*/

		/*弹出对话框配置*/
		/*popupDialog.search(Arrays.asList(

		)).columns(Arrays.asList(

		));*/

		/*编号配置*/
		/*modentityNums.modulename("").prefix("");*/

		/*模块角色数据验证信息*/
		/*dataPermission.setExpressions(new HashMap<String, List<Expression>>(){{
			put("",Arrays.asList(
				new Expression()
			));
		}});*/

	}
}
