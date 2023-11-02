package com.github.restapi.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 3DES加密解密方式
 * @author Oldhand
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
@Slf4j
public class DesEdeUtil {

    private static final String KEY_ALGORITHM = "DESede";
    private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/NoPadding";

    /**
     * DESede 加密操作
     *
     * @param content
     *            待加密内容
     * @param key
     *            加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) {
        try {
            if (key.length() > 24) {
                key = key.substring(0,24);
            }
            byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
            SecretKey deskey = new SecretKeySpec(bytes, KEY_ALGORITHM);
            Cipher c1 = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] msgBytes = content.getBytes(StandardCharsets.UTF_8);
            int remainder = msgBytes.length % 8;
            if (0 != remainder) {
                int oldLength = msgBytes.length;
                msgBytes = Arrays.copyOf(msgBytes, msgBytes.length + 8 - remainder);
                Arrays.fill(msgBytes, oldLength, msgBytes.length, (byte) 0);
            }
            byte[] doFinal = c1.doFinal(msgBytes);
            String ciphertext = Base64.encodeBase64String(doFinal);
            ciphertext =  ciphertext.replace("\n","");
            ciphertext = ciphertext.replace("\r","");
            ciphertext = ciphertext.replace(" ","");
            return ciphertext;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * } } DESede 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content,String key) throws Exception {
        try {
            if (key.length() > 24) {
                key = key.substring(0,24);
            }
            byte[] bytes = key.getBytes( StandardCharsets.UTF_8);
            SecretKey deskey = new SecretKeySpec(bytes, KEY_ALGORITHM);
            Cipher instance = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            instance.init(Cipher.DECRYPT_MODE, deskey);
            byte[] encryptMsgBytes = Base64.decodeBase64(content);
            int remainder = encryptMsgBytes.length % 8;
            if (0 != remainder) {
                int oldLength = encryptMsgBytes.length;
                encryptMsgBytes = Arrays.copyOf(encryptMsgBytes, encryptMsgBytes.length + 8 - remainder);
                Arrays.fill(encryptMsgBytes, oldLength, encryptMsgBytes.length, (byte) 0);
            }
            byte[] doFinal = instance.doFinal(encryptMsgBytes);
            int zeroIndex = doFinal.length;
            for (int i = doFinal.length - 1; i > 0; i--) {
                if (doFinal[i] == (byte) 0) {
                    zeroIndex = i;
                } else {
                    break;
                }
            }
            doFinal = Arrays.copyOf(doFinal, zeroIndex);
            return new String(doFinal, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("decrypt Exception: "+ e.getMessage());
            log.error("decrypt content: "+content);
            log.error("decrypt key: "+key);
            throw e;
        }
    }
}
