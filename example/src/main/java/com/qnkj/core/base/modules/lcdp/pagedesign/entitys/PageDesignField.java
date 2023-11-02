package com.qnkj.core.base.modules.lcdp.pagedesign.entitys;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDesignField {

    private String field = "";
    private String label = "";
    private String type = "";
    private Integer uitype = 1;

    public PageDesignField(String label, String field, String type,Integer uitype){
        this.field=field;
        this.label=label;
        this.type=type;
        this.uitype=uitype;
    }
    public PageDesignField(String label, String field, String type){
        this.field=field;
        this.label=label;
        this.type=type;
        this.uitype=1;
    }
}
