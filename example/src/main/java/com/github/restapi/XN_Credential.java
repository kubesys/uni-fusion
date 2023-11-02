package com.github.restapi;

import com.github.restapi.models.Content;
import com.github.restapi.models.Credential;
import com.github.restapi.models.FetchResult;
import com.github.restapi.utils.Md5Util;
import com.github.restapi.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author oldhand
 */
@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
@Slf4j
public class XN_Credential {
    static Map<String,Credential> lists = new HashMap<>();
    public static String getPublicKey() throws Exception {
        String application = XN_Rest.getApplication();
        if (application != null && !application.isEmpty() && lists.containsKey(application)) {
            Credential credential = lists.get(application);
            if (credential != null && credential.localtime != 0 && !credential.accessToken.isEmpty()) {
                return credential.publicKey;
            }
            throw new Exception("no Credential");
        }
        throw new Exception("no application");
    }
    public static String flush() throws Exception {
        String application = XN_Rest.getApplication();
        if (application != null && !application.isEmpty()) {
            lists.remove(application);
        }
        return get();
    }
    public static void clear() {
        String application = XN_Rest.getApplication();
        if (application != null && !application.isEmpty()) {
            lists.remove(application);
        }
    }
    public static String get() throws Exception {
        try {
            String application = XN_Rest.getApplication();
            if (application != null && !application.isEmpty() && lists.containsKey(application)) {
                Credential credential = lists.get(application);
                if (credential != null) {
                    if (!credential.accessToken.isEmpty() && credential.localtime != 0) {
                        long timestamp = Utils.gettimeStamp() / 1000;
                        if (timestamp - credential.localtime < 3000) {
                            return credential.accessToken;
                        }
                    } else if ("closed".equals(credential.accessToken)) {
                        return "closed";
                    }
                }
            }
            Map<?,?> credential = getRestCredential(RestConfig.appid,RestConfig.secret);
            String appid = credential.get("appid").toString();
            String secret = credential.get("secret").toString();
            String uniqueid = credential.get("uniqueid").toString();
            String timestamp = credential.get("timestamp").toString();
            String url = "/credential";
            url += "(appid="+ appid + "&secret=" + secret + "&uniqueid=" + uniqueid + "&timestamp=" + timestamp + ")";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            url += "?xn_out=json";
            Map<?,?> cipher =  XN_Fetch.get(url,"",headers);
            FetchResult fetchresult = Content.mapToContents("",cipher,0);

            if (!fetchresult.entery.isEmpty()) {
                Content credentialInfo = (Content)fetchresult.entery.get(0);
                String crypto = credentialInfo.my.get("crypto").toString();
                long localtime = Utils.gettimeStamp() / 1000;
                if ("1".equals(crypto)) {
                    String accessToken = credentialInfo.id;
                    String publicKey = credentialInfo.my.get("publickey").toString();
                    long timeStamp = Long.parseLong(credentialInfo.my.get("timestamp").toString());
                    Credential tmpCredential = new Credential(accessToken,publicKey,localtime,timeStamp);
                    lists.put(application,tmpCredential);
                    log.info("application: {},access_token: {}",application,accessToken);
                    return accessToken;
                } else {
                    Credential tmpCredential = new Credential("closed","",localtime,0);
                    lists.put(application,tmpCredential);
                    return "closed";
                }
            } else {
                throw new Exception("no credential Content");
            }
        } catch (Exception e){
            log.error("XN_Credential Exception: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 获取数据接口票据参数
     */
    public static Map<?,?> getRestCredential(String appid, String localsecret) {
        try {
            Map<String,Object> result = new HashMap<>(1);
            result.put("code", "0");
            String uniqueid = Md5Util.get(Utils.getMacAddress());
            java.util.Calendar cal = java.util.Calendar.getInstance() ;
            cal.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
            long timeStamp = cal.getTimeInMillis() - 28800000;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
            Date date = new Date(timeStamp);
            String timestamp = String.valueOf(timeStamp / 1000);
            String secret = appid + uniqueid + localsecret + timestamp;

            result.put("uniqueid", uniqueid);
            result.put("appid", appid);
            result.put("secret", Md5Util.get(secret));
            result.put("timestamp", timestamp);
            result.put("datetime", simpleDateFormat.format(date));
            return result;
        } catch (Exception e) {
            Map<String,Object> result = new HashMap<>(1);
            result.put("domain", "getRestCredential");
            result.put("code", "-1");
            result.put("message", e.toString());
            return result;
        }
    }
}
