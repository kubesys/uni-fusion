package com.github.restapi;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oldhand
 **/

@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
public class XN_Filter {
    public static String filter(String field, String operator, Object value) {
        if (value == null) {
            return field + " " + operator + " null";
        }
        String runtimeType = value.getClass().getTypeName();
        if ("in".equals(operator) || "!in".equals(operator)) {
            if (value instanceof List || value instanceof String[] || value instanceof Set){
                List<Object> lists;
                if(value instanceof List) {
                    lists = (List<Object>) value;
                }else if(value instanceof Set) {
                    lists = new ArrayList<>((Set<Object>)value);
                }else {
                    lists = Arrays.asList((String[]) value);
                }
                if (!lists.isEmpty()) {
                    List<String> newlists = lists.stream().map(String::valueOf).collect(Collectors.toList());
                    if (lists.get(0) instanceof Integer || lists.get(0) instanceof Long) {
                        return field + " " + operator + " [" + StringUtils.join(newlists,",") + "]";
                    } else {
                        List<Object> tmp = new ArrayList<>();
                        for(Object item: lists){
                            if(item instanceof String){
                                tmp.add(((String) item).replace("'","%27"));
                            } else {
                                tmp.add(item);
                            }
                        }
                        return field + " " + operator + " ['" + StringUtils.join(tmp,"','") + "']";
                    }
                }
            }
        } else {
            if (value instanceof Integer || value instanceof Long) {
                return field + " " + operator + " " + value + "";
            } else {
                return field + " " + operator + " '" + transcode(value.toString()) + "'";
            }
        }
        return "";
    }
    public static String transcode(String value) {
        return value.replace("\"","&#34;")
                .replace("'","&#39;");
    }
    public static String all(String filter,String filter1) {
        return all(filter,filter1,null,null,null,null,null);
    }
    public static String all(String filter,String filter1,String filter2) {
        return all(filter,filter1,filter2,null,null,null,null);
    }
    public static String all(String filter,String filter1,String filter2,String filter3) {
        return all(filter,filter1,filter2,filter3,null,null,null);
    }
    public static String all(String filter,String filter1,String filter2,String filter3,String filter4) {
        return all(filter,filter1,filter2,filter3,filter4,null,null);
    }
    public static String all(String filter,String filter1,String filter2,String filter3,String filter4,String filter5) {
        return all(filter,filter1,filter2,filter3,filter4,filter5,null);
    }
    public static String all(String filter,String filter1,String filter2,String filter3,String filter4,String filter5,String filter6) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(filter);
        if (filter1 != null && !filter1.isEmpty()){
            sb.append(" & ").append(filter1);
        }
        if (filter2 != null && !filter2.isEmpty()){
            sb.append(" & ").append(filter2);
        }
        if (filter3 != null && !filter3.isEmpty()){
            sb.append(" & ").append(filter3);
        }
        if (filter4 != null && !filter4.isEmpty()){
            sb.append(" & ").append(filter4);
        }
        if (filter5 != null && !filter5.isEmpty()){
            sb.append(" & ").append(filter5);
        }
        if (filter6 != null && !filter6.isEmpty()){
            sb.append(" & ").append(filter6);
        }
        sb.append(')');
        return sb.toString();
    }

    public static String all(List<?> filters){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Object filter : filters){
            if (!filter.toString().isEmpty()){
                if (filters.contains(filter)){
                    sb.append(" & ");
                }
                sb.append(filter);
            }
        }
        sb.append(')');
        return sb.toString();
    }

    public static String any(String filter,String filter1) {
        return any(filter,filter1,null,null,null,null,null);
    }
    public static String any(String filter,String filter1,String filter2) {
        return any(filter,filter1,filter2,null,null,null,null);
    }
    public static String any(String filter,String filter1,String filter2,String filter3) {
        return any(filter,filter1,filter2,filter3,null,null,null);
    }
    public static String any(String filter,String filter1,String filter2,String filter3,String filter4) {
        return any(filter,filter1,filter2,filter3,filter4,null,null);
    }
    public static String any(String filter,String filter1,String filter2,String filter3,String filter4,String filter5) {
        return any(filter,filter1,filter2,filter3,filter4,filter5,null);
    }
    public static String any(String filter,String filter1,String filter2,String filter3,String filter4,String filter5,String filter6) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(filter);
        if (filter1 != null && !filter1.isEmpty()){
            sb.append(" | ").append(filter1);
        }
        if (filter2 != null && !filter2.isEmpty()){
            sb.append(" | ").append(filter2);
        }
        if (filter3 != null && !filter3.isEmpty()){
            sb.append(" | ").append(filter3);
        }
        if (filter4 != null && !filter4.isEmpty()){
            sb.append(" | ").append(filter4);
        }
        if (filter5 != null && !filter5.isEmpty()){
            sb.append(" | ").append(filter5);
        }
        if (filter6 != null && !filter6.isEmpty()){
            sb.append(" | ").append(filter6);
        }
        sb.append(')');
        return sb.toString();
    }

    public static String any(List<?> filters){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Object filter : filters){
            if (StringUtils.isNotEmpty(filter.toString())){
                if (filters.contains(filter)){
                    sb.append(" | ");
                }
                sb.append(filter);
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
