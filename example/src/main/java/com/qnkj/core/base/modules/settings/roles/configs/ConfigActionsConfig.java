package com.qnkj.core.base.modules.settings.roles.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;

import java.util.Arrays;

public class ConfigActionsConfig extends BaseActionsConfig {
    public ConfigActionsConfig() {
        actions = Arrays.asList(
                new Action().actionkey("EditView").actionlabel("新建").toggle("dialog").digwidth("500px")
        );
    }
}
