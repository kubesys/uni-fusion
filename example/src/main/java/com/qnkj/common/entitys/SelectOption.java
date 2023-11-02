package com.qnkj.common.entitys;


public class SelectOption {
    public String label = "";
    public String value = "";
    public Boolean selected = false;


    public SelectOption(String value,String label,Boolean selected) {
        this.value = value;
        this.label = label;
        this.selected = selected;
    }

    public SelectOption value(String value) {
        this.value = value;
        return this;
    }
    public SelectOption label(String label) {
        this.label = label;
        return this;
    }
    public SelectOption selected(Boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"label\":\"").append(label.replaceAll("\"","\\\"")).append("\"");
        sb.append(",\"value\":\"").append(value).append("\"");
        sb.append(",\"selected\":\"").append(selected).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
