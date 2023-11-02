package com.qnkj.core.webconfigs.configure;


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
public class GlobalConfig {

  @Getter
  public static String version;

  @Getter
  private static String projectVersion;

  @Getter
  public static Boolean autoApprovalRegister;

  @Value("${build.version}")
  public void setBuildTime(String value) {
    version = value;
  }

  @Value("${build.project-version}")
  public void setProjectVersion(String value) {
    projectVersion = value;
  }

  @Value("${auto-approval-register}")
  public void setAutoApprovalRegister(Boolean value) {
    autoApprovalRegister = value;
  }



}
