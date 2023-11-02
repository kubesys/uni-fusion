package com.qnkj.common.services;

import com.qnkj.common.entitys.Program;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/6
 */

public interface IProgramServices {

    void clear();

    void clear(String group);

    Program get(String group);

    Program load(String programid);

    List<Program> list();

    void update(Program program) throws Exception;
}
