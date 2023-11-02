package com.qnkj.common.services;

import com.qnkj.common.entitys.SearchColumn;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface ISearchColumnServices {

    void clear();
    void clear(String modulename);

    SearchColumn get(String modulename, String fieldname);

    SearchColumn load(String columnid);

    List<SearchColumn> list(String modulename);

    void update(SearchColumn searchColumn) throws Exception;
}
