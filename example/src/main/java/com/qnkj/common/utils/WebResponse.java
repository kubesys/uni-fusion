package com.qnkj.common.utils;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * @author Oldhand
 */
public class WebResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public WebResponse code(Object code) {
        if(code instanceof HttpStatus){
            this.put("code", ((HttpStatus) code).value());
        } else {
            this.put("code", code);
        }
        return this;
    }

    public WebResponse message(String message) {
        this.put("message", message);
        this.put("msg",message);
        return this;
    }

    public WebResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    public WebResponse totalRow(Object totalRow) {
        this.put("totalRow", totalRow);
        return this;
    }

    public WebResponse count(Object count) {
        this.put("count",count);
        return this;
    }

    public WebResponse record(Object record) {
        if(Utils.isEmpty(record)){
            this.put("record","");
        }else {
            this.put("record", record);
        }
        return this;
    }

    public WebResponse success() {
        this.code(HttpStatus.OK);
        return this;
    }

    public WebResponse success(String message) {
        this.code(HttpStatus.OK).message(message);
        return this;
    }

    public WebResponse success(int code,String message) {
        this.code(code).message(message);
        return this;
    }

    public WebResponse fail() {
        this.code(HttpStatus.INTERNAL_SERVER_ERROR);
        return this;
    }

    public WebResponse alert() {
        this.code(600);
        return this;
    }

    public WebResponse alert(String msg) {
        this.code(600).message(msg);
        return this;
    }
    public WebResponse title(String title) {
        this.put("title",title);
        return this;
    }

    public WebResponse fail(String message) {
        this.code(HttpStatus.INTERNAL_SERVER_ERROR).message(message);
        return this;
    }

    public WebResponse fail(int code,String message) {
        this.code(code).message(message);
        return this;
    }

    public WebResponse fail(int code) {
        this.code(code);
        return this;
    }

    public WebResponse refresh(){
        this.put("refresh",true);
        return this;
    }
    public WebResponse redirect(String url){
        this.put("redirect",url);
        return this;
    }


    public WebResponse close() {
        this.put("close",true);
        return this;
    }

    @Override
    public WebResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
