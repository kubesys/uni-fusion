package com.qnkj.core.base.services;

/**
 * create by 徐雁
 */
public interface IInitService {
    void initpt() throws Exception;
    void initdata(String modulename) throws Exception;
    Object getStatus();
    void clearStatus();
}
