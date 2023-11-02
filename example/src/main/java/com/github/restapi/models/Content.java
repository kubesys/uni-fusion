package com.github.restapi.models;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Credential;
import com.github.restapi.XN_Fetch;
import com.github.restapi.XN_Rest;
import com.github.restapi.utils.ColorUtils;
import com.github.restapi.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @author Oldhand
 **/

@SuppressWarnings("AlibabaUndefineMagicConstant")
@Slf4j
public class Content implements Serializable {
    private static final long serialVersionUID = 5702271568363798328L;
    public String id = "";
    public String type = "";
    public String application = "";
    public String title = "";
    public String published = "";
    public String updated = "";
    public String author = "";
    public int datatype = 0;
    public String tag = "";
    public Map<String, Object> my;

    /**
     * 新增字段，如果数据库有，则更新数据，没有创建字段并更新数据
     * @param k 字段名称
     * @param v 字段值
     * @return Content对像
     */
    public Content add(String k,Object v) {
        return set(k,v);
    }

    public Content set(String k, Object v) {
        if("id".equals(k) || "xn_id".equals(k)) {
            return this;
        }
        if("application".equals(k)) {
            return this;
        }
        if("published".equals(k)) {
            return this;
        }
        if("updated".equals(k)) {
            return this;
        }
        if("author".equals(k)) {
            return this;
        }
        my.put(k,v);
        return this;
    }
    public Object get(String k) {
        return get(k,null);
    }
    public Object get(String k, Object defval) {
        if("id".equals(k) || "xn_id".equals(k)) {
            return this.id;
        }
        if("application".equals(k)) {
            return this.application;
        }
        if("title".equals(k)) {
            return this.title;
        }
        if("published".equals(k)) {
            return this.published;
        }
        if("updated".equals(k)) {
            return this.updated;
        }
        if("author".equals(k)) {
            return this.author;
        }
        return this.my.getOrDefault(k,defval);
    }
    public Content remove(String k) {
        if(k != null && !k.isEmpty()){
            my.remove(k);
        }
        return this;
    }
    public Content setCreateNew() {
        my.put("createnew","1");
        my.put("deleted","-1");
        return this;
    }
    public Content(String xnType,String title,int datatype) {
        my = new HashMap<>();
        this.id = "";
        this.type = xnType;
        this.application = XN_Rest.getApplication();
        this.title = title;
        this.published = "";
        this.updated = "";
        this.author = "";
        this.tag = "";
        this.datatype = datatype;
        my.put("deleted",0);
    }

    public Content(Map<String,Object> json) {
        my = new HashMap<>();
        for(String key : json.keySet()){
            switch (key) {
                case "id":
                case "xn_id":
                    if (json.containsKey(key) && json.get(key) != null) {
                        id = json.get(key).toString();
                    }
                    break;
                case "type":
                case "xn_type":
                    type = json.get(key) != null ? json.get(key).toString() : "";
                    break;
                case "application":
                case "xn_application":
                    application = json.get(key) != null ? json.get(key).toString() : "";
                    break;
                case "title":
                    if (json.containsKey(key) && json.get(key) != null) {
                        title = json.get(key).toString();
                    }
                    break;
                case "published":
                    if (json.containsKey(key) && json.get(key) != null) {
                        published = json.get(key).toString();
                    }
                    break;
                case "updated":
                    if (json.containsKey(key) && json.get(key) != null) {
                        updated = json.get(key).toString();
                    }
                    break;
                case "author":
                    if (json.containsKey(key) && json.get(key) != null) {
                        author = json.get(key).toString();
                    }
                    break;
                default:
                    my.put(key, json.get(key));
                    break;
            }
        }
    }

    public void clone(Content content) {
        this.id = content.id;
        this.type = content.type;
        this.application = content.application;
        this.title = content.title;
        this.published = content.published;
        this.updated = content.updated;
        this.author = content.author;
        this.tag = content.tag;
        this.datatype = content.datatype;
        this.my = content.my;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>(1);
        json.put("id",id);
        json.put("xn_id",id);
        json.put("xn_application",application);
        json.put("xn_type",type);
        json.put("title",title);
        json.put("author",author);
        json.put("published",published);
        json.put("updated",updated);
        for(String key : my.keySet()){
            if(my.get(key).getClass().isAssignableFrom(Double.class)) {
                json.put(key, String.valueOf(my.get(key)));
            } else {
                json.put(key, my.get(key));
            }
        }
        return json;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":\"").append(id).append("\"");
        sb.append(",\"xn_id\":\"").append(id).append("\"");
        sb.append(",\"xn_application\":\"").append(application).append("\"");
        sb.append(",\"xn_type\":\"").append(type).append("\"");
        sb.append(",\"title\":\"").append(title).append("\"");
        sb.append(",\"published\":\"").append(published).append("\"");
        sb.append(",\"updated\":\"").append(updated).append("\"");
        sb.append(",\"author\":\"").append(author).append("\"");
        sb.append(",\"datatype\":\"").append(datatype).append("\"");
        sb.append(",\"my\":[");
        boolean separated = false;
        for(String key : my.keySet()){
            if (separated) {
                sb.append(",\"").append(key).append("\":\"").append(my.get(key).toString()).append("\"");
            } else {
                sb.append("\"").append(key).append("\":\"").append(my.get(key).toString()).append("\"");
                separated = true;
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    public static FetchResult mapToContents(String accessToken, Map<?,?> cipher,int datatype) throws Exception {
        int size = 0;
        List<Object> contents = new ArrayList<>();
        if (cipher != null) {
            if (cipher.containsKey("cipher") && cipher.get("cipher").toString().compareTo("") != 0) {
                String ciphertext = cipher.get("cipher").toString();
                String publickey = "";
                if (StringUtils.isNotEmpty(accessToken) && accessToken.compareTo("closed") != 0) {
                    publickey = XN_Credential.getPublicKey();
                }
                Map<?,?> decryptbody =  CryptoUtils.restDecrypt(ciphertext, publickey);
                if (decryptbody.containsKey("code") && decryptbody.get("code").toString().compareTo("0") == 0) {
                    Map<?,?> body = (Map<?,?>)decryptbody.get("body");
                    if (body.containsKey("size") && Integer.parseInt(body.get("size").toString()) > 0) {
                        size = Integer.parseInt(body.get("size").toString());
                        List<?> entrys = (List<?>)body.get("entry");
                        entrys.forEach((Object value) -> {
                            Content newcontent = new Content((Map<String,Object>)value);
                            newcontent.datatype = datatype;
                            contents.add(newcontent);
                        });
                    } else if (body.containsKey("error") && body.get("error").toString().compareTo("") != 0) {
                        throw new Exception(body.get("error").toString());
                    }
                } else {
                    throw new Exception(decryptbody.get("message").toString());
                }
            } else if (accessToken.isEmpty() || "closed".equals(accessToken)) {
                if (cipher.containsKey("size") && Integer.parseInt(cipher.get("size").toString()) > 0) {
                    size = Integer.parseInt(cipher.get("size").toString());
                    List<?> entrys = (List<?>)cipher.get("entry");
                    entrys.forEach((Object value) -> {
                        Content newcontent = new Content((Map<String,Object>)value);
                        newcontent.datatype = datatype;
                        contents.add(newcontent);
                    });
                }
            } else {
                if (cipher.containsKey("error") && cipher.get("error").toString().compareTo("") != 0) {
                    throw new Exception(cipher.get("error").toString());
                }
            }
        }
        return new FetchResult(size,contents);
    }

    /**
     * 保存
     * @param tag 缓存标签
     * @return
     * @throws Exception
     */
    public Content save(String tag) throws Exception {
        if (StringUtils.isNotEmpty(tag)) {
            this.tag = tag;
        }
        if ("".equals(id)) {
            log.info(ColorUtils.blue() + "Content.create" + ColorUtils.white() + "[" + ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](id:" + id + ", tag:'" + tag + "',datatype:" + datatype + ")");
        } else {
            log.info(ColorUtils.blue() + "Content.save" + ColorUtils.white() + "[" + ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](id:" + id + ", tag:'" + tag + "',datatype:" + datatype + ")");
        }
        String url = XN_Content.transdatatype(this.datatype);
        if (url.isEmpty()) {
            throw new Exception("XN_Content.save Data type parameter error");
        }
        Map<String, String> headers = new HashMap<>(1);
        headers.put("domain",XN_Rest.getApplication());
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
                    throw new Exception(fe.errormsg);
                }
            } else {
                throw new Exception(fme.errormsg);
            }
        }
        FetchResult entrys = mapToContents(accessToken,cipher,this.datatype);
        if (!entrys.entery.isEmpty()) {
            Content content = (Content)entrys.entery.get(0);
            clone(content);
            return content;
        }
        throw new Exception("no Content");
    }


    public List<Content> excuteSql(String tag) throws Exception {
        if (StringUtils.isNotEmpty(tag)) {
            this.tag = tag;
        }
        log.info(ColorUtils.blue() + "XN_Content.excuteSql" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](id:" + id + ", tag:'" + tag + "',datatype:" + datatype + ")");
        String url = XN_Content.transdatatype(this.datatype);
        if (url.isEmpty()) {
            throw new Exception("XN_Content.save Data type parameter error");
        }
        Map<String, String> headers = new HashMap<>(1);
        headers.put("domain",XN_Rest.getApplication());
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
                    throw new Exception(fe.errormsg);
                }
            } else {
                throw new Exception(fme.errormsg);
            }
        }
        FetchResult entrys = mapToContents(accessToken,cipher,this.datatype);
        List<Content> results = new ArrayList<>();
        for (Object item : entrys.entery) {
            results.add((Content)item);
        }
        return results;
    }

    public void delete(String tag) throws Exception {
        if (StringUtils.isNotEmpty(tag)) {
            this.tag = tag;
        }
        log.info(ColorUtils.blue() + "Content.delete" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](id:" + id + ", tag:'" + tag + "',datatype:" + datatype + ")");
        String url = XN_Content.transdatatype(this.datatype);
        if (url.isEmpty()) {
            throw new Exception("XN_Content.save datatype parameter error");
        }
        if (this.id.isEmpty()) {
            throw new Exception("XN_Content.save id parameter error");
        }
        Map<String, String> headers = new HashMap<>(1);
        headers.put("domain",XN_Rest.getApplication());
        if (StringUtils.isNotEmpty(tag)) {
            headers.put("tag",tag);
        }
        url += "(id="+this.id+")";
        url += "?xn_out=json";
        String accessToken = XN_Credential.get();
        Map<?,?> cipher;
        try {
            cipher =  XN_Fetch.delete(url,accessToken,headers);
        } catch (FetchMsgException fme) {
            if (Boolean.TRUE.equals(fme.isNeedFlush(accessToken))) {
                try {
                    accessToken = XN_Credential.flush();
                    cipher =  XN_Fetch.delete(url,accessToken,headers);
                } catch (FetchMsgException fe) {
                    throw new Exception(fe.errormsg);
                }
            } else {
                throw new Exception(fme.errormsg);
            }
        }
        if (cipher != null) {
            if (cipher.containsKey("cipher") && !cipher.get("cipher").toString().isEmpty()) {
                String ciphertext = cipher.get("cipher").toString();
                String publickey = "";
                if (StringUtils.isNotEmpty(accessToken) && "closed".equals(accessToken)) {
                    publickey = XN_Credential.getPublicKey();
                }
                Map<?,?> decryptbody =  CryptoUtils.restDecrypt(ciphertext, publickey);
                if (decryptbody.containsKey("code") && "0".equals(decryptbody.get("code").toString())) {
                    Map<?,?> body = (Map<?,?>)decryptbody.get("body");
                    if (body.containsKey("code") && Integer.parseInt(body.get("code").toString()) == 0) {
                        return;
                    }
                }
            } else if ("closed".equals(accessToken)) {
                if (cipher.containsKey("code") && Integer.parseInt(cipher.get("code").toString()) == 0) {
                    return;
                }
            }
        }
        throw new Exception("delete failure");
    }
}
