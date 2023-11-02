package com.qnkj.core.plugins.netty.enums;

/**
 *
 */
public enum ResultEnum {
    /**
     *
     */
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "ok"),
    PRIMARY_SCHOOL(100, "没有相关信息"),
    MIDDLE_SCHOOL(101, "参数错误"),
    TIME_OUT(102,"网络超时");

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
