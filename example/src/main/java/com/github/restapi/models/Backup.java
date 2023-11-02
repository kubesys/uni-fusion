package com.github.restapi.models;

import com.github.restapi.XN_Credential;
import com.github.restapi.XN_Fetch;
import com.github.restapi.XN_Rest;
import com.github.restapi.utils.CryptoUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * create by 徐雁
 * @author clubs
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
public class Backup implements Serializable {
    protected String id = "";
    protected String path = "";
    protected String style = "";
    protected String application = "";
    protected String published = "";

    public void setPublished(String published) {
        this.published = published;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getPublished() {
        return published;
    }

    public String getStyle() {
        return style;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Backup() {
        this.id = "";
        this.application = XN_Rest.getApplication();
        this.published = "";
        this.style = "";
        this.path = "";
    }

    public Backup save(String tag) throws Exception {
        Map<String,String> headers = new HashMap<>(1);
        headers.put("domain",XN_Rest.getApplication());
        if (StringUtils.isNotEmpty(tag)) {
            headers.put("tag",tag);
        }
        String url = "/backup?xn_out=json";
        String accessToken = XN_Credential.get();
        Map<String, Object> sendbody = new HashMap<>(1);
        sendbody.put("size",1);
        sendbody.put("entry", Collections.singletonList(this.toJson()));
        Map<?,?> cipher;
        try {
            if (this.id.isEmpty()) {
                cipher =  XN_Fetch.post(url,accessToken,headers,sendbody);
            } else {
                cipher =  XN_Fetch.put(url,accessToken,headers,sendbody);
            }
        } catch (FetchMsgException fme) {
            if (Boolean.TRUE.equals(fme.isNeedFlush(accessToken))) {
                try {
                    accessToken = XN_Credential.flush();
                    if (this.id.isEmpty()) {
                        cipher =  XN_Fetch.post(url,accessToken,headers,sendbody);
                    } else {
                        cipher =  XN_Fetch.put(url,accessToken,headers,sendbody);
                    }
                } catch (FetchMsgException fe) {
                    throw new WebException(fe.errormsg);
                }
            } else {
                throw new WebException(fme.errormsg);
            }
        }
        FetchResult entrys = mapToContents(accessToken,cipher);
        if (!entrys.entery.isEmpty()) {
            Backup content = (Backup) entrys.entery.get(0);
            clone(content);
            return content;
        }
        throw new WebException("no Backup");
    }

    public Map<String, Object> toJson(){
        Map<String, Object> json = new HashMap<>(1);
        json.put("xn_application",application);
        json.put("published",published);
        json.put("path",path);
        return json;
    }

    public Backup(Map<String,Object> json) {
        for(Map.Entry<String,Object> entry: json.entrySet()){
            if(entry.getValue() != null){
                switch (entry.getKey()) {
                    case "id":
                    case "xn_id":
                        this.id = entry.getValue().toString();
                        break;
                    case "application":
                    case "xn_application":
                        this.application = entry.getValue().toString();
                        break;
                    case "published":
                        this.published = entry.getValue().toString();
                        break;
                    case "path":
                        this.path = entry.getValue().toString();
                        break;
                    case "style":
                        this.style = entry.getValue().toString();
                        break;
                    default: break;
                }
            }
        }
    }

    public static FetchResult mapToContents(String accessToken, Map<?,?> cipher) throws Exception {
        int size = 0;
        List<Object> contents = new ArrayList<>(1);
        if (cipher != null) {
            if (cipher.containsKey("cipher") && !cipher.get("cipher").toString().isEmpty()) {
                String ciphertext = cipher.get("cipher").toString();
                String publickey = "";
                if (StringUtils.isNotEmpty(accessToken) && accessToken.compareTo("closed") != 0) {
                    publickey = XN_Credential.getPublicKey();
                }
                Map<?,?> decryptbody =  CryptoUtils.restDecrypt(ciphertext, publickey);
                if (decryptbody.containsKey("code") && "0".equals(decryptbody.get("code").toString())) {
                    Map<?,?> body = (Map<?,?>)decryptbody.get("body");
                    if (body.containsKey("size") && Integer.parseInt(body.get("size").toString()) > 0) {
                        size = Integer.parseInt(body.get("size").toString());
                        List<Object> entrys = (List<Object>)body.get("entry");
                        entrys.forEach((Object value) -> contents.add(new Backup((Map<String,Object>)value)));
                    } else if (body.containsKey("error") && body.get("error").toString().isEmpty()) {
                        throw new WebException(body.get("error").toString());
                    }
                } else {
                    throw new WebException(decryptbody.get("message").toString());
                }
            } else if ("closed".equals(accessToken) || accessToken.isEmpty()) {
                if (cipher.containsKey("size") && Integer.parseInt(cipher.get("size").toString()) > 0) {
                    size = Integer.parseInt(cipher.get("size").toString());
                    List<Object> entrys = (List<Object>)cipher.get("entry");
                    entrys.forEach((Object value) -> contents.add(new Backup((Map<String,Object>)value)));
                }
            } else {
                if (cipher.containsKey("error") && StringUtils.isNotEmpty(cipher.get("error").toString())) {
                    throw new WebException(cipher.get("error").toString());
                }
            }
        }
        return new FetchResult(size,contents);
    }

    public void clone(Backup content) {
        this.setId(content.getId());
        this.setApplication(content.getApplication());
        this.setPublished(content.getPublished());
        this.setStyle(content.getStyle());
        this.setPath(content.getPath());
    }

}
