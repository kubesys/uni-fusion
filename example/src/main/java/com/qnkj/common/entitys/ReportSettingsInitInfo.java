package com.qnkj.common.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReportSettingsInitInfo  extends BaseRecordConfig {
    @ApiModelProperty("报表名称")
    public String reportname = "";

    @ApiModelProperty("归属分组")
    public String reportgroup = "";

    @ApiModelProperty("归属分类")
    public String reporttype = "";

    @ApiModelProperty("归属模块")
    public String modulename = "";

    @ApiModelProperty("归属菜单")
    public String ofmenu = "";

    @ApiModelProperty("设计数据")
    public String template = "";

    @ApiModelProperty("报表用户")
    public List<String> reportuser = new ArrayList<>();

    @ApiModelProperty("启用")
    public String status = "";


    public ReportSettingsInitInfo(Object content) {
        this.fromContent(content);
    }


    public String toInitString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReportSettingsInitInfo.builder()\n\t\t\t\t");
        sb.append(".reportname(\"").append(Utils.replaceString(this.reportname)).append("\")\n\t\t\t\t");
        sb.append(".reportgroup(\"").append(Utils.replaceString(this.reportgroup)).append("\")\n\t\t\t\t");
        sb.append(".reporttype(\"").append(this.reporttype).append("\")\n\t\t\t\t");
        sb.append(".modulename(\"").append(this.modulename).append("\")\n\t\t\t\t");
        sb.append(".template(\"").append(this.template).append("\")\n\t\t\t\t");
        sb.append(".ofmenu(\"").append(this.ofmenu).append("\")\n\t\t\t\t");
        sb.append(".build()");
        return sb.toString();
    }

}
