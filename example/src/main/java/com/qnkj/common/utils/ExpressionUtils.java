package com.qnkj.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.github.restapi.XN_Query;
import com.qnkj.common.entitys.Expression;
import com.qnkj.common.entitys.Logic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExpressionUtils {

    private ExpressionUtils() {}

    public static void executeQuery(JSONArray json, XN_Query query) throws Exception {
        try {
            List<Expression> lists = JSON.parseArray(json.toJSONString(), Expression.class);
            String filterStr = execute(lists);
            if (isNotEmpty(filterStr)) {
                query.filter(filterStr);
            }
        }catch (JSONException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static void executeQuery(String json, XN_Query query) throws Exception {
        try {
            List<Expression> lists = JSON.parseArray(json, Expression.class);
            String filterStr = execute(lists);
            if (isNotEmpty(filterStr)) {
                query.filter(filterStr);
            }
        }catch (JSONException e) {
           throw new Exception(e.getMessage());
        }
    }

    public static String execute(List<Expression> lists) throws Exception {
        if (lists.isEmpty()) {
            return "";
        }
        verify(lists);
        StringBuilder sb = new StringBuilder();
        for(Expression item : lists) {
            if (item.getLogic() == Logic.BEGIN) {
                if (isNotEmpty(item.getFieldName()) && isNotEmpty(item.getSymbol()) && isNotEmpty(item.getValue())) {
                    sb.append(fillLeftBrackets(item.getLeftBrackets()));
                    sb.append(checkFilter(item.getFieldName())).append(" ").append(item.getSymbol().getValue()).append(" ").append(getExpressionValue(item.getValue()));
                    sb.append(fillRightBrackets(item.getRightBrackets()));
                }
            } else {
                if (isNotEmpty(item.getLogic()) && isNotEmpty(item.getFieldName()) && isNotEmpty(item.getSymbol()) && isNotEmpty(item.getValue())) {
                    sb.append(item.getLogic().getSymbol());
                    sb.append(fillLeftBrackets(item.getLeftBrackets()));
                    sb.append(checkFilter(item.getFieldName())).append(" ").append(item.getSymbol().getValue()).append(" ").append(getExpressionValue(item.getValue()));
                    sb.append(fillRightBrackets(item.getRightBrackets()));
                }
            }
        }
        return sb.toString();
    }

    public static void executeQuery(List<Expression> lists, XN_Query query) throws Exception {
        String filterStr = execute(lists);
        if (isNotEmpty(filterStr)) {
            query.filter(filterStr);
        }
    }

    private static void verify(List<Expression> lists) throws Exception {
        Integer totalLeftBrackets = lists.stream().mapToInt(v -> {
            if (isEmpty(v.getLeftBrackets())) {
                return 0;
            } else {
                return Integer.parseInt(v.getLeftBrackets());
            }
        }).sum();
        Integer totalRightBrackets = lists.stream().mapToInt(v -> {
            if (isEmpty(v.getRightBrackets())) {
                return 0;
            } else {
                return Integer.parseInt(v.getRightBrackets());
            }
        }).sum();
        if (!totalLeftBrackets.equals(totalRightBrackets)) {
            throw new Exception("左右括弧不匹配,请检查表达式!");
        }
    }
    private static String fillLeftBrackets(String value) {
        if (isNotEmpty(value)) {
            return StringUtils.leftPad("", Integer.parseInt(value), '(');
        }
        return "";
    }
    private static String fillRightBrackets(String value) {
        if (isNotEmpty(value)) {
            return StringUtils.leftPad("", Integer.parseInt(value), ')');
        }
        return "";
    }
    private static String getExpressionValue(String value) {
        if (isNotEmpty(value)) {
            if ("$now".equals(value)) {
                return "'" + DateTimeUtils.getDatetime() + "'";
            } else if ("$today".equals(value)) {
                return "'" + DateTimeUtils.getDatetime("yyyy-MM-dd") + "'";
            } else {
                return "'" + value + "'";
            }
        }
        return "''";
    }



    private static String checkFilter(String fieldname){
        if (!fieldname.startsWith("my.") && !Arrays.asList("id", "title", "author", "published", "updated", "application").contains(fieldname)) {
            return "my." + fieldname;
        } else {
            return fieldname;
        }
    }



    /**
     * 判断对像是否不为空
     * @param obj String List Map
     * @return boolean
     */
    private static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断对像是否为空
     * @param obj String List Map
     * @return boolean
     */
    private static boolean isEmpty(Object obj) {
        return ((obj == null) || (obj instanceof String && obj.toString().isEmpty()) ||
                (obj instanceof List && ((List<?>) obj).isEmpty()) ||
                (obj instanceof Map && ((Map<?,?>) obj).isEmpty()) ||
                (obj instanceof String[] && ((String[])obj).length <= 0));
    }
}
