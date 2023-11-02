package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.Layout;
import com.qnkj.common.services.ILayoutServices;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2021/5/11
 */
@Service
public class LayoutServicesImpl implements ILayoutServices {
    private static Map<String, List<Layout>> cacheLayouts = new HashMap<>();
    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }

    @Override
    public void clear() {
        cacheLayouts.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        cacheLayouts.remove(key);
    }

    @Override
    public List<Layout> list(String modulename) {
        try{
            String key = getKey(modulename);
            if(!cacheLayouts.containsKey(key) || cacheLayouts.get(key).size() <= 0) {
                List<Layout> layouts = new ArrayList<>();
                List<Object> query = XN_Query.contentQuery().tag("layouts")
                        .filter("type", "eic", "layouts")
                        .filter("my.modulename", "eic", modulename)
                        .order("my.sequence", "A_N")
                        .notDelete().end(-1).execute();
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        layouts.add(new Layout(item));
                    }
                    cacheLayouts.put(key, layouts);
                }
            }
            if(cacheLayouts.containsKey(key)){
                return cacheLayouts.get(key);
            }
        }catch (Exception ignored) {}
        return null;
    }

    @Override
    public void update(Layout layout) throws Exception {
        if (Utils.isEmpty(layout.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        this.clear(layout.modulename);
        if (Utils.isEmpty(layout.id)) {
            Content conn = XN_Content.create("layouts", "");
            layout.toContent(conn);
            conn.save("layouts");
            layout.fromContent(conn);
        } else {
            Content conn = XN_Content.load(layout.id, "layouts");
            if(layout.deleted == 0) {
                layout.toContent(conn);
                conn.save("layouts");
            } else {
                conn.delete("layouts");
            }
        }
    }
}
