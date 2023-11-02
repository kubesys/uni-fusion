package com.qnkj.common.services;

import com.qnkj.common.entitys.ModentityNum;

/**
 * create by 徐雁
 * create date 2020/11/7
 */

public interface IModentityNumServices {
    void clear();
    void clear(String modulename);
    ModentityNum get(String modulename);

    ModentityNum load(String modentitynumid);

    void update(ModentityNum modentityNum) throws Exception;
}
