package com.qnkj.common.entitys;

import com.github.restapi.models.Content;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

@Getter
public class Expression {

    private String fieldName = "";
    private String fieldLabel = "";
    private String logicfieldLabel = "";
    private String leftBrackets = "";
    private String rightBrackets = "";
    private Logic logic = Logic.BEGIN;
    private Symbol symbol = Symbol.EQUAL;
    private String value = "";
    private String valueLabel = "";

    public Expression() {

    }
    @SuppressWarnings("unchecked")
    public Expression(Object expression) {
        if(expression instanceof Content) {
            this.setFieldLabel(((Content)expression).get("fieldlabel").toString());
            this.setFieldName(((Content)expression).get("fieldname").toString());
            this.setLeftBrackets(((Content)expression).get("leftbrackets").toString());
            this.setLogic(((Content)expression).get("logic").toString());
            this.setValue(((Content)expression).get("fieldvalue").toString());
            this.setRightBrackets(((Content)expression).get("rightbrackets").toString());
            this.setSymbol(((Content)expression).get("symbol").toString());
            this.setValueLabel(((Content)expression).get("valuelabel").toString());
        }else if(expression instanceof Map) {
            this.setFieldLabel(((Map<String,Object>)expression).getOrDefault("fieldLabel","").toString());
            this.setFieldName(((Map<String,Object>)expression).get("fieldName").toString());
            this.setLeftBrackets(((Map<String,Object>)expression).get("leftBrackets").toString());
            this.setLogic(((Map<String,Object>)expression).get("logic").toString());
            this.setValue(((Map<String,Object>)expression).get("value").toString());
            this.setRightBrackets(((Map<String,Object>)expression).get("rightBrackets").toString());
            this.setSymbol(((Map<String,Object>)expression).get("symbol").toString());
            this.setValueLabel(((Map<String,Object>)expression).get("valueLabel").toString());
        }
    }

    public Expression setLogic(String value) {
        if (Arrays.asList("and","or").contains(value)) {
            this.logic = Logic.valueOf(value);
        } else {
            this.logic = Logic.BEGIN;
        }
        return this;
    }
    public Expression setSymbol(String value) {
        this.symbol = Symbol.of(value);
        return this;
    }

    public Expression setRightBrackets(String rightBrackets) {
        this.rightBrackets = rightBrackets;
        return this;
    }

    public Expression setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public Expression setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
        return this;
    }

    public Expression setLogicfieldLabel(String logicfieldLabel) {
        this.logicfieldLabel = logicfieldLabel;
        return this;
    }

    public Expression setValue(String value) {
        this.value = value;
        return this;
    }

    public Expression setValueLabel(String valueLabel) {
        this.valueLabel = valueLabel;
        return this;
    }

    public Expression setLeftBrackets(String leftBrackets) {
        this.leftBrackets = leftBrackets;
        return this;
    }

    public Expression(String leftBrackets, String logic, String fieldName, String fieldLabel,
                      String symbol, String value, String valueLabel, String rightBrackets){
        this.leftBrackets = leftBrackets;
        this.logic = Logic.valueOf(logic);
        this.fieldName = fieldName;
        this.fieldLabel = fieldLabel;
        this.symbol =  Symbol.valueOf(symbol);
        this.value = value;
        this.valueLabel = valueLabel;
        this.rightBrackets = rightBrackets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"logic\":\"").append(logic).append("\"");
        sb.append(",\"leftBrackets\":\"").append(leftBrackets).append("\"");
        sb.append(",\"fieldName\":\"").append(fieldName).append("\"");
        sb.append(",\"fieldLabel\":\"").append(fieldLabel).append("\"");
        sb.append(",\"symbol\":\"").append(symbol).append("\"");
        sb.append(",\"value\":\"").append(value).append("\"");
        sb.append(",\"valueLabel\":\"").append(valueLabel).append("\"");
        sb.append(",\"rightBrackets\":\"").append(rightBrackets).append("\"");
        sb.append("}\n");
        return sb.toString();
    }
}
