package com.qnkj.common.utils;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 验证工具
 * @author oldhand
 * @date 2019-12-16
*/
public class ValidationUtil {

    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");


    private static final List<String> JAVA_KEYWORDS = Arrays.asList(
            "abstract",
            "continue",
            "for",
            "new",
            "switch",
            "assert",
            "default",
            "goto",
            "package",
            "synchronized",
            "boolean",
            "do",
            "if",
            "private",
            "this",
            "break",
            "double",
            "implements",
            "protected",
            "throw",
            "byte",
            "else",
            "import",
            "public",
            "throws",
            "case",
            "enum",
            "instanceof",
            "return",
            "transient",
            "catch",
            "extends",
            "int",
            "short",
            "try",
            "char",
            "final",
            "interface",
            "static",
            "void",
            "class",
            "finally",
            "long",
            "volatile",
            "const",
            "float",
            "native",
            "super",
            "while",
            "strictfp",
            "null",
            "true",
            "false",
            "TRUE",
            "FALSE",
            "String",
            "Integer",
            "Double",
            "Boolean",
            "List",
            "Map",
            "Arrays",
            "Exception");

    private static final List<String> SQL_KEYWORDS = Arrays.asList(
            "select",
            "from",
            "where",
            "group",
            "by",
            "having",
            "order",
            "limit",
            "count",
            "summax",
            "min",
            "avg",
            "distinct",
            "left",
            "join",
            "inner",
            "primary",
            "key",
            "foreign",
            "not",
            "null",
            "int",
            "char",
            "varchar",
            "smallint",
            "numeric",
            "real",
            "double",
            "precision",
            "float",
            "create",
            "insert",
            "delete",
            "update",
            "drop",
            "right");

    /**
     * 正则校验
     *
     * @param regex 正则表达式字符串
     * @param value 要匹配的字符串
     * @return 正则校验结果
     */
    public static boolean match(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private static final Pattern IS_ZH_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    /**
     * 验证模块名，菜单名，字段名是否符合要求
     * @param str String
     * @return true
     * @throws Exception 错误消息
     */
    public static boolean check(String str) throws Exception {
        if (str.isEmpty()) {
            throw new Exception("不能为空");
        }
        if (str.length() < 2) {
            throw new Exception("长度不能小于2位");
        }
        if (str.length() > 20) {
            throw new Exception("长度不能大于20位");
        }
        if (str.contains("\"")) {
            throw new Exception("不能含有双引号");
        }
        if (str.contains("'")) {
            throw new Exception("不能含有单引号");
        }
        if (IS_ZH_PATTERN.matcher(str).find()) {
            throw new Exception("不能含有中文");
        }
        if (!str.matches("^[0-9a-zA-z].*")) {
            throw new Exception("必须以字母开头");
        }
        if (!str.matches("^[a-zA-Z_0-9]+$")) {
            throw new Exception("存在非法字符");
        }
        if (JAVA_KEYWORDS.contains(str)) {
            throw new Exception("不能使用Java关键字");
        }
        for(String item : SQL_KEYWORDS){
            if (item.toLowerCase().compareTo(str.toLowerCase()) == 0) {
                throw new Exception("不能使用Sql关键字");
            }
        }
        return true;
    }

    /**
     * 验证密码是否符合要求
     * @param password String
     * @return true
     * @throws Exception 错误消息
     */
    public static boolean checkPassword(String password) throws Exception {
        if (password.isEmpty()) {
            throw new Exception("密码不能为空");
        }
        if (password.length() < 6) {
            throw new Exception("密码长度不能小于6位");
        }
        if (password.length() > 20) {
            throw new Exception("密码长度不能大于20位");
        }
        if (password.contains("\"")) {
            throw new Exception("密码不能含有双引号");
        }
        if (password.contains("'")) {
            throw new Exception("密码不能含有单引号");
        }
        if (IS_ZH_PATTERN.matcher(password).find()) {
            throw new Exception("密码不能含有中文");
        }
        if (!password.matches("^(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^[^\\s\\u4e00-\\u9fa5]{6,20}$")) {
            throw new Exception("密码必须同时包含数字与字母!");
        }
        return true;
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String string) {
        if (string == null){
            return false;
        }
        if (string.isEmpty()){
            return false;
        }
        String regEx1 = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)\\.[a-zA-Z]+\\s*$";
//        String regEx1 = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return string.matches(regEx1);
    }
    /**
     * 验证日期是否符合格式并正确
     */
    private static final Pattern IS_DATE_PATTERN = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))$");
    public static boolean isDate(String date) {
        Matcher mat = IS_DATE_PATTERN.matcher(date);
        return mat.matches();
    }
    /**
     * 验证日期是否符合格式 yyyy-MM-dd HH:mm:ss
     */
    private static final Pattern IS_DATETIME_PATTERN = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))\\s[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$");
    public static boolean isDateTime(String date) {
        if (date == null){
            return false;
        }
        if (date.isEmpty()){
            return false;
        }
        Matcher mat = IS_DATETIME_PATTERN.matcher(date);
        return mat.matches();
    }
    /**
     * 判断是不是纯数字
     */
    private static final Pattern IS_NUMERIC_PATTERN = Pattern.compile("[0-9]+");
    public static boolean isNumeric(String str) {
        Matcher isNum = IS_NUMERIC_PATTERN.matcher(str);
        return isNum.matches();
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isMobile(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 判断是否包含中文
     *
     * @param value 内容
     * @return 结果
     */
    public static boolean containChinese(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        Matcher matcher = CHINESE_PATTERN.matcher(value);
        return matcher.find();
    }

}
