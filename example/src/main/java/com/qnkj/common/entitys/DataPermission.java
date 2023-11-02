package com.qnkj.common.entitys;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2021/12/14
 * create time 3:34 下午
 */

public class DataPermission {
    @ApiModelProperty("模块名称")
    protected String modulename;
    @ApiModelProperty("验证条件")
    protected Map<String,List<Expression>> expressions;

    public String getModulename() {
        return modulename;
    }

    public Map<String,List<Expression>> getExpressions() {
        if(expressions == null) {
            expressions = new HashMap<>(1);
        }
        return expressions;
    }

    public DataPermission setModulename(String modulename) {
        this.modulename = modulename;
        return this;
    }

    public DataPermission setExpressions(Map<String,List<Expression>> expressions) {
        this.expressions = expressions;
        return this;
    }

    public String toInitString() {
        StringBuilder sb = new StringBuilder();
        sb.append("dataPermission.setExpressions(new HashMap<String, List<Expression>>()");
        if(!this.getExpressions().isEmpty()) {
            sb.append("{{\n");
            for(String rolename: this.getExpressions().keySet()){
                if(this.getExpressions().get(rolename).isEmpty()) {
                    continue;
                }
                sb.append("\t\t\tput(\"").append(rolename).append("\",");
                if(this.getExpressions().get(rolename).size() > 1) {
                    sb.append("Arrays.asList(\n");
                } else {
                    sb.append("Collections.singletonList(\n");
                }
                List<String> expStr = new ArrayList<>(1);
                for(Expression expression: this.getExpressions().get(rolename)){
                    if(expression.getFieldName().isEmpty()) {
                        continue;
                    }
                    String expsb = "\t\t\t\tnew Expression()";
                    if(!expression.getLogic().getValue().isEmpty() && !"begin".equals(expression.getLogic().getValue())){
                        expsb += ".setLogic(\"" + expression.getLogic().getValue() + "\")";
                    }
                    if(!expression.getLeftBrackets().isEmpty()){
                        expsb += ".setLeftBrackets(\"" + expression.getLeftBrackets() + "\")";
                    }
                    expsb += ".setFieldName(\"" + expression.getFieldName() + "\")" +
                            ".setSymbol(\"" + expression.getSymbol().getValue() + "\")" +
                            ".setValue(\"" + expression.getValue() + "\")";
                    if(!expression.getRightBrackets().isEmpty()) {
                        expsb += ".setRightBrackets(\"" + expression.getRightBrackets() + "\")";
                    }
                    expStr.add(expsb);
                }
                if(!expStr.isEmpty()){
                    sb.append(String.join(",\n",expStr));
                }
                sb.append("\n\t\t\t));\n");
            }
            sb.append("\t\t}}");
        }
        sb.append(")");
        return sb.toString();
    }
}
