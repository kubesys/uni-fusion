package com.qnkj.common.entitys;

/**
 * 操作符
 */
public enum Symbol {
    /** */
    EQUAL("="),
    NOTEQUAL("!="),
    GREATER(">"),
    GREATERANDEQUAL(">="),
    LESS("<"),
    LESSANDEQUAL("<="),
    LIKE("like"),
    NOTLIKE("!like"),
    IN("in"),
    NOTIN("!in");


    private String value;
    private String label;

    public static Symbol of(String value) {
        switch (value) {
            case "!=":
            case "<>":
                return Symbol.NOTEQUAL;
            case ">":
                return Symbol.GREATER;
            case ">=":
                return Symbol.GREATERANDEQUAL;
            case "<":
                return Symbol.LESS;
            case "<=":
                return Symbol.LESSANDEQUAL;
            case "like":
                return Symbol.LIKE;
            case "!like":
                return Symbol.NOTLIKE;
            case "in":
                return Symbol.IN;
            case "!in":
                return Symbol.NOTIN;
            case "=":
            case "==":
            default:
                return Symbol.EQUAL;
        }
    }

    Symbol(String val) {
        this.value = val;
        switch (value) {
            case "=":
                this.label = "等于";
                break;
            case "!=":
            case "<>":
                this.label = "不等于";
                break;
            case ">":
                this.label = "大于";
                break;
            case ">=":
                this.label = "大于等于";
                break;
            case "<":
                this.label = "小于";
                break;
            case "<=":
                this.label = "小于等于";
                break;
            case "like":
                this.label = "模糊等于";
                break;
            case "!like":
                this.label = "模糊不等于";
                break;
            case "in":
                this.label = "包含";
                break;
            case "!in":
                this.label = "不包含";
                break;
            default:
                this.value = "=";
                this.label = "等于";
                break;
        }
    }

    public String getValue() {
        return value;
    }
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
