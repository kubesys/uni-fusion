package com.github.restapi.models;

import com.github.restapi.XN_Credential;
import com.github.restapi.XN_Fetch;
import com.github.restapi.XN_Rest;
import com.github.restapi.utils.CryptoUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @author oldhand
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
public class Application implements Serializable {
    private static final long serialVersionUID = -4352868070794165001L;
    protected String id = "";
    protected String application = "";
    protected String title = "";
    protected String author = "";
    protected String published = "";
    protected String updated = "";
    protected String active = "";
    protected String trialtime = "";


    public String getId() {
        return id;
    }

    public String getApplication() {
        return application;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }

    public String getUpdated() {
        return updated;
    }

    public String getActive() {
        return active;
    }

    public String getTrialtime() {
        return trialtime;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public void setTrialtime(String trialtime) {
        this.trialtime = trialtime;
    }


    public Application(String name,String description) {
        this.application = name;
        this.id = "";
        this.title = description;
    }

    public Application(Map<String,Object> json) {
        for(Map.Entry<String,Object> entry: json.entrySet()){
            if(entry.getValue() != null) {
                switch (entry.getKey()) {
                    case "id":
                    case "xn_id":
                        this.id = entry.getValue().toString();
                        break;
                    case "application":
                        this.application = entry.getValue().toString();
                        break;
                    case "title":
                        this.title = entry.getValue().toString();
                        break;
                    case "published":
                        this.published = entry.getValue().toString();
                        break;
                    case "updated":
                        this.updated = entry.getValue().toString();
                        break;
                    case "author":
                        this.author = entry.getValue().toString();
                        break;
                    case "active":
                        this.active = entry.getValue().toString();
                        break;
                    case "trialtime":
                        this.trialtime = entry.getValue().toString();
                        break;
                    default:
                        break;
                }
            }
        }
        if(this.application == null || this.application.isEmpty()){
            this.application = XN_Rest.getApplication();
        }
    }
    Map<String, Object> toJson(){
        Map<String, Object> json = new HashMap<>(1);
        json.put("id", id);
        json.put("xn_id", id);
        json.put("xn_application", application);
        json.put("title", title);
        json.put("author", author);
        json.put("published", published);
        json.put("updated", updated);
        json.put("active", active);
        json.put("trialtime", trialtime);
        return json;
    }

    public void clone(Application application) {
        this.id = application.id;
        this.title = application.title;
        this.application = application.application;
        this.published = application.published;
        this.updated = application.updated;
        this.author = application.author;
        this.active = application.active;
        this.trialtime = application.trialtime;
    }
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"" +
                ",\"xn_id\":\"" + id + "\"" +
                ",\"xn_application\":\"" + application + "\"" +
                ",\"title\":\"" + title + "\"" +
                ",\"author\":\"" + author + "\"" +
                ",\"published\":\"" + published + "\"" +
                ",\"updated\":\"" + updated + "\"" +
                ",\"active\":\"" + active + "\"" +
                ",\"trialtime\":\"" + trialtime + "\"" +
                '}';
    }

    public static FetchResult mapToApplications(String accessToken, Map<?,?> cipher) throws Exception {
        int size = 0;
        List<Object> applications = new ArrayList<>(1);
        if (cipher != null) {
            if (cipher.containsKey("cipher") && !cipher.get("cipher").equals("")) {
                String ciphertext = cipher.get("cipher").toString();
                String publickey = "";
                if (StringUtils.isNotEmpty(accessToken) && !"closed".equals(accessToken)) {
                    publickey = XN_Credential.getPublicKey();
                }
                Map<?,?> decryptbody =  CryptoUtils.restDecrypt(ciphertext, publickey);
                if (decryptbody.containsKey("code") && "0".equals(decryptbody.get("code"))) {
                    Map<?,?> body = (Map<?,?>)decryptbody.get("body");
                    if (body.containsKey("size") && Integer.parseInt(body.get("size").toString()) > 0) {
                        size = Integer.parseInt(body.get("size").toString());
                        List<Object> entrys = (List<Object>)(body.get("entry"));
                        entrys.forEach((Object value) -> applications.add(new Application((Map<String,Object>)value)));
                    }
                } else {
                    throw new WebException(decryptbody.toString());
                }
            } else if ("closed".equals(accessToken) || accessToken.isEmpty()) {
                if (cipher.containsKey("size") && Integer.parseInt(cipher.get("size").toString()) > 0) {
                    size = Integer.parseInt(cipher.get("size").toString());
                    List<Object> entrys = (List<Object>)(cipher.get("entry"));
                    entrys.forEach((Object value) -> applications.add(new Application((Map<String,Object>)value)));
                }
            } else {
                if (cipher.containsKey("error") && !cipher.get("error").toString().isEmpty()) {
                    throw new WebException(cipher.get("error").toString());
                }
            }
        }
        return new FetchResult(size,applications);
    }

    public Application save(String tag) throws Exception {
        String url = "/application";
        Map<String, String> headers = new HashMap<>(1);
        headers.put("domain", XN_Rest.getApplication());
        if (StringUtils.isNotEmpty(tag)) {
            headers.put("tag",tag);
        }
        url += "?xn_out=json";
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
        FetchResult entrys = mapToApplications(accessToken,cipher);
        if (!entrys.entery.isEmpty()) {
            Application xnApplication = (Application)entrys.entery.get(0);
            this.clone(xnApplication);
            return xnApplication;
        }
        throw new WebException("no Application");
    }

}
