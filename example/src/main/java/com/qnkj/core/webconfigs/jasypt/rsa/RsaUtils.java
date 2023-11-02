package com.qnkj.core.webconfigs.jasypt.rsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 工具
 *
 * <p>用openssl生成512位RSA：
 *
 * <p>生成私钥： openssl genrsa -out key.pem 512
 *
 * <p>从私钥中导出公钥： openssl rsa -in key.pem -pubout -out public-key.pem
 *
 * <p>公钥加密： openssl rsautl -encrypt -in xx.file -inkey public-key.pem -pubin -out xx.en
 *
 * <p>私钥解密： openssl rsautl -decrypt -in xx.en -inkey key.pem -out xx.de
 *
 * <p>pkcs8编码（Java）： openssl pkcs8 -topk8 -inform PEM -in key.pem -outform PEM -out private-key.pem
 * -nocrypt
 *
 * <p>最后将公私玥放在/resources/rsa/：private-key.pem public-key.pem
 *
 * @author Oldhand
 * @date 2019/12/12
 */
@Slf4j
@Component
public class RsaUtils {
  private static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
  /**
   * RSA最大加密明文大小
   */
  private static final int MAX_ENCRYPT_BLOCK = 117;
  /**
   * RSA最大解密密文大小
   */
  private static final int MAX_DECRYPT_BLOCK = 128;

  @Resource private RsaConfigurationProperties rsaProperties;
  private static final String ALGORITHM = "RSA";
  private PrivateKey privateKey;
  private PublicKey publicKey;

  public RsaUtils() {
    if (this.rsaProperties == null) {
      this.rsaProperties = new RsaConfigurationProperties();
    }
  }

  /**
   * 生成密钥对
   *
   * @param keyLength 密钥长度(最少512位)
   * @return 密钥对 公钥 keyPair.getPublic() 私钥 keyPair.getPrivate()
   * @throws Exception e
   */
  public KeyPair genKeyPair(final int keyLength) throws Exception {
    final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
    keyPairGenerator.initialize(keyLength);
    return keyPairGenerator.generateKeyPair();
  }

  /**
   * 公钥加密
   *
   * @param content 待加密数据
   * @param publicKey 公钥
   * @return 加密内容
   * @throws Exception e
   */
  public byte[] encrypt(final byte[] content, final PublicKey publicKey) throws Exception {
    // 数据加密
    Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    int inputLen = content.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段加密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
        cache = cipher.doFinal(content, offSet, MAX_ENCRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(content, offSet, inputLen - offSet);
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
   * 公钥加密
   *
   * @param content 待加密数据
   * @return 加密内容
   * @throws Exception e
   */
  public byte[] encrypt(final byte[] content) throws Exception {
    return this.encrypt(content, this.publicKey != null ? this.publicKey : this.loadPublicKey());
  }

  /**
   * 私钥解密
   *
   * @param encrypted 加密数据
   * @param privateKey 私钥
   * @return 解密内容
   * @throws Exception e
   */
  public byte[] decrypt(final byte[] encrypted, final PrivateKey privateKey) throws Exception {
    // 解密数据
    Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
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
  }

  /**
   * 私钥解密
   *
   * @param content 加密数据
   * @return 解密内容
   * @throws Exception e
   */
  public byte[] decrypt(final byte[] content) throws Exception {
    return this.decrypt(content, this.privateKey != null ? this.privateKey : this.loadPrivateKey());
  }

  /**
   * 加载pem格式的公钥
   *
   * @param decoded 二进制公钥
   * @return 公钥
   */
  public PublicKey loadPublicKey(final byte[] decoded) {
    try {
      final X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
      final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      this.publicKey = keyFactory.generatePublic(spec);
      return this.publicKey;
    } catch (final Exception e) {
      log.error("==> RSA Utils Exception: {}", e.getMessage());
      return null;
    }
  }

  /**
   * 加载配置文件中设置的公钥
   *
   * @return 公钥
   */
  public PublicKey loadPublicKey() {
    try {
      byte[] decoded;
      if (this.rsaProperties.isUseFile()) {
        decoded =
            this.replaceAndBase64Decode(
                this.rsaProperties.getPublicKeyPath(),
                this.rsaProperties.getPublicKeyHead(),
                this.rsaProperties.getPublicKeyTail());
      } else {
        decoded = Base64.decodeBase64(this.rsaProperties.getPublicKey());
      }
      return this.loadPublicKey(decoded);
    } catch (final Exception e) {
      log.error("==> RSA Utils Exception: {}", e.getMessage());
      return null;
    }
  }

  /**
   * 加载pem格式PKCS8编码的私钥
   *
   * @param decoded 二进制私钥
   * @return 私钥
   */
  public PrivateKey loadPrivateKey(final byte[] decoded) {
    try {
      final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
      final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      this.privateKey = keyFactory.generatePrivate(spec);
      return this.privateKey;
    } catch (final Exception e) {
      log.error("==> RSA Utils Exception: {}", e.getMessage());
      return null;
    }
  }

  /**
   * 加载配置文件中设置的私钥
   *
   * @return 私钥
   */
  public PrivateKey loadPrivateKey() {
    try {
      byte[] decoded;
      if (this.rsaProperties.isUseFile()) {
        decoded =
            this.replaceAndBase64Decode(
                this.rsaProperties.getPrivateKeyPath(),
                this.rsaProperties.getPrivateKeyHead(),
                this.rsaProperties.getPrivateKeyTail());
      } else {
        decoded = Base64.decodeBase64(this.rsaProperties.getPrivateKey());
      }
      return this.loadPrivateKey(decoded);
    } catch (final Exception e) {
      log.error("==> RSA Utils Exception: {}", e.getMessage());
      return null;
    }
  }

  /**
   * 加载文件后替换头和尾并解密
   *
   * @return 文件字节
   */
  private byte[] replaceAndBase64Decode(
      final String filePath, final String headReplace, final String tailReplace) throws Exception {
    // 从 classpath:resources/ 中加载资源
    final ClassPathResource resource = new ClassPathResource(filePath);
    if (!resource.exists()) {
      throw new Exception("公私钥文件找不到");
    }
    final byte[] keyBytes = new byte[(int) resource.getFile().length()];
    try(FileInputStream in = new FileInputStream(resource.getFile())) {
      if(in.read(keyBytes) <= 0){
        throw new Exception("公私钥文件读取失败");
      }
    }

    final String keyPem =
        new String(keyBytes).replace(headReplace, "").trim().replace(tailReplace, "").trim();

    return Base64.decodeBase64(keyPem);
  }
}
