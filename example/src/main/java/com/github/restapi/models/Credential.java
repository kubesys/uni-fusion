package com.github.restapi.models;

import java.util.HashMap;
import java.util.Map;

/**
 * @author oldhand
 */
public class Credential {
    public String accessToken;
    public String publicKey;
    public long timestamp;
    public long localtime;

    public Credential(String accessToken, String publicKey, long timestamp, long localtime) {
        this.accessToken = accessToken;
        this.publicKey = publicKey;
        this.timestamp = timestamp;
        this.localtime = localtime;
    }

    public Credential(Map<String, Object> json) {
        this.accessToken = json.get("access_token").toString();
        this.publicKey = json.get("public_key").toString();
        this.timestamp = Integer.parseInt(json.get("timestamp").toString());
        this.localtime = Integer.parseInt(json.get("localtime").toString());
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>(1);
        json.put("access_token",accessToken);
        json.put("public_key",publicKey);
        json.put("timestamp",timestamp);
        json.put("localtime",localtime);
        return json;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"access_token\":\"").append(accessToken).append("\"");
        sb.append(",\"public_key\":\"").append(publicKey).append("\"");
        sb.append(",\"timestamp\":\"").append(timestamp).append("\"");
        sb.append(",\"localtime\":\"").append(localtime).append("\"");
        sb.append("}");
        return sb.toString();
    }

}
