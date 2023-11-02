package com.qnkj.common.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class SaaSInfo {
    private static SaaSInfo saaSInfo;
    private IniReaderUtils iniReader;
    private String currentDomain;

    @PostConstruct
    public void init() {
        saaSInfo = this;
        currentDomain = null;
        try {
            if(iniReader == null) {
                iniReader = new IniReaderUtils("platform_setting.conf");
            }
        }catch (Exception e){
            iniReader = null;
        }
    }

    public static void save() throws Exception {
        if(saaSInfo.iniReader != null){
            saaSInfo.iniReader.save();
        } else {
            throw new Exception("保存数据失败");
        }
    }

    public static List<String> getDomains() {
        if(saaSInfo.iniReader != null){
            return saaSInfo.iniReader.getSections();
        } else {
            return new ArrayList<>();
        }
    }

    public static Boolean domainExist(String domain) {
        if(saaSInfo.iniReader == null) {
            return false;
        }
        return saaSInfo.iniReader.isExist(domain);
    }

    public static Properties getSettings(String domain) {
        if(saaSInfo.iniReader != null){
            return saaSInfo.iniReader.getSection(domain);
        } else {
            return new Properties();
        }
    }

    public static void delDomain(String domain) {
        saaSInfo.iniReader.delete(domain);
        try {
            saaSInfo.iniReader.save();
        }catch (Exception ignored) {}
        if(domain.equals(saaSInfo.currentDomain)) {
            saaSInfo.currentDomain = null;
        }
    }

    public static void setApplication(String domain, Object value) {
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_appid",value.toString());
        }
    }
    public static void setPlatformName(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_name",value.toString());
        }
    }
    public static void setPlatformDesc(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_desc",value.toString());
        }
    }
    public static void setCopyright(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_copyright",value.toString());
        }
    }
    public static void setCompany(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_company",value.toString());
        }
    }
    public static void setCompanyNickname(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"company_nickname",value.toString());
        }
    }
    public static void setICP(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_icp",value.toString());
        }
    }
    public static void setPolice(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_police",value.toString());
        }
    }

    public static void setRegister(String domain, Object value){
        if(value == null){
            value = false;
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_isregister",value.toString());
        }
    }
    public static void setSingle(String domain, Object value){
        if(value == null){
            value = false;
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"platform_issingleon",value.toString());
        }
    }
    public static void setSMSAccessID(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"sms_accessid",value.toString());
        }
    }
    public static void setSMSSecret(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"sms_secret",value.toString());
        }
    }
    public static void setSMSSignName(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"sms_signname",value.toString());
        }
    }
    public static void setSMSTemplateCode(String domain, Object value){
        if(value == null){
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            saaSInfo.iniReader.setValue(domain,"sms_templatecode",value.toString());
        }
    }
    public static void setSMSFlowTemplateCode(String domain, Object value) {
        if(value == null) {
            value = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()) {
            saaSInfo.iniReader.setValue(domain,"sms_flowtemplatecode",value.toString());
        }
    }
    public static void setPlatformAuthorizes(String domain, Object auth,Object label){
        if(auth == null){
            auth = "";
        }
        if(label == null){
            label = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            List<String> authStr = new ArrayList<>();
            if(auth instanceof List){
                for(Object item: (List)auth){
                    authStr.add(item.toString().trim());
                }
            } else {
                authStr.add(auth.toString().trim());
            }
            List<String> labelStr = new ArrayList<>();
            if(label instanceof List){
                for(Object item: (List)label){
                    labelStr.add(item.toString().trim());
                }
            } else {
                labelStr.add(label.toString().trim());
            }

            saaSInfo.iniReader.setValue(domain,"platform_authorizes_auth",String.join(",",authStr));
            saaSInfo.iniReader.setValue(domain,"platform_authorizes_label",String.join(",",labelStr));
        }
    }
    public static void setUserAuthorizes(String domain, Object auth,Object label) {
        if(auth == null){
            auth = "";
        }
        if(label == null){
            label = "";
        }
        if(saaSInfo.iniReader != null && domain != null && !domain.isEmpty()){
            List<String> authStr = new ArrayList<>();
            if(auth instanceof List){
                for(Object item: (List)auth){
                    authStr.add(item.toString().trim());
                }
            } else {
                authStr.add(auth.toString().trim());
            }
            List<String> labelStr = new ArrayList<>();
            if(label instanceof List){
                for(Object item: (List)label){
                    labelStr.add(item.toString().trim());
                }
            } else {
                labelStr.add(label.toString().trim());
            }
            saaSInfo.iniReader.setValue(domain,"user_authorizes_auth",String.join(",",authStr));
            saaSInfo.iniReader.setValue(domain,"user_authorizes_label",String.join(",",labelStr));
        }
    }
}
