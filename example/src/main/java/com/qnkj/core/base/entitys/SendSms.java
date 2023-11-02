package com.qnkj.core.base.entitys;

import com.alibaba.fastjson.JSONObject;

public class SendSms {
    public String mobile;
    public String verifyCode;
    public long time;

    public SendSms(String mobile,String verifyCode,long time) {
        this.mobile = mobile;
        this.verifyCode = verifyCode;
        this.time = time;
    }

    public SendSms(String json) {
        try {
            JSONObject jsonbody = JSONObject.parseObject(json);
            this.mobile = jsonbody.get("mobile").toString();
            this.verifyCode = jsonbody.get("verifyCode").toString();
            this.time = Long.parseLong(jsonbody.get("time").toString());
        }catch (Exception e) {
            this.mobile = "";
            this.verifyCode = "";
            this.time = 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"mobile\":\"").append(mobile).append("\"");
        sb.append(",\"verifyCode\":\"").append(verifyCode).append("\"");
        sb.append(",\"time\":").append(time);
        sb.append("}");
        return sb.toString();
    }
}
