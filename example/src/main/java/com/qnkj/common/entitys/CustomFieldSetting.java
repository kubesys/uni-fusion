package com.qnkj.common.entitys;

/**
 * create by 徐雁
 * create date 2021/7/7
 */

public class CustomFieldSetting {
    public String fieldname;
    public String fieldlabel;
    public String grouplabel;
    public Boolean ishidden;

    public CustomFieldSetting(String fieldname,String fieldlabel,Boolean ishidden) {
        this.fieldname = fieldname;
        this.fieldlabel = fieldlabel;
        this.grouplabel = "";
        this.ishidden = ishidden;
    }
    public CustomFieldSetting(String fieldname,String fieldlabel,String grouplabel,Boolean ishidden) {
        this.fieldname = fieldname;
        this.fieldlabel = fieldlabel;
        this.grouplabel = grouplabel;
        this.ishidden = ishidden;
    }

}
