package io.github.kubesys.backend.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author bingshuai@nj.iscas.ac.cn
 * @since 11.01
 */

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
