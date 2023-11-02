package com.qnkj.core.base.modules.settings.picklistsmanage.entity;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class PickLists {
    @ApiModelProperty("字段名称")
    public String name = "";

    @ApiModelProperty("字典列表")
    public List<PickList> picklists = new ArrayList<>();

    public PickLists() {}

    public PickLists(String name, List<Object> result) {
        this.name = name;
        for(Object item : result){
            if(item instanceof Content) {
                PickList pickList = new PickList(item);
                this.picklists.add(pickList);
            }
        }
    }
}

class PickList extends BaseRecordConfig {
    @ApiModelProperty("字典")
    public String strval = "";
    @ApiModelProperty("字典名字")
    public String label = "";
    @ApiModelProperty("字典值")
    public int intval = 0;
    @ApiModelProperty("字典排序")
    public int sequence = 0;

    public PickList() {
        this.id = "";
    }

    public PickList(Object content) {
        this.id = "";
        this.fromContent(content);
    }

}
