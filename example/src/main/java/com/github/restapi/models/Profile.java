package com.github.restapi.models;

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

@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaLowerCamelCaseVariableNaming"})
@Slf4j
public class Profile implements Serializable {
    private static final long serialVersionUID = -4352868070794165001L;
    public String id = "";
    public String xn_type = "";
    public String application = "";
    public String fullname = "";
    public String published = "";
    public String updated = "";
    public String tag = "";

    public boolean status = true;
    public String mobile = "";
    public String email = "";
    public String givenname = "";
    public String link = "";
    public String type = "";
    public String uuid = "";
    public int identifier = 0;
    public String regioncode = "";
    public String region = "";
    public String country = "";
    public String gender = "";
    public String browser = "";
    public String system = "";
    public String reg_ip = "";
    public String identitycard = "";
    public String realname = "";
    public String city = "";
    public String province = "";
    public String birthdate = "";
    public String password = "";

    public String getId() {
        return id;
    }

    public Profile(String password) {
        this.application = XN_Rest.getApplication();
        this.password = password;
    }

    public Profile(String email, String password) {
        this.email = email;
        this.application = XN_Rest.getApplication();
        this.password = password;
    }

    public Profile(Map<String,Object> json) {
        for(String key : json.keySet()) {
            if ("id".equals(key) || "xn_id".equals(key)) {
                if (json.containsKey(key) && json.get(key) != null) {
                    id = json.get(key).toString();
                }
            }
            if ("xn_type".equals(key)) {
                if (json.containsKey(key) && json.get(key) != null) {
                    xn_type = json.get(key).toString();
                }
            }
            if ("application".equals(key) || "xn_application".equals(key)) {
                if (json.containsKey(key) && json.get(key) != null) {
                    application = json.get(key).toString();
                }
            }
            if ("title".equals(key)) {
                if (json.containsKey(key) && json.get(key) != null) {
                    fullname = json.get(key).toString();
                }
            }
            if ("fullname".equals(key)) {
                if (json.get(key) != null) {
                    fullname = json.get(key).toString();
                }
            }
            if ("status".equals(key)) {
                if (json.containsKey(key) && json.get(key) != null) {
                    status = Boolean.parseBoolean(json.get(key).toString());
                }
            }
            if ("identifier".equals(key)) {
                if (json.containsKey(key) && json.get(key) != null) {
                    identifier = Integer.parseInt(json.get(key).toString());
                }
            }
            if ("published".equals(key)) {
                if (json.get(key) != null) {
                    published = json.get(key).toString();
                }
            }
            if ("updated".equals(key)) {
                if (json.get(key) != null) {
                    updated = json.get(key).toString();
                }
            }
            if ("mobile".equals(key)) {
                if (json.get(key) != null) {
                    mobile = json.get(key).toString();
                }
            }
            if ("email".equals(key)) {
                if (json.get(key) != null) {
                    email = json.get(key).toString();
                }
            }
            if ("givenname".equals(key)) {
                if (json.get(key) != null) {
                    givenname = json.get(key).toString();
                }
            }
            if ("link".equals(key)) {
                if (json.get(key) != null) {
                    link = json.get(key).toString();
                }
            }
            if ("type".equals(key)) {
                if (json.get(key) != null) {
                    type = json.get(key).toString();
                }
            }
            if ("uuid".equals(key)) {
                if (json.get(key) != null) {
                    uuid = json.get(key).toString();
                }
            }
            if ("regioncode".equals(key)) {
                if (json.get(key) != null) {
                    regioncode = json.get(key).toString();
                }
            }
            if ("region".equals(key)) {
                if (json.get(key) != null) {
                    region = json.get(key).toString();
                }
            }
            if ("country".equals(key)) {
                if (json.get(key) != null) {
                    country = json.get(key).toString();
                }
            }
            if ("gender".equals(key)) {
                if (json.get(key) != null) {
                    gender = json.get(key).toString();
                }
            }
            if ("browser".equals(key)) {
                if (json.get(key) != null) {
                    browser = json.get(key).toString();
                }
            }
            if ("system".equals(key)) {
                if (json.get(key) != null) {
                    system = json.get(key).toString();
                }
            }
            if ("reg_ip".equals(key)) {
                if (json.get(key) != null) {
                    reg_ip = json.get(key).toString();
                }
            }
            if ("identitycard".equals(key)) {
                if (json.get(key) != null) {
                    identitycard = json.get(key).toString();
                }
            }
            if ("realname".equals(key)) {
                if (json.get(key) != null) {
                    realname = json.get(key).toString();
                }
            }
            if ("city".equals(key)) {
                if (json.get(key) != null) {
                    city = json.get(key).toString();
                }
            }
            if ("province".equals(key)) {
                if (json.get(key) != null) {
                    province = json.get(key).toString();
                }
            }
            if ("birthdate".equals(key)) {
                if (json.get(key) != null) {
                    birthdate = json.get(key).toString();
                }
            }
            if ("password".equals(key)) {
                if (json.get(key) != null) {
                    password = json.get(key).toString();
                }
            }
            application = XN_Rest.getApplication();
        }
    }
    Map<String, Object> toJson(){
        Map<String, Object> json = new HashMap<>(1);
        json.put("id", id);
        json.put("xn_id", id);
        json.put("xn_application", application);
        json.put("xn_type", xn_type);
        json.put("title", fullname);
        json.put("published", published);
        json.put("updated", updated);
        json.put("status", Boolean.toString(status));
        json.put("mobile", mobile);
        json.put("email", email);
        json.put("givenname", givenname);
        json.put("link", link);
        json.put("type", type);
        json.put("uuid", uuid);
        json.put("identifier", identifier);
        json.put("regioncode", regioncode);
        json.put("region", region);
        json.put("country", country);
        json.put("gender", gender);
        json.put("browser", browser);
        json.put("system", system);
        json.put("reg_ip", reg_ip);
        json.put("identitycard", identitycard);
        json.put("realname", realname);
        json.put("city", city);
        json.put("province", province);
        json.put("birthdate", birthdate);
        json.put("password", password);
        return json;
    }
    public void clone(Profile profile) {
        this.id = profile.id;
        this.xn_type = profile.xn_type;
        this.application = profile.application;
        this.fullname = profile.fullname;
        this.published = profile.published;
        this.updated = profile.updated;
        this.tag = profile.tag;
        this.status = profile.status;
        this.mobile = profile.mobile;
        this.email = profile.email;
        this.givenname = profile.givenname;
        this.link = profile.link;
        this.type = profile.type;
        this.uuid = profile.uuid;
        this.identifier = profile.identifier;
        this.regioncode = profile.regioncode;
        this.region = profile.region;
        this.country = profile.country;
        this.gender = profile.gender;
        this.browser = profile.browser;
        this.system = profile.system;
        this.reg_ip = profile.reg_ip;
        this.identitycard = profile.identitycard;
        this.realname = profile.realname;
        this.city = profile.city;
        this.province = profile.province;
        this.birthdate = profile.birthdate;
        this.password = profile.password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":\"").append(id).append("\"");
        sb.append(",\"xn_id\":\"").append(id).append("\"");
        sb.append(",\"xn_application\":\"").append(application).append("\"");
        sb.append(",\"xn_type\":\"").append(type).append("\"");
        sb.append(",\"fullname\":\"").append(fullname).append("\"");
        sb.append(",\"published\":\"").append(published).append("\"");
        sb.append(",\"updated\":\"").append(updated).append("\"");
        sb.append(",\"status\":\"").append(status).append("\"");
        sb.append(",\"mobile\":\"").append(mobile).append("\"");
        sb.append(",\"email\":\"").append(email).append("\"");
        sb.append(",\"givenname\":\"").append(givenname).append("\"");
        sb.append(",\"link\":\"").append(link).append("\"");
        sb.append(",\"type\":\"").append(type).append("\"");
        sb.append(",\"uuid\":\"").append(uuid).append("\"");
        sb.append(",\"identifier\":\"").append(identifier).append("\"");
        sb.append(",\"regioncode\":\"").append(regioncode).append("\"");
        sb.append(",\"region\":\"").append(region).append("\"");
        sb.append(",\"country\":\"").append(country).append("\"");
        sb.append(",\"gender\":\"").append(gender).append("\"");
        sb.append(",\"browser\":\"").append(browser).append("\"");
        sb.append(",\"system\":\"").append(system).append("\"");
        sb.append(",\"reg_ip\":\"").append(reg_ip).append("\"");
        sb.append(",\"identitycard\":\"").append(identitycard).append("\"");
        sb.append(",\"realname\":\"").append(realname).append("\"");
        sb.append(",\"province\":\"").append(province).append("\"");
        sb.append(",\"city\":\"").append(city).append("\"");
        sb.append(",\"birthdate\":\"").append(birthdate).append("\"");
        sb.append(",\"password\":\"").append(password).append("\"");
        sb.append('}');
        return sb.toString();
    }

    public static FetchResult mapToProfiles(String accessToken, Map<?,?> cipher) throws Exception {
        int size = 0;
        List<Object> profiles = new ArrayList<>(1);
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
                        List<?> entrys = (List<?>)body.get("entry");
                        entrys.forEach((Object value) -> {
                            profiles.add(new Profile((Map<String,Object>)value));
                        });
                    }
                } else {
                    throw new Exception(decryptbody.get("message").toString());
                }
            } else if (accessToken.isEmpty() || "closed".equals(accessToken)) {
                if (cipher.containsKey("size") && Integer.parseInt(cipher.get("size").toString()) > 0) {
                    size = Integer.parseInt(cipher.get("size").toString());
                    List<?> entrys = (List<?>)cipher.get("entry");
                    entrys.forEach((Object value) -> {
                        profiles.add(new Profile((Map<String,Object>)value));
                    });
                }
            } else {
                if (cipher.containsKey("error") && StringUtils.isNotEmpty(cipher.get("error").toString())) {
                    throw new Exception(cipher.get("error").toString());
                }
            }
        }
        return new FetchResult(size,profiles);
    }

    private String verify() {
        if (fullname.contains("'") || fullname.contains("\"")) {
            return "账号不能包含引号";
        }
        if (mobile.contains("'") || mobile.contains("\"")) {
            return "手机号不能包含引号";
        }
        if (email.contains("'") || email.contains("\"")) {
            return "邮箱不能包含引号";
        }
        if (givenname.contains("'") || givenname.contains("\"")) {
            return "姓名不能包含引号";
        }
        if (link.contains("'") || link.contains("\"")) {
            return "头像不能包含引号";
        }
        if (type.contains("'") || type.contains("\"")) {
            return "类型不能包含引号";
        }
        if (uuid.contains("'") || uuid.contains("\"")) {
            return "UUID不能包含引号";
        }
        if (regioncode.contains("'") || regioncode.contains("\"")) {
            return "邮编不能包含引号";
        }
        if (region.contains("'") || region.contains("\"")) {
            return "国家代码不能包含引号";
        }
        if (country.contains("'") || country.contains("\"")) {
            return "国家不能包含引号";
        }
        if (gender.contains("'") || gender.contains("\"")) {
            return "性别不能包含引号";
        }
        if (browser.contains("'") || browser.contains("\"")) {
            return "浏览器不能包含引号";
        }
        if (system.contains("'") || system.contains("\"")) {
            return "系统不能包含引号";
        }
        if (reg_ip.contains("'") || reg_ip.contains("\"")) {
            return "注册IP不能包含引号";
        }
        if (identitycard.contains("'") || identitycard.contains("\"")) {
            return "身份证不能包含引号";
        }
        if (realname.contains("'") || realname.contains("\"")) {
            return "实名不能包含引号";
        }
        if (city.contains("'") || city.contains("\"")) {
            return "城市不能包含引号";
        }
        if (province.contains("'") || province.contains("\"")) {
            return "省份不能包含引号";
        }
        if (birthdate.contains("'") || birthdate.contains("\"")) {
            return "生日不能包含引号";
        }
        if (password.contains("'") || password.contains("\"")) {
            return "密码不能包含引号";
        }
        return "";
    }

    public Profile save(String tag) throws Exception {
        if(!verify().isEmpty()) {
            throw new WebException(verify());
        }

        if (StringUtils.isNotEmpty(tag)) {
            this.tag = tag;
        }
        log.info(ColorUtils.blue() + "Profile.save" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](id:" + id + ", tag:'" + tag + "')");
        String url = "/profile";
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
            if (fme.isNeedFlush(accessToken)) {
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
        FetchResult entrys = mapToProfiles(accessToken,cipher);
        if (!entrys.entery.isEmpty()) {
            Profile profile = (Profile)entrys.entery.get(0);
            clone(profile);
            return profile;
        }
        throw new Exception("no Profile");
    }

}
