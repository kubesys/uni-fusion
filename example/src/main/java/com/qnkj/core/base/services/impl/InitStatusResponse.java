package com.qnkj.core.base.services.impl;


import com.qnkj.common.utils.Utils;

import java.util.HashMap;

class InitStatusResponse extends HashMap<String, Object> {
    private static final long serialVersionUID = -8713837118340960775L;

    public InitStatusResponse code(int code) {
        this.put("code", code);
        return this;
    }

    public InitStatusResponse message(String msg) {
        this.put("message", msg);
        return this;
    }

    public InitStatusResponse progress(double progress) {
        this.put("code", 201);
        this.put("progress", progress);
        return this;
    }

    public InitStatusResponse progress(double progress, String msg) {
        this.put("code", 201);
        this.put("progress", progress);
        this.put("message", msg);
        return this;
    }

    public InitStatusResponse success() {
        this.put("code", 200);
        return this;
    }

    public InitStatusResponse success(String msg) {
        this.put("code", 200);
        this.put("message", msg);
        return this;
    }

    public InitStatusResponse success(double progress, String msg) {
        this.put("code", 200);
        this.put("progress", progress);
        this.put("message", msg);
        return this;
    }

    public InitStatusResponse fail() {
        this.put("code", 500);
        return this;
    }

    public InitStatusResponse fail(String msg) {
        this.put("code", 500);
        this.put("message", msg);
        return this;
    }

    public String toJson() {
        if (Utils.isEmpty(this)) {
            return "{}";
        } else {
            return Utils.objectToJson(this);
        }
    }

    public static InitStatusResponse fromJson(String json) {
        Object obj = Utils.jsonToObject(json);
        if (!Utils.isEmpty(obj) && obj instanceof HashMap) {
            InitStatusResponse statusResponse = new InitStatusResponse();
            if (!Utils.isEmpty(((HashMap) obj).get("message"))) {
                statusResponse.message(((HashMap) obj).get("message").toString());
            }
            if (!Utils.isEmpty(((HashMap) obj).get("progress"))) {
                statusResponse.progress((double) ((HashMap) obj).get("progress"));
            }
            if (!Utils.isEmpty(((HashMap) obj).get("code"))) {
                statusResponse.code((int) ((HashMap) obj).get("code"));
            }
            return statusResponse;
        }
        return new InitStatusResponse().fail("获取状态失败");
    }
}
