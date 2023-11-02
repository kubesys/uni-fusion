package com.qnkj.common.utils;

import com.qnkj.common.configs.BaseSaasConfig;

import java.util.*;

/**
 * create by 徐雁
 * create date 2021/3/19
 */

public class SaaSUtils {

    private String currentDomain = BaseSaasConfig.getDomain();
    private Properties props;

    public SaaSUtils() {
        props = SaaSInfo.getSettings(currentDomain);
    }
    public SaaSUtils(String domain) {
        currentDomain = domain;
        props = SaaSInfo.getSettings(currentDomain);
    }
    public static List<String> getDomains() {
        return SaaSInfo.getDomains();
    }

    public static void save() throws Exception {
        SaaSInfo.save();
    }

    public static Boolean isExist(String domain) {
        return SaaSInfo.domainExist(domain);
    }

    public static void Delete(String domain){
        SaaSInfo.delDomain(domain);
    }

    public Properties getSettings(){
        return props;
    }

    public String getApplication() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("platform_appid","");
    }
    public String getPlatformName() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("platform_name","");
    }
    public String getPlatformDesc() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("platform_desc","");
    }
    public String getCopyright() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("platform_copyright","");
    }
    public String getCompany() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("platform_company","");
    }
    public String getCompanyNickname() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("company_nickname","");
    }
    public String getICP() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("platform_icp","");
    }
    public String getPolice() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("platform_police","");
    }

    public Boolean isRegister() {
        if(props == null || props.isEmpty()) { return false; }
        return Boolean.parseBoolean(props.getProperty("platform_isregister","false"));
    }
    public Boolean isSingle() {
        if(props == null || props.isEmpty()) { return false; }
        return Boolean.parseBoolean(props.getProperty("platform_issingleon","false"));
    }
    public String getSMSAccessID() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("sms_accessid","");
    }
    public String getSMSSecret() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("sms_secret","");
    }
    public String getSMSSignName() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("sms_signname","");
    }
    public String getSMSTemplateCode() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("sms_templatecode","");
    }
    public String getSMSFlowTemplateCode() {
        if(props == null || props.isEmpty()) { return ""; }
        return props.getProperty("sms_flowtemplatecode","");
    }
    public List<Object> getPlatformAuthorizes() {
        List<Object> result = new ArrayList<>();
        if(props == null || props.isEmpty()) { return result; }
        String auth = props.getProperty("platform_authorizes_auth","");
        String authlabel = props.getProperty("platform_authorizes_label","");
        if(!auth.contains("adminassistant")){
            auth = "adminassistant,"+auth;
            authlabel = "系统管理员助理," + authlabel;
        }
        String[] auths = auth.split(",");
        String[] labels = authlabel.split(",");
        for(int i=0;i<auths.length;i++){
            Map<String, Object> info = new HashMap<>(1);
            info.put("authorize",auths[i]);
            info.put("authlabel",labels[i]);
            result.add(info);
        }
        return result;
    }
    public List<Object> getUserAuthorizes() {
        List<Object> result = new ArrayList<>();
        if(props == null || props.isEmpty()) { return result; }
        String auth = props.getProperty("user_authorizes_auth","");
        String authlabel = props.getProperty("user_authorizes_label","");
        if(!auth.contains("supplierassistant")){
            auth = "supplierassistant,"+auth;
            authlabel = "企业管理助理," + authlabel;
        }
        String[] auths = auth.split(",");
        String[] labels = authlabel.split(",");
        for(int i=0;i<auths.length;i++){
            Map<String, Object> info = new HashMap<>(1);
            info.put("authorize",auths[i]);
            info.put("authlabel",labels[i]);
            result.add(info);
        }
        return result;
    }

    public void setApplication(Object value) {
        SaaSInfo.setApplication(currentDomain,value);
    }
    public void setPlatformName(Object value){
        SaaSInfo.setPlatformName(currentDomain,value);
    }
    public void setPlatformDesc(Object value){
        SaaSInfo.setPlatformDesc(currentDomain,value);
    }
    public void setCopyright(Object value){
        SaaSInfo.setCopyright(currentDomain,value);
    }
    public void setCompany(Object value){
        SaaSInfo.setCompany(currentDomain,value);
    }
    public void setCompanyNickname(Object value){
        SaaSInfo.setCompanyNickname(currentDomain,value);
    }
    public void setICP(Object value){
        SaaSInfo.setICP(currentDomain,value);
    }
    public void setPolice(Object value){
        SaaSInfo.setPolice(currentDomain,value);
    }

    public void setRegister(Object value){
        SaaSInfo.setRegister(currentDomain,value);
    }
    public void setSingle(Object value){
        SaaSInfo.setSingle(currentDomain,value);
    }
    public void setSMSAccessID(Object value){
        SaaSInfo.setSMSAccessID(currentDomain,value);
    }
    public void setSMSSecret(Object value){
        SaaSInfo.setSMSSecret(currentDomain,value);
    }
    public void setSMSSignName(Object value){
        SaaSInfo.setSMSSignName(currentDomain,value);
    }
    public void setSMSTemplateCode(Object value){
        SaaSInfo.setSMSTemplateCode(currentDomain,value);
    }
    public void setSMSFlowTemplateCode(Object value) {
        SaaSInfo.setSMSFlowTemplateCode(currentDomain,value);
    }
    public void setPlatformAuthorizes(Object auth,Object label) {
        SaaSInfo.setPlatformAuthorizes(currentDomain,auth,label);
    }
    public void setUserAuthorizes(Object auth, Object label) {
        SaaSInfo.setUserAuthorizes(currentDomain,auth,label);
    }
}

