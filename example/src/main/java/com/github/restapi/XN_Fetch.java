package com.github.restapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.restapi.models.FetchMsgException;
import com.github.restapi.utils.CryptoUtils;
import com.github.restapi.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @author oldhand
 */
@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
@Slf4j
public class XN_Fetch {
    /** 连接超时时间*/
    private static final Integer CONNECTTIMEOUT = 10000;
    /** 请求超时时间 */
    private static final Integer CONNECTIONREQUESTTIMEOUT = 30000;
    /** 响应超时时间 */
    private static final Integer SOCKETTIMEOUT= 60000;

    private static RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(CONNECTTIMEOUT)
                .setConnectionRequestTimeout(CONNECTIONREQUESTTIMEOUT)
                .setSocketTimeout(SOCKETTIMEOUT)
                .build();
    }

    public static Map<?,?> get(String url, String accessToken, Map<String, String> headers) throws Exception {
        return fetch("get",url,accessToken,headers,null);
    }
    public static Map<?,?> put(String url,String accessToken,Map<String, String> headers,Map<?,?> sendbody) throws Exception {
        return fetch("put",url,accessToken,headers,sendbody);
    }
    public static Map<?,?> post(String url,String accessToken,Map<String, String> headers,Map<?,?> sendbody) throws Exception {
        return  fetch("post",url,accessToken,headers,sendbody);
    }
    public static Map<?,?> delete(String url,String accessToken,Map<String, String> headers) throws Exception {
        return fetch("delete",url,accessToken,headers,null);
    }
    @SuppressWarnings("AlibabaMethodTooLong")
    private static Map<?,?> fetch(String method, String url, String accessToken, Map<String, String> headers, Map<?,?> sendbody) throws Exception {
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            CloseableHttpResponse response;
            CryptoUtils.PUBLIC_KEY = RestConfig.publicKey;
            String publickey = "";
            if (StringUtils.isNotEmpty(accessToken) && accessToken.compareTo("closed") != 0 ) {
                publickey = XN_Credential.getPublicKey();
            }
            Map<?,?> info = getRestToken("/xn/rest/1.0" + url, publickey);
            String token = info.get("token").toString();
            String timestamp = info.get("timestamp").toString();
            String fullurl = RestConfig.baseurl + "/base64/" + CryptoUtils.base64Encode("/xn/rest/1.0" + url);
            URI uri = URI.create(fullurl);
            headers.put("accesstoken", accessToken);
            headers.put("token", token);
            headers.put("timestamp", timestamp);
            if ("post".equalsIgnoreCase(method)){
                String encryptBody = encryptbody(accessToken,sendbody);
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setEntity(new StringEntity(encryptBody, StandardCharsets.UTF_8));
                httpPost.setConfig(requestConfig());
                for(String key : headers.keySet()){
                    httpPost.setHeader(key,headers.get(key));
                }
                httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
                response = httpclient.execute(httpPost);
            } else if ("put".equalsIgnoreCase(method)){
                String encryptBody = encryptbody(accessToken,sendbody);
                HttpPut httpput = new HttpPut(uri);
                httpput.setEntity(new StringEntity(encryptBody, StandardCharsets.UTF_8));
                httpput.setConfig(requestConfig());
                for(String key : headers.keySet()){
                    httpput.setHeader(key,headers.get(key));
                }
                httpput.addHeader("Content-Type", "application/json;charset=UTF-8");
                response = httpclient.execute(httpput);
            }else if ("delete".equalsIgnoreCase(method)){
                HttpDelete httpDelete = new HttpDelete(uri);
                httpDelete.setConfig(requestConfig());
                for(String key : headers.keySet()){
                    httpDelete.setHeader(key,headers.get(key));
                }
                response = httpclient.execute(httpDelete);
            }else {
                HttpGet httpGet = new HttpGet(uri);
                httpGet.setConfig(requestConfig());
                for(String key : headers.keySet()){
                    httpGet.setHeader(key,headers.get(key));
                }
                response = httpclient.execute(httpGet);
            }
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 解析响应数据
                String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject jsonbody = JSONObject.parseObject(responseContent);
                if (jsonbody.containsKey("code")) {
                    if ("0".equals(jsonbody.get("code").toString())) {
                        return jsonbody;
                    } else {
                        if (jsonbody.containsKey("error")) {
                            throw new FetchMsgException(1,jsonbody.get("error").toString());
                        } else {
                            throw new FetchMsgException(1,"Response format error");
                        }
                    }
                } else {
                    return jsonbody;
                }
            } else {
                throw new FetchMsgException(1,response.getStatusLine().getReasonPhrase());
            }
        } catch(FetchMsgException e) {
            throw e;
        } catch(Exception e) {
            log.error("XN_Fetch." + method + " error ：" + e);
            throw e;
        }
    }

    static String encryptbody(String accessToken,Map<?,?> sendbody) throws Exception {
        Map<?,?> encryptBody;
        String plaintext = JSON.toJSONString(sendbody);
        if ("closed".equals(accessToken)) {
            return plaintext;
        } else if (accessToken.isEmpty()) {
            encryptBody = CryptoUtils.restEncrypt(plaintext,"");
        } else {
            String publickey = XN_Credential.getPublicKey();
            encryptBody = CryptoUtils.restEncrypt(plaintext,publickey);
        }
        if (encryptBody.containsKey("code") && "0".equals(encryptBody.get("code").toString())) {
            return "{\"cipher\":\"" + encryptBody.get("cipher").toString() + "\"}";
        }
        throw new Exception("RestEncrypt failure");
    }

    /**
     * 获取数据接口token参数
     */
    private static Map<?,?> getRestToken(String url,String publickey) {
        Map<String,Object> result = new HashMap<>(1);
        try {
            result.put("code", "0");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            long timeStamp = cal.getTimeInMillis() - cal.get(java.util.Calendar.ZONE_OFFSET);
            String timestamp = String.valueOf(timeStamp / 1000);
            String token = Md5Util.get( url + timestamp);
            result.put("timestamp", timestamp);
            result.put("token", CryptoUtils.encrypt(token,publickey));
            return result;
        } catch (Exception e) {
            result.put("domain", "getRestToken");
            result.put("code", "-1");
            result.put("message", e.toString());
            return result;
        }
    }
    public static Map<?,?> cipherToMap(String accessToken, Map<?,?> cipher) throws Exception {
        if (cipher != null) {
            if (cipher.containsKey("cipher") && !cipher.get("cipher").toString().isEmpty()) {
                String ciphertext = cipher.get("cipher").toString();
                String publickey = "";
                if (StringUtils.isNotEmpty(accessToken) && accessToken.compareTo("closed") != 0) {
                    publickey = XN_Credential.getPublicKey();
                }
                Map<?,?> decryptbody =  CryptoUtils.restDecrypt(ciphertext, publickey);
                if (decryptbody.containsKey("code") && "0".equals(decryptbody.get("code").toString())) {
                    return (Map<?,?>) decryptbody.get("body");
                } else {
                    throw new Exception(decryptbody.get("message").toString());
                }
            } else if ("closed".equals(accessToken) || accessToken.isEmpty()) {
                return cipher;
            } else {
                if (cipher.containsKey("error") && StringUtils.isNotEmpty(cipher.get("error").toString())) {
                    throw new Exception(cipher.get("error").toString());
                }
            }
        }
        return cipher;
    }
}
