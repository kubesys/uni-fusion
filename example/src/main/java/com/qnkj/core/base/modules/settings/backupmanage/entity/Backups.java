package com.qnkj.core.base.modules.settings.backupmanage.entity;

import com.github.restapi.models.Backup;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.ClassUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Backups extends BaseRecordConfig {
    @ApiModelProperty("备份路径")
    public String path = "";

    @ApiModelProperty("备份名称")
    public String backupname = "";

    @ApiModelProperty("备份类型")
    public String style = "";

    public Backups() {
        this.id = "";
    }

    public Backups(Object content) {
        ClassUtils.setValue(this,"id",((Backup) content).getId());
        ClassUtils.setValue(this,"backupname",((Backup) content).getId());
        String published = ((Backup) content).getPublished();
        ClassUtils.setValue(this,"published",published.replace("\\.000",""));
        ClassUtils.setValue(this,"application",((Backup) content).getApplication());
        ClassUtils.setValue(this,"path",((Backup) content).getPath());
        String styleType = ((Backup) content).getStyle();
        if ("User".equals(styleType)) {
            styleType = "用户创建";
        } else {
            styleType = "系统创建";
        }
        ClassUtils.setValue(this,"style",styleType);
    }

}
