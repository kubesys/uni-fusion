package com.qnkj.common.services;

import com.qnkj.common.entitys.TabField;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface ITabFieldServices {

    void clear();

    void clear(String modulename);

    void clear(String modulename, TabField field);

    TabField get(String modulename, String fieldname);

    TabField load(String fieldid);

    List<TabField> list(String modulename);

    List<TabField> list(String modulename, String blockid);

    void update(TabField tabField) throws Exception;

    void update(List<TabField> tabFields) throws Exception;
}
