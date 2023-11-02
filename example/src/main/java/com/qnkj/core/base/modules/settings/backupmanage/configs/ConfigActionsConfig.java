package com.qnkj.core.base.modules.settings.backupmanage.configs;

import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.entitys.Action;

import java.util.Arrays;

public class ConfigActionsConfig extends BaseActionsConfig {
    public ConfigActionsConfig() {
        actions = Arrays.asList(
                new Action().actionkey("EditView").actionlabel("新建").toggle("ajax"),
//                new Action().actionkey("BackupPolicy").actionlabel("备份策略").toggle("dialog"),
//                new Action().actionkey("RestoreBackup").actionlabel("还原备份").toggle("dialog").group("ids").multiselect(false),
                new Action().actionkey("Delete").actionlabel("删除").group("ids").toggle("ajax").actionclass("layui-btn-grey").confirm("确定要删除吗?")
        );
    }
}
