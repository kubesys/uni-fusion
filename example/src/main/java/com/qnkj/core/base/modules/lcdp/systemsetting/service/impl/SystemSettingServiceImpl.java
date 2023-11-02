package com.qnkj.core.base.modules.lcdp.systemsetting.service.impl;

import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.lcdp.systemsetting.service.ISystemSettingService;
import com.qnkj.core.plugins.file.entity.UploadFile;
import com.qnkj.core.plugins.file.service.IUploadFileService;
import com.qnkj.core.webconfigs.configure.FreemarkerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * create by 徐雁
 * create date 2021/3/5
 */
@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl implements ISystemSettingService {
    private final IUploadFileService uploadFileService;

    @Override
    public Properties getSetting(String domain) {
        SaaSUtils saaSUtils = new SaaSUtils(domain);
        return saaSUtils.getSettings();
    }
    @Override
    public Object getPlatformAuthorizes(String domain) {
        SaaSUtils saaSUtils = new SaaSUtils(domain);
        return saaSUtils.getPlatformAuthorizes();
    }
    @Override
    public Object getUserAuthorizes(String domain) {
        SaaSUtils saaSUtils = new SaaSUtils(domain);
        return saaSUtils.getUserAuthorizes();
    }
    @Override
    public Object getTree() {
        Map<String,Object> noneInfo = new HashMap<>(1);
        noneInfo.put("pId", "");
        noneInfo.put("id", "saasmanager");
        noneInfo.put("title", "SaaS管理");
        noneInfo.put("name", "SaaS管理");
        List<Object> result = new ArrayList<>(Collections.singletonList(noneInfo));
        try{
            List<String> sections = SaaSUtils.getDomains();
            if(!sections.isEmpty()) {
                for (String section : sections) {
                    SaaSUtils saaSUtils = new SaaSUtils(section);
                    Map<String,Object> info = new HashMap<>(1);
                    info.put("pId", "saasmanager");
                    info.put("id", section);
                    info.put("name", section);
                    info.put("title", saaSUtils.getCompany());
                    result.add(info);
                }
            } else {
                SaaSUtils saaSUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                Map<String,Object> info = new HashMap<>(1);
                info.put("pId", "saasmanager");
                info.put("id", BaseSaasConfig.getDomain());
                info.put("name",  BaseSaasConfig.getDomain());
                info.put("title", saaSUtils.getCompany());
                result.add(info);
            }
        }catch (Exception ignored){}
        return result;
    }

    @Override
    public void save(Map<String, Object> httpRequest) throws Exception {
        if(Utils.isEmpty(httpRequest.get("domain"))){
            throw new Exception("获取域名失败");
        }
        String domain = httpRequest.get("domain").toString();
        SaaSUtils saasUtils = new SaaSUtils(domain);

        saasUtils.setApplication(httpRequest.getOrDefault("platform_appid", ""));
        saasUtils.setPlatformName(httpRequest.getOrDefault("platform_name", ""));
        saasUtils.setPlatformDesc(httpRequest.getOrDefault("platform_desc", ""));
        saasUtils.setCompanyNickname(httpRequest.getOrDefault("company_nickname", ""));
        saasUtils.setCompany(httpRequest.getOrDefault("platform_company", ""));
        saasUtils.setCopyright(httpRequest.getOrDefault("platform_copyright", ""));
        saasUtils.setICP(httpRequest.getOrDefault("platform_icp", ""));
        saasUtils.setPolice(httpRequest.getOrDefault("platform_police", ""));
        saasUtils.setRegister(httpRequest.getOrDefault("platform_isregister", "false"));
        saasUtils.setSingle(httpRequest.getOrDefault("platform_issingleon", "false"));;
        saasUtils.setSMSAccessID(httpRequest.getOrDefault("sms_accessid", ""));
        saasUtils.setSMSSecret(httpRequest.getOrDefault("sms_secret", ""));
        saasUtils.setSMSSignName(httpRequest.getOrDefault("sms_signname", ""));
        saasUtils.setSMSTemplateCode(httpRequest.getOrDefault("sms_templatecode", ""));
        saasUtils.setSMSFlowTemplateCode(httpRequest.getOrDefault("sms_flowtemplatecode", ""));
        saasUtils.setPlatformAuthorizes(httpRequest.getOrDefault("platform_authorizes_auth", ""),httpRequest.getOrDefault("platform_authorizes_label", ""));
        saasUtils.setUserAuthorizes(httpRequest.getOrDefault("user_authorizes_auth", ""),httpRequest.getOrDefault("user_authorizes_label", ""));

        SaaSUtils.save();
        if(domain.equals(BaseSaasConfig.getDomain())) {
            FreemarkerConfig.updateConfig();
        }
    }

    @Override
    public void delete(Map<String, Object> httpRequest) throws Exception {
        if(!Utils.isEmpty(httpRequest.get("domain"))){
            String domain = httpRequest.get("domain").toString();
            if(!SaaSUtils.isExist(domain)) {
                return;
            }
            SaaSUtils saasUtils = new SaaSUtils(domain);
            SaaSUtils.Delete(domain);
            if(domain.equals(BaseSaasConfig.getDomain())) {
                FreemarkerConfig.updateConfig();
            }
        }
    }

    protected void deleteOriginal(String original, String source){
        List<String> phototypes = Arrays.asList("png", "jpg", "jpeg", "gif", "ico");
        List<String> exclude = Arrays.asList("reg-bac","bac-index","index-logo","favicon","logo-left-tab","logo-left-tab-no-text");
        if(original != null && source != null && !original.isEmpty() && !original.equals(source) && !exclude.contains(original)){
            if (!ContextUtils.isJar()){
                String filepath = ContextUtils.getLocalFilepath(new String[]{"resources","static","images"},true);
                File pathfile = new File(filepath);
                if (pathfile.exists()) {
                    for(String ext : phototypes){
                        String tmp = filepath + File.separator + original + "." + ext;
                        File tmpfile = new File(tmp);
                        if(tmpfile.exists() && tmpfile.delete()){
                            try {
                                UploadFile uploadFile = uploadFileService.load(original);
                                if(uploadFile != null){
                                   /* if(FastDFSClient.delete(uploadFile.groupname,uploadFile.fileid) == 0){
                                        uploadFileService.delete(uploadFile.uniquekey);
                                    }*/
                                }
                            }catch (Exception ignored){}
                            break;
                        }
                    }
                }
                filepath = ContextUtils.getLocalFilepath(new String[]{"static","images"});
                pathfile = new File(filepath);
                if(pathfile.exists()){
                    for(String ext : phototypes){
                        String tmp = filepath + File.separator + original + "." + ext;
                        File tmpfile = new File(tmp);
                        if(tmpfile.exists() && tmpfile.delete()){
                            break;
                        }
                    }
                }
            }
        }
    }
}
