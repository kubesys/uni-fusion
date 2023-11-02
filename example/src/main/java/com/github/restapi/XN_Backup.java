package com.github.restapi;

import com.github.restapi.models.Backup;
import com.github.restapi.models.FetchMsgException;
import com.github.restapi.models.FetchResult;
import com.github.restapi.utils.ColorUtils;
import com.github.restapi.utils.CryptoUtils;
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
public class XN_Backup {
    public static Backup create() {
        Backup backup = new Backup();
        backup.setApplication(XN_Rest.getApplication());
        return backup;
    }

    public static List<Object> load(String tag) throws Exception {
        log.info(ColorUtils.blue() + "XN_Backup.load" +  ColorUtils.white() + "["+ColorUtils.yellow() + "{}" + ColorUtils.white() +"](tag:'{}')",XN_Rest.getApplication(),tag);
        try {
            String url = "/backup";
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
            FetchResult entry = Backup.mapToContents(accessToken,cipher);
            return entry.entery;
        } catch(Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }

    public static void delete(String id,String tag) throws Exception {
        try {
            String url =  "/backup";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (StringUtils.isNotEmpty(tag)) {
                headers.put("tag",tag);
            }
            url += "(id="+ id +")";
            url += "?xn_out=json";
            String accessToken = XN_Credential.get();
            Map cipher;
            try {
                cipher =  XN_Fetch.delete(url,accessToken,headers);
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher =  XN_Fetch.delete(url,accessToken,headers);
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
            if (cipher != null) {
                if (cipher.containsKey("cipher") && cipher.get("cipher").toString().compareTo("") != 0) {
                    String ciphertext = cipher.get("cipher").toString();
                    String publickey = "";
                    if (StringUtils.isNotEmpty(accessToken) && accessToken.compareTo("closed") != 0) {
                        publickey = XN_Credential.getPublicKey();
                    }
                    Map decryptbody =  CryptoUtils.restDecrypt(ciphertext, publickey);
                    if (decryptbody.containsKey("code") && decryptbody.get("code").toString().compareTo("0") == 0) {
                        Map body = (Map)decryptbody.get("body");
                        if (body.containsKey("code") && Integer.parseInt(body.get("code").toString()) == 0) {
                            return;
                        }
                    } else {
                        throw new Exception(decryptbody.get("message").toString());
                    }
                } else if (accessToken.compareTo("closed") == 0) {
                    if (cipher.containsKey("code") && Integer.parseInt(cipher.get("code").toString()) == 0) {
                        return;
                    }
                }
            }
            throw new Exception("delete failure");
        } catch (Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }
}
