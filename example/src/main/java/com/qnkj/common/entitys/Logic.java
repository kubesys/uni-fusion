package com.qnkj.common.entitys;

/**
 * 辑辑符
 */
public enum Logic {
    /** */
    AND("and"),
    OR("or"),
    BEGIN("begin");

    private String value;
    private String label;
    private String symbol;

    Logic(String val) {
        this.value = val;
        if (value.compareTo("and") == 0) {
            this.label = "并且";
            this.symbol = "&";
        } else if (value.compareTo("or") == 0) {
            this.label = "或者";
            this.symbol = "|";
        } else {
            this.value = "begin";
            this.label = "开始";
            this.symbol = "";
        }
    }
    public String getValue() {
        return value;
    }
    public String getLabel() {
        return label;
    }
    public String getSymbol() {
        return symbol;
    }
    @Override
    public String toString() {
        return this.value;
    }
}
