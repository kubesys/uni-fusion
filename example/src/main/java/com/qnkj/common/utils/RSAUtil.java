package com.qnkj.common.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RSA Util
 * DateTime:2019/4/9
 **/
@Slf4j
public class RSAUtil {
    private static final String RSA = "RSA";// 非对称加密密钥算法
    private static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private RSAUtil() {

    }

    //map对象中存放公私钥
    public static Map<String, String> initKey() throws Exception {
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
        keyPairGen.initialize(1024, new SecureRandom());
        //通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        byte[] privBytes = privateKey.getEncoded();

        PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privBytes);
        ASN1Encodable encodable = pkInfo.parsePrivateKey();
        ASN1Primitive primitive = encodable.toASN1Primitive();
        byte[] privateKeyPKCS1 = primitive.getEncoded();

        //公私钥对象存入map中
        Map<String, String> keyMap = new HashMap<>(2);
        keyMap.put("publickey", pkcs1ToPem(publicKey.getEncoded(), true));
        keyMap.put("privatekey", pkcs1ToPem(privateKeyPKCS1, false));
        return keyMap;
    }

    public static String pkcs1ToPem(byte[] pcks1KeyBytes, boolean isPublic) throws Exception {
        String type;
        if (isPublic) {
            type = "PUBLIC KEY";
        } else {
            type = "RSA PRIVATE KEY";
        }
        PemObject pemObject = new PemObject(type, pcks1KeyBytes);
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        return stringWriter.toString();
    }

    //从文件中读取公私钥
    public static String loadKey(String filepath) {
        // 从 classpath:resources/ 中加载资源
        try {
            ClassPathResource resource = new ClassPathResource(filepath);
            String content = resource.getReader(Charset.defaultCharset()).lines().collect(Collectors.joining("\n"));
            return content.trim();
        } catch (Exception e) {
            log.error("<loadKey> Error: {}",e.getMessage());
        }
        return "";
    }


    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return byte[] 加密数据
     */
    private static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(privateKey));
        RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey keyPrivate = keyFactory.generatePrivate(rsaPrivKeySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
        return encryptBody(cipher,data);
//        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            int inputLen = data.length;
//            int offSet = 0;
//            byte[] cache;
//            int i = 0;
//            // 对数据分段加密
//            while (inputLen - offSet > 0) {
//                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
//                } else {
//                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
//                }
//                out.write(cache, 0, cache.length);
//                i++;
//                offSet = i * MAX_ENCRYPT_BLOCK;
//            }
//            return out.toByteArray();
//        }
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段加密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(data, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * MAX_ENCRYPT_BLOCK;
//        }
//        byte[] encryptedData = out.toByteArray();
//        out.close();
//        return encryptedData;
    }

    private static byte[] encryptBody(Cipher cipher, byte[] data) throws Exception {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int inputLen = data.length;
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
            return out.toByteArray();
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPublic);
        return encryptBody(cipher,data);
//        int inputLen = data.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段加密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(data, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * MAX_ENCRYPT_BLOCK;
//        }
//        byte[] encryptedData = out.toByteArray();
//        out.close();
//        return encryptedData;
    }

    /**
     * 公钥解密
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
            RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(privateKey));
            RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey keyPrivate = keyFactory.generatePrivate(rsaPrivKeySpec);
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
            throw new Exception("private key is empty");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 数据接口 上传数据加密
     */
    public static String encrypt(String plaintext, String publickey) {
        try {
            String pkey = publickey;
            pkey = pkey.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("\n", "")
                    .replace("\r", "");
            byte[] publicKey = Base64Util.decode(pkey);// 先用base64解密
            byte[] data = plaintext.getBytes();
            byte[] cipherText = encryptByPublicKey(data, publicKey);
            return Base64Util.encode(cipherText);// base64加密
        } catch (Exception e) {
            e.printStackTrace();
            log.error("<encrypt> Error: {}",e.getMessage());
            return plaintext;
        }
    }

    /**
     * 数据接口 解密
     */
    public static String decrypt(String cipher, String privateKey) {
        try {
            byte[] cipherdata = Base64Util.decode(cipher);// 先用base64解密
            String pkey = privateKey;
            pkey = pkey.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replace("\n", "")
                    .replace("\r", "");
            byte[] decodeKey = Base64Util.decode(pkey);// 先用base64解密
            byte[] plaintext = decryptByPrivateKey(cipherdata, decodeKey);
            return new String(plaintext);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("<decrypt> Error: {}",e.getMessage());
            return cipher;
        }
    }
}
