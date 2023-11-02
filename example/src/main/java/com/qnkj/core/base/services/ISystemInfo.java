package com.qnkj.core.base.services;


import java.util.Map;

/**
 * @author Oldhand
 */
public interface ISystemInfo {

    /**
     * 获取系统信息
     */
    Map<String,Object> get() throws Exception;


}
