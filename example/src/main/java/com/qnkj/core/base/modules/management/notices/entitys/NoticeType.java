package com.qnkj.core.base.modules.management.notices.entitys;

public enum NoticeType {
    /**
     *
     */
    profile(0,"profile","个人通知"),
    supplier(1,"supplier","客户通知"),
    approval(2,"approval","审核通知"),
    system(3,"system","系统通知");

    private int intval;
    private String strval;
    private String label;

    NoticeType(String strval) {
        this.strval = strval;
        if (strval.compareTo("profile") == 0) {
            this.intval = 0;
            this.label = "个人通知";
        } else if (strval.compareTo("supplier") == 0) {
            this.intval = 1;
            this.label = "客户通知";
        } else if (strval.compareTo("approval") == 0) {
            this.intval = 2;
            this.label = "审核通知";
        } else {
            this.intval = 3;
            this.label = "系统通知";
        }
    }

    NoticeType(int intval, String strval, String label) {
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
