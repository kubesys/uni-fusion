package com.qnkj.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qnkj.common.entitys.TabField;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


/**
 * @author clubs
 */
@Slf4j
public class Utils {

    private Utils() {}

    private static boolean startComplete = false;

    public static boolean isStartComplete() {
        return startComplete;
    }

    public static void startComplete() {
        startComplete = true;
    }

    /**
     * 在字符串数组中添加一个元素
     *
     * @param strings 字符串数组
     * @param string  添加的字符串
     * @return 新的字符串数组
     */
    private static String[] insertString(String[] strings, String string) {
        int size = strings.length;
        String[] newStr = new String[size + 1];
        System.arraycopy(strings, 0, newStr, 0, size);
        newStr[size] = string;
        return newStr;
    }
    private static boolean isJSON(String str) {
        boolean result = false;
        try {
            if((str.startsWith("{") && str.endsWith("}")) || (str.startsWith("[") && str.endsWith("]"))) {
                result = isEmpty(JSONObject.parse(str)) ? false : true;
            }
        } catch (Exception ignored) {
            result = false;
        }
        return result;
    }
    /**
     * 获取网络请求中参数
     *
     * @param request HttpServletRequest
     * @return HashMap
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getRequestQuery(HttpServletRequest request) {
        Map<String, Object> queryMap = new HashMap<>();
        Enumeration<String> parameteNames = request.getParameterNames();
        while(parameteNames.hasMoreElements()){
            String parameteName = parameteNames.nextElement();
            if((parameteName.startsWith("{") && parameteName.endsWith("}")) || (parameteName.startsWith("[") && parameteName.endsWith("]"))){
                Object obj = Utils.jsonToObject(parameteName);
                if(!isEmpty(obj)){
                    queryMap.put("data",obj);
                }
            }else {
                String[] parameteValues = request.getParameterValues(parameteName);
                Object parameteValue;
                if (parameteValues.length == 1) {
                    if ("on".equals(parameteValues[0])) {
                        parameteValue = true;
                    } else {
                        parameteValue = parameteValues[0];
                    }
                } else if (parameteValues.length > 1) {
                    parameteValue = Arrays.asList(parameteValues);
                } else {
                    parameteValue = "";
                }
                if (parameteValue instanceof String) {
                    try {
                        parameteValue = URLDecoder.decode(parameteValue.toString(),"UTF-8");
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    if (isJSON(parameteValue.toString())){
                        Object obj = Utils.jsonToObject(parameteValue.toString());
                        if (!isEmpty(obj)) {
                            parameteValue = obj;
                        } else {
                            parameteValue = "";
                        }
                    }
                }
                if (parameteName.matches("(.*)\\[(\\d*)\\]$")) {
                    parameteName = parameteName.substring(0, parameteName.indexOf("["));
                } else if (parameteName.endsWith(".id")) {
                    parameteName = parameteName.substring(0, parameteName.indexOf("."));
                    if(parameteName.contains("[") && parameteName.endsWith("]")){
                        String parameteKey = parameteName.substring(parameteName.indexOf("[")+1,parameteName.indexOf("]"));
                        if(Utils.isNotEmpty(parameteKey)){
                            parameteName = parameteName.substring(0, parameteName.indexOf("["));
                            if(Utils.isNotEmpty(parameteValue)) {
                                Map<String,Object> info = new HashMap<>(1);
                                info.put(parameteKey, Arrays.asList(parameteValue.toString().split(",")));
                                parameteValue = info;
                            } else {
                                parameteValue = new HashMap<String, Object>();
                            }
                        }
                    }else if (parameteValue.toString().contains(",")) {
                        parameteValue = Arrays.asList(parameteValue.toString().split(","));
                    }
                }
                if(queryMap.containsKey(parameteName)){
                    if(queryMap.get(parameteName) instanceof List) {
                        if (parameteValue instanceof List) {
                            ((List<Object>) queryMap.get(parameteName)).addAll((List<?>)parameteValue);
                        } else {
                            ((List<Object>) queryMap.get(parameteName)).add(parameteValue);
                        }
                    }else if(queryMap.get(parameteName) instanceof Map) {
                        for(Object item: ((Map<?,?>)parameteValue).keySet()){
                            if(((Map<?,?>)parameteValue).get(item) instanceof List){
                                if(Utils.isNotEmpty(((Map<Object, Object>) queryMap.get(parameteName)).get(item))) {
                                    ((List<Object>) ((Map<Object, Object>) queryMap.get(parameteName)).get(item)).addAll((List<?>) ((Map<?,?>) parameteValue).get(item));
                                } else {
                                    ((Map<Object, Object>) queryMap.get(parameteName)).put(item, ((Map<?,?>) parameteValue).get(item));
                                }
                            }else {
                                ((Map<Object, Object>) queryMap.get(parameteName)).put(item, ((Map<?,?>) parameteValue).get(item));
                            }
                        }
                    } else {
                        queryMap.put(parameteName,new ArrayList<>(Arrays.asList(queryMap.get(parameteName),parameteValue)));
                    }
                }else {
                    queryMap.put(parameteName, parameteValue);
                }
            }
        }
        parameteNames = request.getAttributeNames();
        while(parameteNames.hasMoreElements()){
            String parameteName = parameteNames.nextElement();
            if(!parameteName.contains(".")){
                Object parameteValue = request.getAttribute(parameteName);
                queryMap.put(parameteName,parameteValue);
            }
        }
        return queryMap;
    }

    /**
     * 将对像转换成Json字符串
     *
     * @param obj 需转换的对像
     * @return String
     */
    public static String objectToJson(Object obj) {
        String json = "{}";
        if (obj instanceof List) {
            json = "[]";
        }
        if(!isEmpty(obj)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                if (obj instanceof List){
                    List<Object> newlist = new ArrayList<>();
                    for(Object item: (List<?>)obj){
                        newlist.add(classToData(item));
                    }
                    json = mapper.writeValueAsString(newlist);
                }else if(obj instanceof Map) {
                    Map<Object,Object> newmap = new HashMap<>();
                    for(Object key: ((Map<?,?>) obj).keySet()){
                        newmap.put(key,classToData(((Map<?, ?>) obj).get(key)));
                    }
                    json = mapper.writeValueAsString(newmap);
                } else {
                    json = mapper.writeValueAsString(classToData(obj));
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return json;
    }

    public static Object classToData(Object obj){
        if (obj == null) {
            return null;
        }
        if(obj instanceof String || obj instanceof Integer || obj instanceof Double || obj instanceof Long || obj instanceof Boolean) {
            return obj;
        }else if(obj instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object item : (List<?>) obj) {
                result.add(classToData(item));
            }
            return result;
        }else if(obj instanceof Map) {
            HashMap<Object, Object> result = new HashMap<>();
            for (Object Key : ((HashMap<?, ?>) obj).keySet()) {
                result.put(Key, classToData(((HashMap<?, ?>) obj).get(Key)));
            }
            return result;
        }else if(obj instanceof Enum) {
            return obj.toString();
        } else {
            HashMap<String, Object> fieldmap = new HashMap<>();
            Field[] fields = obj.getClass().getFields();
            if(fields.length > 0) {
                for (Field item : fields) {
                    if (item.getModifiers() == Modifier.PUBLIC) {
                        String fieldname = item.getName();
                        if (!"isEditState".equals(fieldname) && !"flowvalue".equals(fieldname)) {
                            try {
                                Object value = item.get(obj);
                                if (value != null) {
                                    fieldmap.put(fieldname, classToData(value));
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        }
                    }
                }
            } else {
                fields = obj.getClass().getDeclaredFields();
                for (Field item : fields) {
                    String fieldname = item.getName();
                    if(Utils.isNotEmpty(ClassUtils.getterMethod(obj,fieldname))){
                        Object value = ClassUtils.exeGetMethod(obj,fieldname);
                        if(value != null){
                            fieldmap.put(fieldname, classToData(value));
                        }
                    }
                }
            }
            return fieldmap;
        }
    }

    public static Object classToDataDc(Object obj){
        if (obj == null) {
            return null;
        }
        if(obj instanceof String || obj instanceof Integer || obj instanceof Double || obj instanceof Long || obj instanceof Boolean) {
            return obj;
        }else if(obj instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object item : (List<?>) obj) {
                TabField tabField = (TabField)item;
                if ("author".equals(tabField.fieldname)||"updated".equals(tabField.fieldname)||"published".equals(tabField.fieldname)) {
                    continue;
                }
                result.add(classToData(item));
            }
            return result;
        }else if(obj instanceof Map){
            HashMap<Object, Object> result = new HashMap<>();
            for(Object key : ((HashMap<?,?>)obj).keySet()){
                Object classData = classToData(((HashMap<?,?>)obj).get(key));
                if ("fieldname".equals(key) && classData != null &&
                    ("author".equals(classData.toString()) ||
                     "updated".equals(classData.toString()) ||
                     "published".equals(classData.toString()))) {
                    continue;
                }
                result.put(key,classData);
            }
            return result;
        } else {
            HashMap<String, Object> fieldmap = new HashMap<>();
            Field[] fields = obj.getClass().getFields();
            for (Field item : fields) {
                String fieldname = item.getName();
                if ("author".equals(fieldname)||"updated".equals(fieldname)||"published".equals(fieldname)) {
                    continue;
                }
                if(!"isEditState".equals(fieldname)) {
                    try {
                        Object value = item.get(obj);
                        if (!Utils.isEmpty(value)) {
                            fieldmap.put(fieldname, classToData(value));
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
            return fieldmap;
        }
    }

    /**
     * 将Json字符串转换成Java数组或Map
     * @param json Json字符串
     * @return Object
     */
    public static Object jsonToObject(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = null;
        try {
            obj = mapper.readValue(json, Object.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return obj;
    }

    /**
     * 对像克隆
     */
    public static Object objectClone(Object source) {
        return jsonToObject(objectToJson(source));
    }

    /**
     * 将List转换成分“,”分隔的字符串
     * @param object List String[]
     * @return String
     */
    public static String objectToString(Object object) {
        return objectToString(object, ",");
    }

    public static String objectToString(Object object, String delimiter) {
        if (object instanceof String[]) {
            return String.join(delimiter, (String[]) object);
        } else if (object instanceof List) {
            return String.join(delimiter, (List<String>) object);
        } else if (object instanceof String) {
            return (String) object;
        }
        return "";
    }

    /**
     * 判断对像是否不为空
     * @param obj String List Map
     * @return boolean
     */
    public static boolean isNotEmpty(Object obj) {
         return !isEmpty(obj);
    }

    /**
     * 判断对像是否为空
     * @param obj String List Map
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return obj.toString().isEmpty();
        } else if (obj instanceof List) {
            return ((List<?>) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        } else return obj instanceof String[] && ((String[]) obj).length <= 0;
    }

    /**
     * 获取身份证中的生日期
     * @param idCard
     * @return
     */
    public static Date getBirthDay(String idCard) {
        idCard = StringUtils.trimToEmpty(idCard);
        if(StringUtils.isEmpty(idCard)){
            return null;
        }
        String tempStr = "";
        if(idCard.length() != 15 && idCard.length() != 18){
            throw  new RuntimeException("身份证号格式错误.");
        }
        if(idCard.length() == 15){
            tempStr = "19" + idCard.substring(6, 12);
        } else {
            tempStr = idCard.substring(6, 14);
        }
        tempStr = tempStr.substring(0, 4) + "-" + tempStr.substring(4, 6) + "-" + tempStr.substring(6);
        return DateTimeUtils.string2date(tempStr);
    }

    /**
     * 判断是否为 ajax请求
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

    /**
     * 驼峰转下划线
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String camelToUnderscore(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String[] arr = StringUtils.splitByCharacterTypeCamelCase(value);
        if (arr.length == 0) {
            return value;
        }
        StringBuilder result = new StringBuilder();
        IntStream.range(0, arr.length).forEach(i -> {
            if (i != arr.length - 1) {
                result.append(arr[i]).append('_');
            } else {
                result.append(arr[i]);
            }
        });
        return StringUtils.lowerCase(result.toString());
    }

    /**
     * 下划线转驼峰
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String underscoreToCamel(String value) {
        StringBuilder result = new StringBuilder();
        String[] arr = value.split("_");
        for (String s : arr) {
            result.append((String.valueOf(s.charAt(0))).toUpperCase()).append(s.substring(1));
        }
        return result.toString();
    }

    //去重方式添加List对像元素
    public static void removeDuplicate(List<String> source, Object target) {
        if(target instanceof String && isNotEmpty(target)){
            removeDuplicate(source,new ArrayList<>(Collections.singleton(target)));
        }else if(target instanceof List){
            removeDuplicate(source,(List<String>) target);
        }
    }

    public static void removeDuplicate(List<String> source,List<String> target) {
        if(target.isEmpty()) {
            return;
        }
        if(source.equals(target)) {
            return;
        }
        List<String> one = new ArrayList<>(source);
        one.retainAll(target);
        if(!one.isEmpty()) {
            source.removeAll(one);
        }
        source.addAll(target);
    }

    //将数值转换成中文
    public static String intZH(Object src) {
        final String[] num = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        if(src instanceof String){
            src = Integer.parseInt(src.toString(),10);
        }
        if(src instanceof Integer) {
            String dst = "";
            int count = 0;
            while ((int)src > 0) {
                dst = (num[(int)src % 10] + unit[count]) + dst;
                src = (int)src / 10;
                count++;
            }
            return dst.replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                    .replaceAll("零+亿", "亿").replace("亿万", "亿零")
                    .replaceAll("零+", "零").replaceAll("零$", "");
        }
        return "";
    }

    public static String replaceString(String src) {
        return src.replace("\\\\","\\\\\\\\").replace("\"","\\\\\"");
    }

    public static String delHTMLTag(String htmlStr){
        String regExScript="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regExStyle="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regExHtml="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern pScript=Pattern.compile(regExScript,Pattern.CASE_INSENSITIVE);
        Matcher mScript=pScript.matcher(htmlStr);
        htmlStr=mScript.replaceAll(""); //过滤

        Pattern pStyle=Pattern.compile(regExStyle,Pattern.CASE_INSENSITIVE);
        Matcher mStyle=pStyle.matcher(htmlStr);
        htmlStr=mStyle.replaceAll(""); //过滤style标签

        Pattern pHtml=Pattern.compile(regExHtml,Pattern.CASE_INSENSITIVE);
        Matcher mHtml=pHtml.matcher(htmlStr);
        htmlStr=mHtml.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    public static Map<String, Object> getListByMap(List<Object> source, String key){
        for(Object item: source){
            if(item instanceof Map && ((Map<?, ?>) item).containsKey(key)){
                return (Map<String, Object>) item;
            }
        }
        return new HashMap<>(1);
    }

    /**
     * 获取uitype的中文名称
     */
    public static String getUitypeName(Integer uitype) {
        switch (uitype) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 9:
            case 23: return "文本";
            case 6:
            case 7:
            case 8: return "日期";
            case 10:
            case 11:
            case 12:
            case 25:
            case 29: return "关联";
            case 5:
            case 13:
            case 14:
            case 15:
            case 16:
            case 19:
            case 20: return "字典";
            case 17: return "图片";
            case 18: return "附件";
            case 21: return "分隔线";
            case 24: return "编辑器";
            case 26: return "地区";
            case 27: return "货币";
            case 28: return "评分";
            case 30: return "HTML文本";
            case 31: return "HTML";
            default: return "";
        }
    }
    /**
     * 判断链接是否有效
     * 输入链接
     * 返回true或者false
     */
    public static boolean isUrlValid(String strLink) {
        URL url;
        try {
            url = new URL(strLink);
            HttpURLConnection connt = (HttpURLConnection)url.openConnection();
            connt.setRequestMethod("HEAD");
            String strMessage = connt.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            connt.disconnect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
