package com.qnkj.common.utils;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MathUtils {
    /**
     * 两数相乘，返回两位小数点的Double
     * @return
     */
    public static Double multiply(Double p1, Double p2) {
        return BigDecimal.valueOf(p1).multiply(BigDecimal.valueOf(p2)).setScale(2, RoundingMode.HALF_UP).doubleValue();
//        return new BigDecimal(p1).multiply(new BigDecimal(p2)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    public static Double multiply(Double p1, Integer p2) {
        return BigDecimal.valueOf(p1).multiply(BigDecimal.valueOf(p2)).setScale(2, RoundingMode.HALF_UP).doubleValue();
//        return new BigDecimal(p1).multiply(new BigDecimal(p2)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    /**
     * 两数相除，返回两位小数点的Double
     */
    public static Double divide(Double p1, Double p2) {
        return BigDecimal.valueOf(p1).divide(BigDecimal.valueOf(p2),2,RoundingMode.HALF_UP).doubleValue();
//        return new BigDecimal(p1).divide(new BigDecimal(p2),2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static Double divide(Double p1, Integer p2) {
        return BigDecimal.valueOf(p1).divide(BigDecimal.valueOf(p2),1,RoundingMode.HALF_UP).doubleValue();
//        return new BigDecimal(p1).divide(new BigDecimal(p2),2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static Double divide(Integer p1, Integer p2) {
        return BigDecimal.valueOf(p1).divide(BigDecimal.valueOf(p2),2,RoundingMode.HALF_UP).doubleValue();
//        return new BigDecimal(p1).divide(new BigDecimal(p2),2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double getDoubleByScale(double v,int scale) {
        return BigDecimal.valueOf(v).setScale(scale,RoundingMode.HALF_UP).doubleValue();
//        return new BigDecimal(v).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String toString(double v) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(v);
    }

    public static String roundByScale(double v) { return roundByScale(v,2); }
    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(double v, int scale) {
        if(scale <= 0){
            return new DecimalFormat("0").format(v);
        }
        StringBuilder formatStr = new StringBuilder("0.");
        for(int i=0;i<scale;i++){
            formatStr.append("0");
        }
        return new DecimalFormat(formatStr.toString()).format(v);
    }

    public static Integer toInteger(String value) {
        if (Utils.isEmpty(value)) {
            return 0;
        }
        if (NumberUtil.isInteger(value)) {
            return Integer.parseInt(value);
        }
        if (NumberUtil.isDouble(value)) {
            return Double.valueOf(value).intValue();
        }
        return 0;
    }
}
