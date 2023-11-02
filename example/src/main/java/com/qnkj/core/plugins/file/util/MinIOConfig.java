package com.qnkj.core.plugins.file.util;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 配置
 *
 * @author Oldhand
 * @date 2019/12/12
 */
@Component
public class MinIOConfig {
  @Getter
  public static String url;

  @Getter
  public static String bucket;

  @Getter
  public static String accessKey;

  @Getter
  public static String secretKey;

  @Value("${spring.minio.url}")
  public void setUrl(String value) {
    url = value;
  }

  @Value("${spring.minio.bucket}")
  public void setBucket(String value) {
    bucket = value;
  }

  @Value("${spring.minio.access-key}")
  public void setAccessKey(String value) {
    accessKey = value;
  }

  @Value("${spring.minio.secret-key}")
  public void setSecretKey(String value) {
    secretKey = value;
  }

}
