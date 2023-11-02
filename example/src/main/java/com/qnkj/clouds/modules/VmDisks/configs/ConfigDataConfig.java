package com.qnkj.clouds.modules.VmDisks.configs;

import com.qnkj.clouds.modules.VmDiskOfferings.services.IVmDiskOfferingsService;
import com.qnkj.clouds.modules.VmPools.services.IVmPoolsService;
import com.qnkj.clouds.modules.VmZones.services.IVmZonesService;
import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2023-06-26
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("kubestack").parent("CloudResources").label("云盘").icon("iconfont icon-kucun").url("/VmDisks/listview").order(2);

		/*模块配置*/
		tabs = new Tab().modulename("VmDisks").modulelabel("云盘").tabname("vm_disks").iscreate(true).isdelete(true).customview(false);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("name","")),
			new Layout().block(1).columns(Collections.singletonList("12")).fields(Collections.singletonList("describe")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("node","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("diskoffering","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("poolname","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("vmdisktype","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("format","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("eventid","make_event_id"))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("name").fieldlabel("名称").typeofdata("NS~M").issort(true).unique(true),
			new TabField().fieldname("describe").fieldlabel("简介").uitype(23).issort(true),
			new TabField().fieldname("diskoffering").fieldlabel("云盘规格").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("poolname").fieldlabel("主存储").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("pool").fieldlabel("主存储").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("vmdisktype").fieldlabel("类型").uitype(19).typeofdata("V~M").picklist("vmtype").issort(true),
			new TabField().fieldname("eventid").fieldlabel("事件ID").issort(true),
			new TabField().fieldname("make_event_id").fieldlabel("生成事件ID").uitype(34).show_title(false),
			new TabField().fieldname("current").fieldlabel("URL").issort(true),
			new TabField().fieldname("format").fieldlabel("格式").uitype(19).picklist("vmmediatype").typeofdata("V~M").issort(true),
			new TabField().fieldname("uid").fieldlabel("UID").issort(true),
			new TabField().fieldname("capacity").fieldlabel("容量").typeofdata("IN~O").issort(true),
			new TabField().fieldname("node").fieldlabel("节点").uitype(19).typeofdata("V~M").issort(true),
			new TabField().fieldname("status").fieldlabel("启用").uitype(13).displaytype(2).defaultvalue("Active").issort(true).isnumsort(true),
			new TabField().fieldname("state").fieldlabel("就绪状态").uitype(19).picklist("vmdiskstate").issort(true),
			new TabField().fieldname("zone").fieldlabel("区域").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("createtime").fieldlabel("创建时间").issort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("zone","name","pool","current","format","status","state","node","createtime"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Arrays.asList(
			new SearchColumn().fieldname("state").fieldlabel("就绪状态").order(1).searchtype("select"),
			new SearchColumn().fieldname("status").fieldlabel("启用").order(2).searchtype("text")
		);

		/*关联显示配置*/
		entityNames.fieldname("name");

		/*外部关联配置*/
		outsideLinks = Arrays.asList(
			new OutsideLink().fieldname("diskoffering").relmodule("VmDiskOfferings").serviceclass(IVmDiskOfferingsService.class.getName()).url("VmDiskOfferings/popupview"),
			new OutsideLink().fieldname("poolname").relmodule("VmPools").serviceclass(IVmPoolsService.class.getName()).url("VmPools/popupview"),
			new OutsideLink().fieldname("zone").relmodule("VmZones").serviceclass(IVmZonesService.class.getName()).url("VmZones/popupview")
		);

		/*弹出对话框配置*/
		popupDialog.search(Collections.singletonList("name")).columns(Arrays.asList("zone","name","pool","current","format","status","state","node"));

		/*编号配置*/
		/*modentityNums.modulename("").prefix("");*/

		/*模块角色数据验证信息*/
		/*dataPermission.setExpressions(new HashMap<String, List<Expression>>(){{
			put("",Arrays.asList(
				new Expression()
			));
		}});*/

		/*模块API接口配置信息*/
		/*api.modulename("VmDisks");*/

	}
}
