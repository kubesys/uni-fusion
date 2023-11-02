package com.github.restapi;

import com.github.restapi.models.FetchMsgException;
import com.github.restapi.models.FetchResult;
import com.github.restapi.models.Profile;
import com.github.restapi.utils.Base64Util;
import com.github.restapi.utils.ColorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 **/

@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
@Slf4j
public class XN_Profile {
    public static Profile create(String password) { return new Profile(password); }
    public static Profile create(String email, String password){
        return new Profile(email,password);
    }

    public static Profile load(String profileid, String tag) throws Exception {
        return load(profileid,"id",tag);
    }

    public static Profile load(String profileid, String key, String tag) throws Exception {
        log.info(ColorUtils.blue() + "XN_Profile.load" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](id:'" + profileid + "',key:'" + key + "',tag:'" + tag + "')");
        try {
            if (profileid.isEmpty()) {
                throw new Exception("XN_Profile.load profileid cannot be empty");
            }
            String url = "/profile";
            url += "(" + key + "='" + profileid + "')";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (StringUtils.isNotEmpty(tag)) {
                headers.put("tag",tag);
            }
            url += "?xn_out=json";
            String accessToken = XN_Credential.get();
            Map cipher;
            try {
                cipher = XN_Fetch.get(url,accessToken,headers);
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher = XN_Fetch.get(url,accessToken,headers);
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            } catch (Exception e) {
                throw e;
            }
            FetchResult entry = Profile.mapToProfiles(accessToken,cipher);
            if (!entry.entery.isEmpty()) {
                return (Profile)entry.entery.get(0);
            }
            throw new Exception("no Profile");
        } catch(Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }
    public static List loadMany(List<String> ids, String tag) throws Exception {
        log.info(ColorUtils.blue() + "XN_Profile.loadMany" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "]ids:" + ids.toString() + "',tag:'" + tag + "')");
        try {
            if (ids.isEmpty()) {
                throw new Exception("XN_Profile.loadMany ids cannot be empty");
            }
            String url = "/profile";
            url += "(id in ['"+ StringUtils.join(ids,"','") +"'])";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (StringUtils.isNotEmpty(tag)) {
                headers.put("tag",tag);
            }
            url += "?xn_out=json";
            String accessToken = XN_Credential.get();
            Map cipher;
            try {
                cipher = XN_Fetch.get(url,accessToken,headers);
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher = XN_Fetch.get(url,accessToken,headers);
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            } catch (Exception e) {
                throw e;
            }
            FetchResult entry = Profile.mapToProfiles(accessToken,cipher);
            return entry.entery;

        } catch(Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }
    public static boolean sign(String profileid, String password) throws Exception {
        log.info(ColorUtils.blue() + "XN_Profile.sign" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](id:'" + profileid + "',password:'" + password + "')");
        try {
            if (profileid.isEmpty()) {
                throw new Exception("XN_Profile.sign profileid cannot be empty");
            }
            String url = "/profile";
            url += "(id=" + profileid + "')/signin";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            headers.put("Authorization","Basic " + Base64Util.base64Encode(profileid + ":" + password));
            url += "?xn_out=json";
            String accessToken = XN_Credential.get();
            Map cipher;
            try {
                cipher = XN_Fetch.post(url,accessToken,headers,new HashMap<>(1));
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher = XN_Fetch.post(url,accessToken,headers,new HashMap<>(1));
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            } catch (Exception e) {
                throw e;
            }
            Map result = XN_Fetch.cipherToMap(accessToken,cipher);
            String code = result.get("code").toString();
            String message = result.get("ok").toString();
            if (code.compareTo("0") == 0 && message.compareTo("successed") == 0) {
                return true;
            }
            return false;
        } catch(Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }
}
