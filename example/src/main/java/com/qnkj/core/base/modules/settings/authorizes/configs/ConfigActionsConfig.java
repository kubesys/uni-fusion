package com.qnkj.core.base.modules.settings.authorizes.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;
import com.qnkj.core.base.modules.settings.authorizes.service.IAuthorizesService;

import java.util.Arrays;

public class ConfigActionsConfig extends BaseActionsConfig {
    public ConfigActionsConfig() {
        actions = Arrays.asList(
                new Action().actionkey("Authorize").actionlabel("授权").toggle("dialog").digwidth("700px").digheight("500px").group("authorizeids").funclass(IAuthorizesService.class.getName()).funpara("Authorize"),
                new Action().actionkey("CancelAuthorize").actionlabel("取消授权").toggle("ajax").group("authorizeids").actionclass("layui-btn-grey").confirm("确定要取消所有授权吗？").funclass(IAuthorizesService.class.getName()).funpara("CancelAuthorize")
        );
    }
}
