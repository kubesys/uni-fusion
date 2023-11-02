package com.qnkj.common.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PageDesignInitInfo extends BaseRecordConfig {

    @ApiModelProperty("菜单组")
    public String program = "";

    @ApiModelProperty("模块组")
    public String parent = "";

    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("模块标签")
    public String module = "";

    @ApiModelProperty("模板")
    public String template_editor = "";


    public PageDesignInitInfo(Object content) {
        this.fromContent(content);
    }

    public String toInitString() {
        return "PageDesignInitInfo.builder()\n\t\t\t\t" +
                ".program(\"" + this.program + "\")\n\t\t\t\t" +
                ".parent(\"" + this.parent + "\")\n\t\t\t\t" +
                ".modulename(\"" + this.modulename + "\")\n\t\t\t\t" +
                ".module(\"" + this.module + "\")\n\t\t\t\t" +
                ".template_editor(\"" + this.template_editor + "\")\n\t\t\t\t" +
                ".build()";
    }
}
