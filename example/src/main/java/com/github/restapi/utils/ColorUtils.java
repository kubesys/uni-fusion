package com.github.restapi.utils;

public class ColorUtils {
    /**
     * 白色
     */
    public static String white() { return"\33[0m"; }

    /**
     * 红色
     */
    public static String red() { return "\33[1m\33[31m"; }

    /**
     * 绿色
     */
    public static String green() { return "\33[1m\33[32m"; }

    /**
     * 黄色
     */
    public static String yellow() { return "\33[1m\33[33m"; }

    /**
     * 蓝色
     */
    public static String blue() { return "\33[1m\33[34m"; }

    /**
     * 粉色
     */
    public static String pink() { return "\33[1m\33[35m"; }

    /**
     * 青色
     */
    public static String cyan() { return "\33[1m\33[36m"; }
}
