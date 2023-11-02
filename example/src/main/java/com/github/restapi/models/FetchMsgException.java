package com.github.restapi.models;

/**
 * @author Oldhand
 **/

public class FetchMsgException extends Exception {
    public int code;
    public String errormsg;

    public FetchMsgException(int code,String errormsg) {
        this.code = code;
        this.errormsg = errormsg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"code\":").append(code);
        sb.append(",\"errormsg\":\"").append(errormsg).append("\"");
        sb.append("}");
        return sb.toString();
    }

    private final String CLOSED_TOKEN = "closed";
    private final int OVERDUE_CODE = 1;
    private final int ERROR_CODE = 2;

    public Boolean isNeedFlush(String accessToken) {
        if (CLOSED_TOKEN.equals(accessToken)) {
            return false;
        } else if (code == OVERDUE_CODE || code == ERROR_CODE) {
            return true;
        }
        return false;
    }
}
