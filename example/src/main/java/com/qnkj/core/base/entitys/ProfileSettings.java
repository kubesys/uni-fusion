package com.qnkj.core.base.entitys;


import com.alibaba.fastjson.JSONObject;

public class ProfileSettings {
    public String theme;
    public String istab;
    public String menu;
    public Boolean isDev;
    public Integer pageLimit;

    public ProfileSettings() {
        this.theme = "blue";
        this.istab = "0";
        this.menu = "";
        this.isDev = false;
        this.pageLimit = 50;
    }
    public ProfileSettings(String json) {
        try {
            JSONObject jsonbody = JSONObject.parseObject(json);
            this.theme = jsonbody.get("theme").toString();
            this.istab = jsonbody.get("istab").toString();
            this.menu = jsonbody.get("menu").toString();
            this.isDev = Boolean.parseBoolean(jsonbody.get("isdev").toString());
            if (jsonbody.containsKey("pagelimit")) {
                this.pageLimit = Integer.parseInt(jsonbody.get("pagelimit").toString());
            } else {
                this.pageLimit = 50;
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"theme\":\"").append(theme).append("\"");
        sb.append(",\"istab\":\"").append(istab).append("\"");
        sb.append(",\"menu\":\"").append(menu).append("\"");
        sb.append(",\"isdev\":\"").append(isDev.toString()).append("\"");
        sb.append(",\"pagelimit\":").append(String.valueOf(pageLimit));
        sb.append("}");
        return sb.toString();
    }
}
