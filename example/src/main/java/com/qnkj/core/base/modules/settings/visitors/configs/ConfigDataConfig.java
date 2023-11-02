package com.qnkj.core.base.modules.settings.visitors.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2023-05-31
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("generalmanager").label("开放授权").icon("lcdp-icon-kaifazhe").url("/visitors/listview").order(7);

		/*模块配置*/
		tabs = new Tab().modulename("visitors").modulelabel("开放授权").tabname("visitors").iscreate(true).isdelete(true).isenabledisable(true).customview(false);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("appid","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("secret","makesecret")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("supplierid",""))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("appid").fieldlabel("appid").typeofdata("NS~M").issort(true),
			new TabField().fieldname("secret").fieldlabel("密钥").typeofdata("NS~M").issort(true),
			new TabField().fieldname("makesecret").fieldlabel("随机生成32位密钥").uitype(34).show_title(false),
			new TabField().fieldname("supplierid").fieldlabel("授权企业").uitype(11).issort(true),
			new TabField().fieldname("status").fieldlabel("启用").uitype(13).displaytype(2).defaultvalue("Active").issort(true).isnumsort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("appid","secret","supplierid","status","published"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Collections.singletonList(
			new SearchColumn().fieldname("status").fieldlabel("启用").order(1).searchtype("text")
		);

		/*关联显示配置*/
		/*entityNames.fieldname("");*/

		/*外部关联配置*/
		outsideLinks = Collections.singletonList(
			new OutsideLink().fieldname("supplierid").relmodule("supplier").serviceclass(ISupplierService.class.getName()).url("supplier/popupview")
		);

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

		/*模块API接口配置信息*/
		/*api.modulename("visitors");*/

	}
}
