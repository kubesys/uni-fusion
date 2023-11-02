package com.qnkj.core.plugins.feign.authorization;

import com.qnkj.common.utils.MD5Util;
import com.qnkj.common.utils.RSAUtil;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.plugins.feign.FeignApi;

import java.util.regex.Pattern;

public class FeignUtils {

    public static String getSignature(String url,Long timestamp) throws Exception {
        String plainText = MD5Util.get(url + String.valueOf(timestamp));
        if ("/auth/credential".equals(url)) {
            return RSAUtil.encrypt(plainText,FeignConfig.publicKey);
        } else {
            String publicKey = FeignApi.getPublicKey();
            if (Utils.isNotEmpty(publicKey)) {
                return RSAUtil.encrypt(plainText, publicKey);
            }
        }
        return "";
    }
    public static boolean isBase64(Object content){
        if(!(content instanceof String) || content.toString().length() == 0 || content.toString().length()%4!=0){
            return false;
        }
        String pattern = "^[a-zA-Z0-9/+]*={0,2}$";
        return Pattern.matches(pattern, content.toString());
    }
}
