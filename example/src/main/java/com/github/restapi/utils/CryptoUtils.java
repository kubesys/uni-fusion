package com.github.restapi.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author oldhand
 */
public class CryptoUtils {
    /** 非对称加密密钥算法*/
    private static final String RSA = "RSA";
    /**加密填充方式*/
    private static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
     /**默认公钥*/
    public static String PUBLIC_KEY = "";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return byte[] 加密数据
     */
    private static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    private static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPublic);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    /**
     *  公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 密钥
     * @return byte[]   解密数据
     */
    private static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        try {
            // 得到公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PublicKey keyPublic = kf.generatePublic(keySpec);
            // 数据解密
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keyPublic);

            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("No algorithm is available");
        } catch (InvalidKeySpecException e) {
            throw new Exception("Illegal public key");
        } catch (NullPointerException e) {
            throw new Exception("public key is empty");
        }
    }
    /**
     * 使用私钥进行解密
     */
    private static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        try {
            // 得到私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PrivateKey keyPrivate = kf.generatePrivate(keySpec);
            // 解密数据
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keyPrivate);
            int inputLen = encrypted.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encrypted, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encrypted, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("No algorithm is available");
        } catch (InvalidKeySpecException e) {
            throw new Exception("Illegal private key");
        } catch (NullPointerException e) {
            throw new Exception("public key is empty");
        }
    }
    /**
     * 数据接口 上传数据加密
     */
    public static Map<?,?> restEncrypt(String plaintext, String publickey) {
        try {
            Map<String, Object> result = new HashMap<>(1);
            result.put("code", "0");

            String pkey = PUBLIC_KEY;
            if (StringUtils.isNotEmpty(publickey)) {
                pkey = publickey;
            }
            String cipher = DesEdeUtil.encrypt(plaintext, Md5Util.get(pkey));
            result.put("cipher", cipher);
            return result;
        } catch (Exception e) {
            Map<String,String> result = new HashMap<>(1);
            result.put("domain", "RestEncrypt");
            result.put("code", "-1");
            result.put("message", e.toString());
            return result;
        }
    }
    /**
     * 数据接口 解密
     */
    public static Map<?,?> restDecrypt(String cipher, String publickey) {
        try {
            Map<String, Object> result = new HashMap<>(1);
            result.put("code", "0");
            String pkey = PUBLIC_KEY;
            if (StringUtils.isNotEmpty(publickey)) {
                pkey = publickey;
            }
            String plainText = DesEdeUtil.decrypt(cipher, Md5Util.get(pkey));
            JSONObject  myJson = JSONObject.parseObject(plainText);
            result.put("body", myJson);
            return result;
        } catch (Exception e) {
            Map<String,Object> result = new HashMap<>(1);
            result.put("domain", "RestDecrypt");
            result.put("code", "-1");
            result.put("message", e.toString());
            return result;
        }
    }

    /**
     * 数据接口 接口认证数据加密
     */
    public static String encrypt(String plaintext,String publickey) throws Exception {
        String pkey = PUBLIC_KEY;
        if (StringUtils.isNotEmpty(publickey)) {
            pkey = publickey;
        }
        pkey = pkey.replace("-----BEGIN PUBLIC KEY-----", "");
        pkey = pkey.replace("-----END PUBLIC KEY-----", "");
        pkey = pkey.replace("\n", "");
        pkey = pkey.replace("\r", "");
        byte[] publicKey = Base64.decodeBase64(pkey);
        byte[] plainbytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] cipherText = encryptByPublicKey(plainbytes,publicKey);
        String cipher = Base64.encodeBase64String(cipherText);
        cipher =  cipher.replace("\n","");
        cipher = cipher.replace("\r","");
        cipher = cipher.replace("\t","");
        cipher = cipher.replace(" ","");
        return cipher;

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
    public static String base64Decode(String ciphertext) throws Exception {
        byte[] encode = Base64.decodeBase64(ciphertext);
        return new String(encode);
    }
}
