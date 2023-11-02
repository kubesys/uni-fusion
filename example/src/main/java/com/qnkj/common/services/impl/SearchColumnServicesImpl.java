package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.SearchColumn;
import com.qnkj.common.services.ISearchColumnServices;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

@Service
public class SearchColumnServicesImpl implements ISearchColumnServices {

    private static Map<String, List<SearchColumn>> cacheSearchColumns = new HashMap<>();

    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }

    @Override
    public void clear() {
        cacheSearchColumns.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        cacheSearchColumns.remove(key);
    }

    @Override
    public SearchColumn get(String modulename, String fieldname) {
        String key = getKey(modulename);
        if (!cacheSearchColumns.containsKey(key)) {
            list(modulename);
        }
        if (cacheSearchColumns.containsKey(key)) {
            List<SearchColumn> searchcolumns = cacheSearchColumns.get(key);
            for (SearchColumn searchcolumn : searchcolumns) {
                if (searchcolumn.fieldname.equals(fieldname)) {
                    return searchcolumn;
                }
            }
        }
        return null;
    }

    @Override
    public SearchColumn load(String columnid) {
        for (Map.Entry<String, List<SearchColumn>> entry : cacheSearchColumns.entrySet()) {
            List<SearchColumn> searchcolumns = entry.getValue();
            for (SearchColumn searchcolumn : searchcolumns) {
                if (searchcolumn.id.equals(columnid)) {
                    return searchcolumn;
                }
            }
        }
        return null;
    }

    @Override
    public List<SearchColumn> list(String modulename) {
        try {
            String key = getKey(modulename);
            if (!cacheSearchColumns.containsKey(key)) {
                List<Object> query = XN_Query.contentQuery().tag("searchcolumns")
                        .filter("type", "eic", "searchcolumns")
                        .filter("my.modulename", "eic", modulename)
                        .notDelete().order("my.order", "A_N")
                        .end(-1).execute();
                if (!query.isEmpty()) {
                    List<SearchColumn> result = new ArrayList<>();
                    for (Object item : query) {
                        result.add(new SearchColumn(item));
                    }
                    cacheSearchColumns.put(key, result);
                }
            }
            if (cacheSearchColumns.containsKey(key)) {
                return cacheSearchColumns.get(key);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void update(SearchColumn searchColumn) throws Exception {
        if (Utils.isEmpty(searchColumn.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        this.clear(searchColumn.modulename);
        if (Utils.isEmpty(searchColumn.id)) {
            Content conn = XN_Content.create("searchcolumns", "");
            searchColumn.toContent(conn);
            conn.save("searchcolumns");
            searchColumn.fromContent(conn);
        } else {
            Content conn = XN_Content.load(searchColumn.id, "searchcolumns");
            if(searchColumn.deleted == 0) {
                searchColumn.toContent(conn);
                conn.save("searchcolumns");
            } else {
                conn.delete("searchcolumns");
            }
        }
//        if(searchColumn.deleted == 0) {
//            String key = getKey(searchColumn.modulename);
//            if (!cacheSearchColumns.containsKey(key)) {
//                cacheSearchColumns.put(key, new ArrayList<>());
//            }
//            cacheSearchColumns.get(key).add(searchColumn);
//            cacheSearchColumns.get(key).sort((o1, o2) -> {
//                int diff = o1.order - o2.order;
//                return Integer.compare(diff, 0);
//            });
//        }
    }
}
