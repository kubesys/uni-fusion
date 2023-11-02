package com.qnkj.common.services;

import com.qnkj.common.entitys.ParentTab;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface IParentTabServices {

    void clear();

    void clear(String program, String parentname);

    ParentTab get(String program, String parentname);


    ParentTab load(String tabid);

    List<ParentTab> list(String program);

    void update(ParentTab parentTab) throws Exception;

    void updateSubtab(ParentTab parentTab) throws Exception;
}
