package com.qnkj.common.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64
 * Author: oldhand
 * DateTime:2019/4/9
 **/
public class Base64Util {

    private Base64Util() {}

    /**
     * Decoding to binary
     * @param base64 base64
     * @return byte
     */
    public static byte[] decode(String base64) {
        return Base64.decodeBase64(base64);
    }

    /**
     * Binary encoding as a string
     * @param bytes byte
     * @return String
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * base64Encode
     */
    public static String base64Encode(String plaintext) {
        byte[] plainbytes = plaintext.getBytes();
        String ciphertext = Base64.encodeBase64String(plainbytes);
        ciphertext =  ciphertext.replace("\n","");
        ciphertext = ciphertext.replace("\r","");
        ciphertext = ciphertext.replace("\t","");
        ciphertext = ciphertext.replace(" ","");
        return ciphertext;
    }
    /**
     * base64Decode
     */
    public static String base64Decode(String ciphertext) {
        byte[] encode = Base64.decodeBase64(ciphertext);
        return new String(encode);
    }
}
