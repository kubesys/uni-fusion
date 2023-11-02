package com.qnkj.clouds.modules.VmImages.configs;

import com.qnkj.clouds.modules.VmImageServers.services.IVmImageServersService;
import com.qnkj.clouds.modules.VmPools.services.IVmPoolsService;
import com.qnkj.clouds.modules.VmZones.services.IVmZonesService;
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
		moduleMenu = new ModuleMenu().builtin(true).program("kubestack").parent("CloudResources").label("镜像").icon("iconfont icon-kucun").url("/VmImages/listview").order(3);

		/*模块配置*/
		tabs = new Tab().modulename("VmImages").modulelabel("镜像").tabname("vm_images").iscreate(true).isdelete(true).isenabledisable(true).customview(false);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("name","")),
			new Layout().block(1).columns(Collections.singletonList("12")).fields(Collections.singletonList("describe")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("node","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("mediatype","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("imageserver","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("imagefile","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("poolname","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("source","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("eventid","make_event_id"))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("name").fieldlabel("名称").typeofdata("NS~M").issort(true).unique(true),
			new TabField().fieldname("describe").fieldlabel("简介").uitype(23).issort(true),
			new TabField().fieldname("mediatype").fieldlabel("镜像格式").uitype(13).typeofdata("V~M").picklist("vmmediatype").issort(true).defaultvalue("iso"),
			new TabField().fieldname("poolname").fieldlabel("主存储").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("pool").fieldlabel("主存储").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("source").fieldlabel("源文件").typeofdata("V~M").issort(true),
			new TabField().fieldname("current").fieldlabel("挂载路径").typeofdata("V~M").issort(true),
			new TabField().fieldname("format").fieldlabel("格式").uitype(19).picklist("vmmediatype").typeofdata("V~M").issort(true),
			new TabField().fieldname("imageserver").fieldlabel("镜像服务器").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("imagefile").fieldlabel("镜像文件").uitype(19).typeofdata("V~M").issort(true),
			new TabField().fieldname("node").fieldlabel("节点").uitype(19).typeofdata("V~M").issort(true),
			new TabField().fieldname("imageserver").fieldlabel("镜像服务器").uitype(11).issort(true),
			new TabField().fieldname("eventid").fieldlabel("事件ID").issort(true),
			new TabField().fieldname("make_event_id").fieldlabel("生成事件ID").uitype(34).show_title(false),
			new TabField().fieldname("status").fieldlabel("启用").uitype(13).displaytype(2).defaultvalue("Active").issort(true).isnumsort(true),
			new TabField().fieldname("state").fieldlabel("就绪状态").uitype(19).picklist("vmimagestate").issort(true),
			new TabField().fieldname("createtime").fieldlabel("创建时间").issort(true),
			new TabField().fieldname("uid").fieldlabel("UID").issort(true),
			new TabField().fieldname("zone").fieldlabel("区域").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("capacity").fieldlabel("容量").issort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("zone","name","pool","current","format","status","node","state","createtime"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Arrays.asList(
			new SearchColumn().fieldname("state").fieldlabel("就绪状态").order(1).searchtype("select"),
			new SearchColumn().fieldname("platform").fieldlabel("平台").order(2).searchtype("select"),
			new SearchColumn().fieldname("status").fieldlabel("启用").order(3).searchtype("text")
		);

		/*关联显示配置*/
		entityNames.fieldname("name");

		/*外部关联配置*/
		outsideLinks =  Arrays.asList(
			new OutsideLink().fieldname("poolname").relmodule("VmPools").serviceclass(IVmPoolsService.class.getName()).url("VmPools/popupview"),
			new OutsideLink().fieldname("imageserver").relmodule("VmImageServers").serviceclass(IVmImageServersService.class.getName()).url("VmImageServers/popupview"),
			new OutsideLink().fieldname("zone").relmodule("VmZones").serviceclass(IVmZonesService.class.getName()).url("VmZones/popupview")
		);

		/*弹出对话框配置*/
		popupDialog.search(Collections.singletonList("name")).columns(Arrays.asList("zone","name","pool","current","format","status","state","node","createtime"));

		/*编号配置*/
		/*modentityNums.modulename("").prefix("");*/

		/*模块角色数据验证信息*/
		/*dataPermission.setExpressions(new HashMap<String, List<Expression>>(){{
			put("",Arrays.asList(
				new Expression()
			));
		}});*/

		/*模块API接口配置信息*/
		/*api.modulename("VmImages");*/

	}
}
