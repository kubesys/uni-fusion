package com.qnkj.core.base.modules.settings.departments.entity;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Departments extends BaseRecordConfig {
    @ApiModelProperty("部门父级ID")
    public String parentid = "";
    @ApiModelProperty("部门名称")
    public String departmentname = "";
    @ApiModelProperty("部门排序")
    public int sequence = 0;
    @ApiModelProperty("部门领导")
    public List<String> leadership = new ArrayList<>();
    @ApiModelProperty("主管领导")
    public String mainleadership = "";
    @ApiModelProperty("是否隐藏")
    public boolean ishide = false;

    public Departments() {
        this.id = "";
    }

    public Departments(Object content) {
        this.id = "";
        if(content instanceof HashMap){
            this.fromRequest(content);
        }else if(content instanceof Content) {
            this.fromContent(content);
        }
    }
}
