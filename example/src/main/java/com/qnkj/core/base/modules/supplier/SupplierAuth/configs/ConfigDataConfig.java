package com.qnkj.core.base.modules.supplier.SupplierAuth.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.TabField;

import java.util.Arrays;

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("supplier").parent("suppliersettings").label("角色授权").icon("lcdp-icon-guanlishouquan").order(4).url("/supplierauth/listview");

        tabs.modulename("SupplierAuth").modulelabel("角色授权").tabname("supplier_authorizes").hasoperator(false).moduletype(1);

        customViews = Arrays.asList(
                new CustomView().viewname("默认").isdefault(true)
                        .columnlist(Arrays.asList("authorize", "userlist"))
        );

        fields = Arrays.asList(
                new TabField().fieldname("authorize").fieldlabel("角色").uitype(19).typeofdata("V~M").picklist("authorize").editwidth("100").listwidth("15%").align("left"),
                new TabField().fieldname("userlist").fieldlabel("授予人").editwidth("100").listwidth("83%").align("left")
        );
    }
}
