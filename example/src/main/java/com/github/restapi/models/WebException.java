package com.github.restapi.models;

/**
 * 系统内部异常
 *
 * @author Oldhand
 */
public class WebException extends RuntimeException  {

    private static final long serialVersionUID = -994962710559017255L;

    public WebException(String message) {
        super(message);
    }
}
