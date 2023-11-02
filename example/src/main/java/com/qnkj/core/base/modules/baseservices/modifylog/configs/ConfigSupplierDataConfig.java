package com.qnkj.core.base.modules.baseservices.modifylog.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.Tab;

/**
 * create by 徐雁
 * create date 2021/7/6
 */

public class ConfigSupplierDataConfig extends BaseDataConfig {
    public ConfigSupplierDataConfig() {
        /*菜单配置*/
        moduleMenu = new ModuleMenu().builtin(true).program("supplier").parent("suppliergeneralmanager").label("修改日志").icon("lcdp-icon-yundanzhifujilu").url("/modifylog/listview").order(9);
        /*模块配置*/
        tabs = new Tab().modulename("MultipleEntry:modifylog").modulelabel("修改日志").tabname("modifylog").datarole(1).datatype("YearContent").hasoperator(false).autolineheight(true);
    }
}
