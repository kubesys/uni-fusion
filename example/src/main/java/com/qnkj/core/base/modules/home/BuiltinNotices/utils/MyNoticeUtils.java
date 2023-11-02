 package com.qnkj.core.base.modules.home.BuiltinNotices.utils;

 import com.github.restapi.utils.Md5Util;
 import com.qnkj.common.configs.BaseSaasConfig;
 import com.qnkj.common.utils.SaaSUtils;
 import com.qnkj.core.base.modules.management.notices.entitys.NoticeLevel;
 import com.qnkj.core.base.modules.management.notices.entitys.NoticeType;
 import com.qnkj.core.base.modules.management.notices.services.INoticesService;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;

 import javax.annotation.PostConstruct;
 import java.util.UUID;


 @Slf4j
 @Component
 public class MyNoticeUtils {

     @Autowired
     public INoticesService noticesService;

     private static MyNoticeUtils mynoticeutils;

     @PostConstruct
     public void init() {
         mynoticeutils = this;
         mynoticeutils.noticesService = this.noticesService;
     }

     public static void supplierCreate(String supplierid, String title, String body) {
         supplierCreate(supplierid,title,body,NoticeLevel.info,"");
     }
     public static void supplierCreate(String supplierid, String title, String body,String md5) {
         supplierCreate(supplierid,title,body,NoticeLevel.info,md5);
     }

     public static void supplierCreate(String supplierid, String title, String body,NoticeLevel noticelevel) {
         supplierCreate(supplierid,title,body,noticelevel,"");
     }

     public static void supplierCreate(String supplierid, String title, String body,NoticeLevel noticelevel,String md5) {
         try {
             SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
             String publisher = saasUtils.getPlatformName() + "团队";
             if (md5.isEmpty()) {
                 md5 = Md5Util.get(UUID.randomUUID().toString());
             }
             mynoticeutils.noticesService.create(supplierid,"anonymous", publisher, NoticeType.supplier,noticelevel,title,body,md5);
         } catch(Exception e) {
             log.error("MyNoticeUtils.create: {} ", e.getMessage());
         }
     }

     public static void profileCreate(String profileid, String title, String body) {
         profileCreate(profileid,title,body,NoticeLevel.info,"");
     }
     public static void profileCreate(String profileid, String title, String body,String md5) {
         profileCreate(profileid,title,body,NoticeLevel.info,md5);
     }
     public static void profileCreate(String profileid, String title, String body,NoticeLevel noticelevel) {
         profileCreate(profileid,title,body,noticelevel,"");
     }
     public static void profileCreate(String profileid,String title,String body, NoticeLevel noticelevel,String md5) {
         try {
             SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
             String publisher = saasUtils.getPlatformName() + "团队";
             if (md5.isEmpty()) {
                 md5 = Md5Util.get(UUID.randomUUID().toString());
             }
             mynoticeutils.noticesService.create("",profileid,publisher,NoticeType.profile,noticelevel,title,body,md5);
         } catch(Exception e) {
             log.error("MyNoticeUtils.create: {} ", e.getMessage());
         }
     }

     public static void approvalCreate(String title, String body) {
         approvalCreate(title,body,NoticeLevel.info,"");
     }
     public static void approvalCreate(String title, String body,String md5) {
         approvalCreate(title,body,NoticeLevel.info,md5);
     }
     public static void approvalCreate(String title, String body,NoticeLevel noticelevel) {
         approvalCreate(title,body,noticelevel,"");
     }
     public static void approvalCreate(String title,String body, NoticeLevel noticelevel,String md5) {
         try {
             SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
             String publisher = saasUtils.getPlatformName() + "团队";
             if (md5.isEmpty()) {
                 md5 = Md5Util.get(UUID.randomUUID().toString());
             }
             mynoticeutils.noticesService.create("","",publisher,NoticeType.approval,noticelevel,title,body,md5);
         } catch(Exception e) {
             log.error("MyNoticeUtils.create: {} ", e.getMessage());
         }
     }
     public static void systemCreate(String title, String body) {
         systemCreate(title,body,NoticeLevel.info,"");
     }
     public static void systemCreate(String title, String body,String md5) {
         systemCreate(title,body,NoticeLevel.info,md5);
     }
     public static void systemCreate(String title, String body,NoticeLevel noticelevel) {
         systemCreate(title,body,noticelevel,"");
     }
     public static void systemCreate(String title,String body, NoticeLevel noticelevel,String md5) {
         try {
             SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
             String publisher = saasUtils.getPlatformName() + "团队";
             if (md5.isEmpty()) {
                 md5 = Md5Util.get(UUID.randomUUID().toString());
             }
             mynoticeutils.noticesService.create("","",publisher,NoticeType.system,noticelevel,title,body,md5);
         } catch(Exception e) {
             log.error("MyNoticeUtils.create: {} ", e.getMessage());
         }
     }
     public static Boolean exist(String md5) throws Exception {
         return mynoticeutils.noticesService.exist(md5);
     }
}
