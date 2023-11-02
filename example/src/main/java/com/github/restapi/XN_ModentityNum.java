package com.github.restapi;

import com.github.restapi.models.FetchMsgException;
import com.github.restapi.utils.CryptoUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 **/


@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
public class XN_ModentityNum {

    public static String get(String module) throws Exception{
        String result = "";
        if(module == null || module.isEmpty()) {
            return result;
        }
        try{
            String url = "/modentitynum(module='"+module+"')?xn_out=json";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
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
            return  getModentityNumFromCipher(accessToken,cipher);

        }catch (Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }

    private static String getModentityNumFromCipher(String accessToken, Map cipher) throws Exception {
        int size = 0;
        List<Object> contents = new ArrayList();
        try {
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
                        if (body.containsKey("modentitynum") ) {
                            return body.get("modentitynum").toString();
                        } else if (body.containsKey("error") && body.get("error").toString().compareTo("") != 0) {
                            throw new Exception(body.get("error").toString());
                        }
                    } else {
                        throw new Exception(decryptbody.get("message").toString());
                    }
                } else if (accessToken.compareTo("closed") == 0 || accessToken.isEmpty()) {
                    if (cipher.containsKey("modentitynum") ) {
                        return cipher.get("modentitynum").toString();
                    }
                } else {
                    if (cipher.containsKey("error") && cipher.get("error").toString().compareTo("") != 0) {
                        throw new Exception(cipher.get("error").toString());
                    }
                }
            }
        }catch(Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
        throw new Exception("unknown error");
    }

}
