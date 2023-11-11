package io.github.kubesys.backend.utils;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static String get(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        if (md5code.length() != 32) {
            String str = String.format("%"+(32 - md5code.length())+"d", 0).replace(" ", "0");
            md5code = str + md5code;
        }
        return md5code;
    }
}
