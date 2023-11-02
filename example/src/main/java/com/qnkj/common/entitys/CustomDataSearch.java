package com.qnkj.common.entitys;

import com.qnkj.common.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * create by 徐雁
 * create date 2020/12/1
 */

public class CustomDataSearch extends HashMap<String, Object> {
    @Override
    public CustomDataSearch put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 自定义查询字段名称
     */
    public CustomDataSearch fieldname(String fieldname){
        this.put("filename",fieldname);
        return this;
    }

    public String fieldname(){
        return this.getOrDefault("filename","").toString();
    }

    /**
     * 查询字段的标签
     */
    public CustomDataSearch fieldlabel(String fieldlabel){
        this.put("fieldlabel",fieldlabel);
        return this;
    }

    public String fieldlabel(){
        return this.getOrDefault("fieldlabel","").toString();
    }

    /**
     * 查询字段的提示
     */
    public CustomDataSearch tip(String tip){
        this.put("tip",tip);
        return this;
    }

    public String tip(){
        return this.getOrDefault("tip","").toString();
    }

    /**
     * 附加信息
     * 输入框或下拉框生效
     */
    public CustomDataSearch aux(String aux) {
        this.put("aux",aux);
        return this;
    }

    public String aux() {
        return this.getOrDefault("aux","").toString();
    }
    /**
     * 查询界面显示类型
     * @param searchtype search_input，vague_input，calendar，text，select
     *                   search_input：单字段精准匹配类型
     *                   vague_input：单字段模糊匹配类型
     *                   calendar：日期类型
     *                   text：文本选择类型
     *                   select：下拉选择类型
     *                   tree_select：树型下拉选择类型
     */
    public CustomDataSearch searchtype(String searchtype){
        this.put("type",searchtype);
        return this;
    }

    public String searchtype(){
        return this.getOrDefault("type","").toString();
    }
    /**
     *  除calendar类型之外的查询值
     */
    public CustomDataSearch searchvalue(String value){
        this.put("value",value);
        return this;
    }

    public String searchvalue(){
        return this.getOrDefault("value","").toString();
    }

    /**
     * calendar类型的开始值
     */
    public CustomDataSearch start(String value){
        this.put("start",value);
        return this;
    }
    public String start(){
        return this.getOrDefault("start","").toString();
    }

    /**
     * calendar类型的结束值
     */
    public CustomDataSearch end(String value){
        this.put("end",value);
        return this;
    }
    public String end(){
        return this.getOrDefault("end","").toString();
    }

    /**
     * calendar类型是否
     */
    public CustomDataSearch quickbtn(Boolean quickbtn) {
        this.put("quickbtn",quickbtn);
        return this;
    }
    public Boolean quickbtn(){
        return Boolean.parseBoolean(this.getOrDefault("quickbtn","false").toString());
    }

    /**
     * text，select类型的选项
     */
    public CustomDataSearch picklist(String value) {
        this.put("picklist",value);
        return this;
    }
    public String picklist(){
        return this.getOrDefault("picklist","").toString();
    }
    public CustomDataSearch pickintval(Boolean intval){
        this.put("pickintval",intval);
        return this;
    }
    public Boolean pickintval(){
        return Boolean.parseBoolean(this.getOrDefault("pickintval","false").toString());
    }

    /**
     * text，select类型自定义选项
     */
    public CustomDataSearch options(List<SelectOption> options){
        if(!options.isEmpty()) {
            this.put("options",options);
        }
        return this;
    }

    public List<SelectOption> options(){
        if(!Utils.isEmpty(this.get("options"))){
            return (List<SelectOption>)this.get("options");
        }
        return new ArrayList<>();
    }

    /**
     * treeSelect类型自定义选项
     */
    public CustomDataSearch treeOptions(List<TreeSelectOption> treeOptions){
        if(!treeOptions.isEmpty()) {
            this.put("treeOptions",treeOptions);
        }
        return this;
    }

    public List<TreeSelectOption> treeOptions(){
        if(!Utils.isEmpty(this.get("treeOptions"))){
            return (List<TreeSelectOption>)this.get("treeOptions");
        }
        return new ArrayList<>(1);
    }

    /**
     * select类型能否输入筛选
     */
    public CustomDataSearch hasSearch(Boolean select) {
        if(select){
            this.put("hassearch", true);
        }
        return this;
    }
    public Boolean hasSearch(){
        return Boolean.parseBoolean(this.getOrDefault("hassearch","false").toString());
    }
    /**
     * 占用列数
     */
    public CustomDataSearch colspan(Integer colspan){
        this.put("colspan",colspan);
        return this;
    }

    public Integer colspan(){
        return Integer.parseInt(this.getOrDefault("colspan",1).toString(),10);
    }

    /**
     * 是否新启行显示
     */
    public CustomDataSearch newline(Boolean newline){
        this.put("newline",newline);
        return this;
    }
    public Boolean newline(){
        return Boolean.parseBoolean(this.getOrDefault("newline","false").toString());
    }

    public CustomDataSearch width(Integer width){
        this.put("width",width);
        return this;
    }

    public Integer width(){
        return Integer.parseInt(this.getOrDefault("width",0).toString(),10);
    }

    public CustomDataSearch order(Integer order){
        this.put("order",order);
        return this;
    }
    public Integer order(){
        return Integer.parseInt(this.getOrDefault("order",0).toString(),10);
    }
}
