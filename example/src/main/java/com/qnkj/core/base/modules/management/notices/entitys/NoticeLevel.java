package com.qnkj.core.base.modules.management.notices.entitys;

public enum NoticeLevel {
    /**
     *
     */
    info(0,"info","通知"),
    warn(1,"warn","提醒"),
    error(2,"error","错误"),
    fatal(3,"fatal","严重事件");

    private int intval;
    private String strval;
    private String label;

    NoticeLevel(String strval) {
        this.strval = strval;
        if (strval.compareTo("info") == 0) {
            this.intval = 0;
            this.label = "通知";
        } else if (strval.compareTo("warn") == 0) {
            this.intval = 1;
            this.label = "警告";
        } else if (strval.compareTo("error") == 0) {
            this.intval = 2;
            this.label = "错误";
        } else {
            this.intval = 3;
            this.label = "严重事件";
        }
    }

    NoticeLevel(int intval, String strval, String label) {
        this.intval = intval;
        this.strval = strval;
        this.label = label;
    }

    public int getIntval() {
        return intval;
    }
    public String getStrval() {
        return strval;
    }
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.strval;
    }
}
