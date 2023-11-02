package com.qnkj.common.entitys;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrintConfig {

    private String tablename;
    private String isList="0";
    private String title;
    private List<PrintField> children;
}
