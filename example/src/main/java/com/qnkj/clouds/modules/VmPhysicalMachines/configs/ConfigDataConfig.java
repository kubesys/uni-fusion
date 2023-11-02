package com.qnkj.clouds.modules.VmPhysicalMachines.configs;

import com.qnkj.clouds.modules.VmClusters.services.IVmClustersService;
import com.qnkj.clouds.modules.VmZones.services.IVmZonesService;
import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2023-07-09
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("kubestack").parent("Hardwares").label("物理机").icon("iconfont icon-kucun").url("/VmPhysicalMachines/listview").order(3);

		/*模块配置*/
		tabs = new Tab().modulename("VmPhysicalMachines").modulelabel("物理机").tabname("vm_physicalmachines").iscreate(true).isdelete(true).isenabledisable(true).customview(false).isreadonly(true).iscreate(false).isdelete(false).popupeditview(true);

		/*区块配置*/
		blocks = Collections.singletonList(
				new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("zone","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("cluster","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("name","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("state","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("internalip","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("hostname","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("cpu","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("ephemeral_storage","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("memory","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("architecture","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("kernelVersion","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("kubeletVersion","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("operatingSystem","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("osImage","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("uid","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("createtime",""))
		);

		/*字段配置*/
		fields = Arrays.asList(
				new TabField().fieldname("name").fieldlabel("名称").typeofdata("NS~M").issort(true),
				new TabField().fieldname("uid").fieldlabel("UID").issort(true),
				new TabField().fieldname("state").fieldlabel("就绪状态").uitype(19).picklist("vmphysicalmachinestate").issort(true),
				new TabField().fieldname("internalip").fieldlabel("IP"),
				new TabField().fieldname("hostname").fieldlabel("主机名"),
				new TabField().fieldname("cpu").fieldlabel("CPU"),
				new TabField().fieldname("ephemeral_storage").fieldlabel("磁盘容量"),
				new TabField().fieldname("memory").fieldlabel("内存"),
				new TabField().fieldname("architecture").fieldlabel("架构"),
				new TabField().fieldname("kernelVersion").fieldlabel("内核版本"),
				new TabField().fieldname("kubeletVersion").fieldlabel("Kubernetes版本"),
				new TabField().fieldname("operatingSystem").fieldlabel("操作系统"),
				new TabField().fieldname("osImage").fieldlabel("操作系统版本"),
				new TabField().fieldname("createtime").fieldlabel("创建时间").issort(true),
				new TabField().fieldname("zone").fieldlabel("区域").uitype(11).typeofdata("V~M").issort(true),
				new TabField().fieldname("cluster").fieldlabel("集群").uitype(11).typeofdata("V~M").issort(true),
				new TabField().fieldname("status").fieldlabel("启用").uitype(13).displaytype(2).defaultvalue("Active").issort(true).isnumsort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
				new CustomView().viewname("默认").columnlist(Arrays.asList("zone","name","cluster",
						"internalip",
						"hostname",
						"cpu",
						"memory","state","architecture",
						"kernelVersion","kubeletVersion","operatingSystem","osImage","uid","createtime"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Collections.singletonList(
				new SearchColumn().fieldname("state").fieldlabel("就绪状态").order(1).searchtype("select")
		);

		/*关联显示配置*/
		entityNames.fieldname("name");


		outsideLinks = Arrays.asList(
				new OutsideLink().fieldname("zone").relmodule("VmZones").serviceclass(IVmZonesService.class.getName()).url("VmZones/popupview"),
				new OutsideLink().fieldname("cluster").relmodule("VmClusters").serviceclass(IVmClustersService.class.getName()).url("VmClusters/popupview")
		);
	}
}
