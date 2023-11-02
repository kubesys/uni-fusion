package com.qnkj.common.entitys;

import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public class PickList {
    @ApiModelProperty("字典名称")
    public String name = "";

    @ApiModelProperty("字典标签")
    public String label = "";

    @ApiModelProperty("字典标签")
    public Boolean builtin = false;

    @ApiModelProperty("字典实体")
    public List<PickListEntity> entitys = new ArrayList<>();

    /**
     * 字典字段名称
     */
    public PickList picklistname(String name){
        this.name = name;
        return this;
    }

    /**
     * 字典字段标签
     */
    public PickList picklistlabel(String label){
        this.label = label;
        return this;
    }

    public PickList picklistentitys(List<PickListEntity> entitys){
        this.entitys = entitys;
        return this;
    }

    public PickList builtin(Boolean builtin) {
        this.builtin = builtin;
        return this;
    }

    public PickList() { }
    public PickList(Object picklist) {
        if(picklist instanceof HashMap){
            this.builtin = (Boolean) ((HashMap) picklist).getOrDefault("builtin",false);
            this.label =  ((HashMap) picklist).getOrDefault("label","").toString();
            this.name = ((HashMap) picklist).getOrDefault("name","").toString();
            if(!Utils.isEmpty(((HashMap<?, ?>) picklist).get("entitys"))){
                for(Object obj: (List)((HashMap) picklist).get("entitys")){
                    this.entitys.add(new PickListEntity()
                            .id(((HashMap)obj).getOrDefault("id","").toString())
                            .label(((HashMap)obj).getOrDefault("label","").toString())
                            .sequence(Integer.parseInt(((HashMap)obj).getOrDefault("sequence","").toString(),10))
                            .intval(Integer.parseInt(((HashMap)obj).getOrDefault("intval","").toString(),10))
                            .strval(((HashMap)obj).getOrDefault("strval","").toString())
                            .styclass(((HashMap)obj).getOrDefault("styclass","").toString())
                            .deleted(Integer.parseInt(((HashMap)obj).getOrDefault("deleted","0").toString(),10)));
                }
            }
        }
    }

    @Override
    public String toString() {
        return this.toString("");
    }

    public String toString(String indent) {
        if(Utils.isEmpty(indent)) {
            indent = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("new PickList()");
        sb.append(".picklistname(\"").append(Utils.replaceString(this.name)).append("\")");
        sb.append(".picklistlabel(\"").append(Utils.replaceString(this.label)).append("\")");
        if(this.builtin){
            sb.append(".builtin(true)");
        }
        if (!this.entitys.isEmpty()) {
            List<String> lists = new ArrayList<>();
            for(int i=0;i<this.entitys.size();i++) {
                lists.add(this.entitys.get(i).toString());
            }
            sb.append(".picklistentitys(\n").append(indent).append("\tArrays.asList(\n").append(indent).append("\t\t").append(String.join(",\n" + indent + "\t\t", lists)).append("\n").append(indent).append("\t)\n").append(indent).append(")");
        } else {
            sb.append(indent).append(".picklistentitys(Arrays.asList())");
        }
        return sb.toString();
    }
}
