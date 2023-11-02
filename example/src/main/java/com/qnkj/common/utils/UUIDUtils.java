package com.qnkj.common.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * uuid工具类
 * @author oldhand
 */
public class UUIDUtils {

    private UUIDUtils() {}

    public static String getPrimaryKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * java生成随机数字和字母组合
     *
     * @param length 生成随机数长度
     */
    public static String getCharAndNum(int length) {
        StringBuilder val = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = new SecureRandom().nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = new SecureRandom().nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (choice + new SecureRandom().nextInt(26)));
            } else {
                // 数字
                val.append(new SecureRandom().nextInt(10));
            }
        }
        return val.toString().toLowerCase();
    }
}
