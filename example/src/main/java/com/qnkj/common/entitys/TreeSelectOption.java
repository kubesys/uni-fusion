package com.qnkj.common.entitys;


public class TreeSelectOption {
    public String label = "";
    public String id = "";
    public String parentid = "";
    public Boolean selected = false;


    public TreeSelectOption(String id, String parentid, String label, Boolean selected) {
        this.id = id;
        this.parentid = parentid;
        this.label = label;
        this.selected = selected;
    }

    public TreeSelectOption id(String id) {
        this.id = id;
        return this;
    }
    public TreeSelectOption parentid(String parentid) {
        this.parentid = parentid;
        return this;
    }
    public TreeSelectOption label(String label) {
        this.label = label;
        return this;
    }
    public TreeSelectOption selected(Boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":\"").append(id).append("\"");
        sb.append(",\"parentid\":\"").append(parentid).append("\"");
        sb.append(",\"label\":\"").append(label).append("\"");
        sb.append(",\"selected\":\"").append(selected).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
