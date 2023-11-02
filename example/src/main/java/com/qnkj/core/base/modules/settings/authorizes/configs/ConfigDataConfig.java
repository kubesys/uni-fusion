package com.qnkj.core.base.modules.settings.authorizes.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.TabField;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("usermanager").label("角色授权").icon("lcdp-icon-guanlishouquan").order(4).url("/authorizes/listview");

        tabs.modulename("authorizes").modulelabel("角色授权").tabname("authorizes").hasoperator(false).moduletype(1);

        customViews = Arrays.asList(
                new CustomView().viewname("默认").isdefault(true)
                        .columnlist(Arrays.asList("authorize", "userlist"))
        );

        fields = Arrays.asList(
                new TabField().fieldname("authorize").fieldlabel("角色").uitype(19).typeofdata("V~M").picklist("authorize").editwidth("100").listwidth("15%").align("left"),
                new TabField().fieldname("userlist").fieldlabel("授权人").editwidth("100").listwidth("83%").align("left")
        );

        entityNames.fieldname("authorize");

        popupDialog.columns(Arrays.asList("authorize","author","published"))
                .search(new ArrayList<>());

    }
}
