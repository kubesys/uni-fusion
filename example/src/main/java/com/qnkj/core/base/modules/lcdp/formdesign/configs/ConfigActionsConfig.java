package com.qnkj.core.base.modules.lcdp.formdesign.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;

import java.util.Arrays;

public class ConfigActionsConfig extends BaseActionsConfig {
    public ConfigActionsConfig() {
        actions = Arrays.asList(
                new Action().actionkey("Generate").actionlabel("指定模块代码生成").toggle("ajax").group("ids").confirm("确定要重新生成选择的模块代码？"),
                new Action().actionkey("GenerateAll").actionlabel("所有模块代码生成").actionclass("layui-btn-danger").toggle("ajax").confirm("确定要重新生成所有模块代码？"),
                new Action().actionkey("ReloadJava").actionlabel("重启Java虚拟机").actionclass("layui-btn-danger").toggle("ajax").confirm("确定要重启Java虚拟机？")
        );
    }
}
