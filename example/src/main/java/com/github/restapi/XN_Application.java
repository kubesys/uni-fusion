package com.github.restapi;

import com.github.restapi.models.Application;
import com.github.restapi.models.FetchMsgException;
import com.github.restapi.models.FetchResult;
import com.github.restapi.utils.ColorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author oldhand
 */
@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
@Slf4j
public class XN_Application {

    public static Application create(String name,String description){
        return new Application(name, description);
    }
    public static Application load(String application,String tag) throws Exception {
        log.info(ColorUtils.blue() + "XN_Application.load" +  ColorUtils.white() + "["+ColorUtils.yellow() + "{}" + ColorUtils.white() +"](id='{}',tag:'{}')",XN_Rest.getApplication(),application,tag);
        try {
            if (application.isEmpty()) {
                throw new Exception("XN_Application.load id cannot be empty");
            }
            String url = "/application";
            url += "(domain='" + application + "')";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (StringUtils.isNotEmpty(tag)) {
                headers.put("tag",tag);
            }
            url += "?xn_out=json";
            String accessToken = XN_Credential.get();
            Map<?,?> cipher;
            try {
                cipher = XN_Fetch.get(url,accessToken,headers);
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher = XN_Fetch.get(url,accessToken,headers);
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            }
            FetchResult entry = Application.mapToApplications(accessToken,cipher);
            if (!entry.entery.isEmpty()) {
                return (Application)entry.entery.get(0);
            }
            throw new Exception("no Application");
        } catch(Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }

}
