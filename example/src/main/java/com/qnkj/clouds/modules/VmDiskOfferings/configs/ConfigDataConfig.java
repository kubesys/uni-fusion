package com.qnkj.clouds.modules.VmDiskOfferings.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2023-06-27
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("kubestack").parent("CloudResources").label("云盘规格").icon("iconfont icon-kucun").url("/VmDiskOfferings/listview").order(5);

		/*模块配置*/
		tabs = new Tab().modulename("VmDiskOfferings").modulelabel("云盘规格").tabname("vm_diskofferings").iscreate(true).isdelete(true).isenabledisable(true).customview(false);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("name","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("capacity","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("describe",""))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("name").fieldlabel("名称").typeofdata("NS~M").issort(true),
			new TabField().fieldname("capacity").fieldlabel("容量").typeofdata("IN~M").aux("GB").defaultvalue("10").issort(true).isnumsort(true),
			new TabField().fieldname("describe").fieldlabel("简介").uitype(23).issort(true),
			new TabField().fieldname("status").fieldlabel("启用").uitype(13).displaytype(2).defaultvalue("Active").issort(true).isnumsort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("name","status","capacity","published"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Collections.singletonList(
			new SearchColumn().fieldname("status").fieldlabel("启用").order(1).searchtype("text")
		);

		/*关联显示配置*/
		entityNames.fieldname("name");

		/*外部关联配置*/
		/*outsideLinks = Arrays.asList(
			new OutsideLink().fieldname("").relmodule("")...
		);*/

		/*弹出对话框配置*/
		popupDialog.search(Collections.singletonList("name")).columns(Arrays.asList("name","status","capacity","published"));

		/*编号配置*/
		/*modentityNums.modulename("").prefix("");*/

		/*模块角色数据验证信息*/
		/*dataPermission.setExpressions(new HashMap<String, List<Expression>>(){{
			put("",Arrays.asList(
				new Expression()
			));
		}});*/

		/*模块API接口配置信息*/
		/*api.modulename("VmDiskOfferings");*/

	}
}
