package com.qnkj.core.utils.nacos;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.http.client.config.RequestConfig.custom;


@Slf4j
@Component
public class HttpProvider {
    private static String nacosServiceAddr;
    private static String nacosUserName;
    private static String nacosPassWord;
    private static String nacosServiceGroup;

    @Value("${spring.cloud.nacos.discovery.server-addr:localhost:8848}")
    private void setNacosServiceAddr(String value) {
        nacosServiceAddr = value;
    }

    @Value("${spring.cloud.nacos.discovery.username:nacos")
    private void setNaocsUserName(String value) {
        nacosUserName = value;
    }

    @Value("${spring.cloud.nacos.discovery.password:nacos}")
    private void setNacosPassWord(String value) {
        nacosPassWord = value;
    }

    @Value("${spring.cloud.nacos.discovery.group:LCDP}")
    private void setNacosServiceGroup(String value) {
        nacosServiceGroup = value;
    }

    public enum Method {
        /**
         * 分别是http的 get put delete post 方法
         */
        GET,
        PUT,
        DELETE,
        POST
    }

    private String authStr = "";

    public HttpProvider() { }
    private static HttpProvider instance = null;
    public static HttpProvider getInstance() {
        if (instance == null) {
            instance = new HttpProvider();
            try {
                instance.login(nacosUserName, nacosPassWord);
            } catch (Exception e) {
                log.error("nacos login failed: {}", e.getMessage());
            }
        }
        return instance;
    }

    private static final String LOGIN_FAILED = "unknown user!";

    private static final String FORBIDDEN = "\"status\":403,\"error\":\"Forbidden\"";

    public static String getNacosServiceAddr() {
        return nacosServiceAddr;
    }
    public static String getNacosServiceGroup() {
        return nacosServiceGroup;
    }

    /**
     * http的登录auth
     */
    private void login(String username, String password) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        String tokenStr = nacosRequest(Method.POST, "/auth/login", params);

        // 失败会返回：unknown user!,成功会是一个json串
        if (tokenStr == null) {
            log.error("login failed due to network error");
            return;
        }
        if (tokenStr.contains(LOGIN_FAILED)) {
            log.error("Login failed, please check your username and password");
            return;
        }
        try {
            JsonObject root = new JsonParser().parse(tokenStr).getAsJsonObject();
            authStr = root.getAsJsonPrimitive("accessToken").getAsString();
        } catch (Exception e) {
            throw new Exception("Failed to parse login result, please check nacos-server version.");
        }
    }

    private String getNacosUrl() {
        return "http://" + nacosServiceAddr + "/nacos";
    }
    /**
     * 发送请求
     *
     * @param method
     * @param path
     * @param parameterMap
     * @return
     */
    public String nacosRequest(Method method, String path, Map<String, Object> parameterMap) throws Exception {
        // 拼接完整url
        String url = getNacosUrl() + "/v1" + path;
        List<NameValuePair> params = new ArrayList<>();
        if (parameterMap != null) {
            parameterMap.entrySet().forEach(e -> {
                if (e.getValue() != null) {
                    params.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())));
                }
            });
        }
        // 根据请求类型不同，构造请求，拼接参数，发送请求
        try {
            HttpRequestBase req = generateRequest(method, url, params);
            String ret = sendRequest(req);
            if (ret != null && ret.contains(FORBIDDEN)) {
                throw new Exception("403 Forbidden! Please check your permission.");
            }
            return ret;
        } catch (Exception e) {
            throw e;
        }
    }
    private HttpRequestBase generateRequest(Method method, String url, List<NameValuePair> params) throws Exception {
        if (Method.GET.equals(method)) {
            params.add(new BasicNameValuePair("accessToken", authStr));
            return new HttpGet(new URIBuilder(url).setParameters(params).build());
        } else if (Method.PUT.equals(method)) {
            HttpPut httpPut = new HttpPut(url + "?accessToken=" + authStr);
            httpPut.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            return httpPut;
        } else if (Method.POST.equals(method)) {
            HttpPost httpPost = new HttpPost(url + "?accessToken=" + authStr);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            return httpPost;
        } else if (Method.DELETE.equals(method)) {
            HttpDelete httpDelete = new HttpDelete(url + "?accessToken=" + authStr);
            httpDelete.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            return httpDelete;
        } else {
            throw new Exception("illegal method name");
        }
    }
    /**
     * 默认的超时设置
     */
    private static final RequestConfig TIMEOUT = custom().setSocketTimeout(1000).setConnectionRequestTimeout(1000).setConnectTimeout(1000).build();

    private String sendRequest(HttpRequestBase request) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        request.setConfig(TIMEOUT);
        CloseableHttpResponse resp = client.execute(request);
        try {
            return EntityUtils.toString(resp.getEntity(), "UTF-8");
        } finally {
            resp.close();
        }
    }
}
