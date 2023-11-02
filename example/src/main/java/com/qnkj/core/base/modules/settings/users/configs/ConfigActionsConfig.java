package com.qnkj.core.base.modules.settings.users.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;
import com.qnkj.core.base.modules.settings.users.service.IUsersService;

import java.util.Arrays;

public class ConfigActionsConfig extends BaseActionsConfig {
    public ConfigActionsConfig() {
        actions = Arrays.asList(
                new Action().actionkey("Active").actionlabel("启用").group("ids").icon("lcdp-icon-qiyong").toggle("ajax").confirm("确定要启用吗？").funclass(IUsersService.class.getName()).funpara("Active"),
                new Action().actionkey("Inactive").actionlabel("停用").group("ids").icon("lcdp-icon-tingyong").actionclass("layui-btn-grey").toggle("ajax").confirm("确定要停用吗？").funclass(IUsersService.class.getName()).funpara("Inactive"),
                new Action().actionkey("ChanageAccount").actionlabel("修改账号").multiselect(false).group("ids").toggle("dialog").funpara("ChanageAccount").securitycheck(true).digwidth("500px").funclass(IUsersService.class.getName()),
                new Action().actionkey("ModifyPassword").actionlabel("修改密码").multiselect(false).group("ids").toggle("dialog").funpara("ModifyPassword").securitycheck(true).digwidth("500px").funclass(IUsersService.class.getName())
        );
    }
}
