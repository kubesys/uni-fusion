package com.qnkj.common.entitys;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrintField {

    private String field = "";
    private String label = "";

    public PrintField(String label,String field){
        this.field=field;
        this.label=label;
    }
}
