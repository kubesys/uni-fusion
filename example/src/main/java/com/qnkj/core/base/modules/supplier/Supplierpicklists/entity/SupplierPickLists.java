package com.qnkj.core.base.modules.supplier.Supplierpicklists.entity;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class SupplierPickLists {
    @ApiModelProperty("字段名称")
    public String name = "";

    @ApiModelProperty("字典列表")
    public List<SupplierPickList> picklists = new ArrayList<>();

    public SupplierPickLists() {}

    public SupplierPickLists(String name, List<Object> result) {
        this.name = name;
        for(Object item : result){
            if(item instanceof Content) {
                SupplierPickList pickList = new SupplierPickList(item);
                this.picklists.add(pickList);
            }
        }
    }
}

class SupplierPickList extends BaseRecordConfig {
    @ApiModelProperty("字典")
    public String strval = "";
    @ApiModelProperty("字典名字")
    public String label = "";
    @ApiModelProperty("字典值")
    public int intval = 0;
    @ApiModelProperty("字典排序")
    public int sequence = 0;

    public SupplierPickList() {
        this.id = "";
    }

    public SupplierPickList(Object content) {
        this.id = "";
        this.fromContent(content);
    }

}
