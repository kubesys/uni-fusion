package com.qnkj.common.utils;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.github.restapi.XN_Content;
import com.github.restapi.models.Content;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SmsUtils {

    @Getter
    public static List<String> ipWhiteLists = new ArrayList<>();
    @Value("${sms.ip_white_lists}")
    public void setIpWhiteLists(String value) {
        ipWhiteLists = Arrays.asList(value.split(","));
    }


    public static Boolean isSmsConfig() {
        SaaSUtils saas = new SaaSUtils();
        String accessKeyId = saas.getSMSAccessID();
        String accessKeySecret = saas.getSMSSecret();
        String signname = saas.getSMSSignName();
        String templatecode = saas.getSMSTemplateCode();
        if (Utils.isEmpty(accessKeyId)) {
            return false;
        }
        if (Utils.isEmpty(accessKeySecret)) {
            return false;
        }
        if (Utils.isEmpty(signname)) {
            return false;
        }
        if (Utils.isEmpty(templatecode)) {
            return false;
        }
        return true;
    }


    public static void sendSms(String mobile,String verifyCode) throws Exception {
        sendSmsHasRegionCode("+86",mobile,verifyCode);
    }

    public static void sendMessage(String mobile,String templatecode,String templateparam) throws Exception {
        sendMessage("+86",mobile,templatecode,templateparam);
    }

    public static void sendMessage(String regioncode,String mobile,String templatecode,String templateparam) throws Exception {
        try {
            if (!isAllowSend()){ return;}
            try {
                SendSmsResponse response = AliyunSms.send(mobile,regioncode,templatecode,templateparam);
                if (response.getCode().compareTo("OK") == 0) {
                    log.info("向手机{}发送短信成功！模板ID：{}，模板参数：{}", mobile,templateparam);
                    try {
                        Content newcontent = XN_Content.create("smslog", "","", 7);
                        newcontent.add("deleted", "0");
                        newcontent.add("mobile",  mobile);
                        newcontent.add("regioncode", regioncode);
                        newcontent.add("templatecode", templatecode);
                        newcontent.add("templateparam", templateparam);
                        SaaSUtils saas = new SaaSUtils();
                        String accessKeyId = saas.getSMSAccessID();
                        newcontent.add("accesskey_id", accessKeyId);
                        newcontent.add("biz_id", response.getBizId());
                        newcontent.add("request_id", response.getRequestId());
                        newcontent.save("smslog");
                    } catch (Exception ignored) {}
                } else {
                    throw new Exception(response.getMessage());
                }
            } catch (Exception e) {
                log.error("SmsUtils.sendSmsHasRegionCode: {}",e.getMessage());
                throw e;
            }
        } catch(Exception e) {
            throw e;
        }
    }
    public static void sendSmsHasRegionCode(String regioncode,String mobile,String verifycode) throws Exception {
        if (!isAllowSend()){ return;}
        try {
            SendSmsResponse response = AliyunSms.send(mobile,regioncode,verifycode);
            if (response.getCode().compareTo("OK") == 0) {
                log.info("向手机{}发送短信验证码成功!验证码：{}", mobile,verifycode);
                try {
                    Content newcontent = XN_Content.create("smslog", "","", 7);
                    newcontent.add("deleted", "0");
                    newcontent.add("mobile",  mobile);
                    newcontent.add("regioncode", regioncode);
                    newcontent.add("verifycode", verifycode);
                    SaaSUtils saas = new SaaSUtils();
                    String accessKeyId = saas.getSMSAccessID();
                    newcontent.add("accesskey_id", accessKeyId);
                    newcontent.add("biz_id", response.getBizId());
                    newcontent.add("request_id", response.getRequestId());
                    newcontent.save("smslog");
                } catch (Exception ignored) {}
            } else {
                throw new Exception(response.getMessage());
            }
        } catch (Exception e) {
            log.error("SmsUtils.sendSmsHasRegionCode: {}",e.getMessage());
            throw e;
        }
    }

    public static Boolean isAllowSend() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress(); //返回IP地址
            log.info("ipWhiteLists ip: {} ", ipWhiteLists);
            log.info("localhost ip: {} ", ip);
            if (ipWhiteLists.contains(ip)) { return true;}
        } catch (Exception e) {
            log.error("SmsUtils.isAllowSend: {}",e.getMessage());
        }
        return false;
    }
}
