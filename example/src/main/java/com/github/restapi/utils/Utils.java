package com.github.restapi.utils;

import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author Oldhand
 **/

public class Utils {
    /**
     * 获取格林威治时间戳
     */
    public static long gettimeStamp() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
        long timeStamp = cal.getTimeInMillis() - 28800000;
        return timeStamp / 1000;

    }

    /**
     * 获取时间偏移量
     */
    public static int getzoneOffset() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.ZONE_OFFSET);
    }
    /**
     * 获取本地MAC地址
     * 按照"XX-XX-XX-XX-XX-XX"格式，获取本机MAC地址
     * @return
     * @throws Exception
     */
    public static String getMacAddress() throws Exception{
        Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
        while(ni.hasMoreElements()){
            NetworkInterface netI = ni.nextElement();
            byte[] bytes = netI.getHardwareAddress();
            if(netI != null && netI.isUp() && bytes != null && bytes.length == 6){
                StringBuilder sb = new StringBuilder();
                for(byte b:bytes){
                    //与11110000作按位与运算以便读取当前字节高4位
                    sb.append(Integer.toHexString((b&240)>>4));
                    //与00001111作按位与运算以便读取当前字节低4位
                    sb.append(Integer.toHexString(b&15));
                    sb.append("-");
                }
                sb.deleteCharAt(sb.length()-1);
                return sb.toString().toUpperCase();
            }
        }
        return "";
    }

}
