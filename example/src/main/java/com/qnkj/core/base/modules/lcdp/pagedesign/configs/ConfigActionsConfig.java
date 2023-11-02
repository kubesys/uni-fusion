package com.qnkj.core.base.modules.lcdp.pagedesign.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;
import com.qnkj.core.base.modules.lcdp.pagedesign.services.IpagedesignService;

import java.util.Arrays;

public class ConfigActionsConfig extends BaseActionsConfig {
    public ConfigActionsConfig() {
        actions = Arrays.asList(
                new Action().actionkey("editviewAction").actionlabel("编辑").toggle("navtab").actiontype("editview").order(0),
                new Action().actionkey("Generate").actionlabel("指定模块代码生成页面").securitycheck(false).funclass(IpagedesignService.class.getName()).toggle("ajax").group("ids").order(0).confirm("确定要重新生成选择的模块页面？"),
                new Action().actionkey("exportAssigned").actionlabel("导出指定数据到配置").securitycheck(false).funclass(IpagedesignService.class.getName()).toggle("ajax").group("ids").order(1).confirm("确定要导出指定数据到配置？"),
                new Action().actionkey("exportAll").actionlabel("导出所有数据到配置").securitycheck(false).funclass(IpagedesignService.class.getName()).toggle("ajax").group("ids").order(2).confirm("确定要导出所有数据到配置？"),
                new Action().actionkey("Delete").actionlabel("指定模块清空页面设计").securitycheck(false).funclass(IpagedesignService.class.getName()).toggle("ajax").group("ids").order(3).confirm("确定要清空指定模块页面设计？"),
                new Action().actionkey("ReloadJava").actionlabel("重启Java虚拟机").actionclass("layui-btn-danger").toggle("ajax").confirm("确定要重启Java虚拟机？")
        );
    }
}
