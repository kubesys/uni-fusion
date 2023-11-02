package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.services.ICustomViewServices;
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
public class CustomViewServicesImpl implements ICustomViewServices {

    private static Map<String, List<CustomView>> cacheCustomViews = new HashMap<>();

    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }

    @Override
    public void clear() {
        cacheCustomViews.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        if(cacheCustomViews.containsKey(key)){
            cacheCustomViews.remove(key);
        }
    }

    @Override
    public void clear(String modulename, String viewid) {
        String key = getKey(modulename);
        if(cacheCustomViews.containsKey(key)){
            cacheCustomViews.get(key).removeIf(customView -> customView.id.equals(viewid));
        }
    }

    private List<String> getColumnlists(String viewid) {
        List<String> result = new ArrayList<>();
        try {
            List<Object> query = XN_Query.contentQuery().tag("cvcolumnlists")
                    .filter("type", "eic", "cvcolumnlists")
                    .filter("my.record", "=", viewid)
                    .order("my.sequence", "A_N")
                    .notDelete().end(-1).execute();
            if (!query.isEmpty()) {
                for (Object item : query) {
                    result.add(((Content) item).my.get("columnname").toString());
                }
                query.clear();
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    @Override
    public CustomView get(String modulename, String viewname) {
        String key = getKey(modulename);
        if (!cacheCustomViews.containsKey(key)) {
            this.list(key);
        }
        if (cacheCustomViews.containsKey(key)) {
            List<CustomView> customviews = cacheCustomViews.get(key);
            for (CustomView customview : customviews) {
                if (viewname.equals(customview.viewname)) {
                    return customview;
                }
            }
        }
        return null;
    }

    @Override
    public CustomView load(String customid) {
        if (!Utils.isEmpty(customid)) {
            try {
                for (Map.Entry<String, List<CustomView>> entry : cacheCustomViews.entrySet()) {
                    List<CustomView> customviews = entry.getValue();
                    for (CustomView customview : customviews) {
                        if (customview.id.equals(customid)) {
                            return customview;
                        }
                    }
                }
                Content conn = XN_Content.load(customid, "customviews");
                CustomView result = new CustomView(conn);
                result.columnlist(getColumnlists(result.id));
                String key = getKey(result.modulename);
                if (!cacheCustomViews.containsKey(key)) {
                    cacheCustomViews.put(key, new ArrayList<>());
                }
                cacheCustomViews.get(key).add(result);
                return result;
            } catch (Exception ignored) {
            }

        }
        return null;
    }

    @Override
    public List<CustomView> list(String modulename) {
        String key = getKey(modulename);
        try {
            if (!cacheCustomViews.containsKey(key)) {
                List<CustomView> customviews = new ArrayList<>();
                List<Object> query = XN_Query.contentQuery().tag("customviews")
                        .filter("type", "eic", "customviews")
                        .filter("my.modulename", "eic", modulename)
                        .notDelete().end(-1).execute();
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        CustomView result = new CustomView(item);
                        result.columnlist(getColumnlists(result.id));
                        customviews.add(result);
                    }
                    query.clear();
                    cacheCustomViews.put(key, customviews);
                }
            }
            if (cacheCustomViews.containsKey(key)) {
                List<CustomView> result = new ArrayList<>();
                for(CustomView item: cacheCustomViews.get(key)){
                    if(Utils.isEmpty(item.privateuser)){
                        result.add(item);
                    }
                }
                return result;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public List<CustomView> list(String modulename, String profileid) {
        String key = getKey(modulename);
        try {
            if (!cacheCustomViews.containsKey(key)) {
                List<CustomView> customviews = new ArrayList<>();
                List<Object> query = XN_Query.contentQuery().tag("customviews")
                        .filter("type", "eic", "customviews")
                        .filter("my.modulename", "eic", modulename)
                        .notDelete().end(-1).execute();
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        CustomView result = new CustomView(item);
                        result.columnlist(getColumnlists(result.id));
                        customviews.add(result);
                    }
                    query.clear();
                    cacheCustomViews.put(key, customviews);
                }
            }
            if (cacheCustomViews.containsKey(key)) {
                List<CustomView> result = new ArrayList<>();
                for(CustomView item: cacheCustomViews.get(key)){
                    if(Utils.isEmpty(item.privateuser) || item.privateuser.equals(profileid)){
                        result.add(item);
                    }
                }
                return result;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void update(CustomView customView) throws Exception {
        if (Utils.isEmpty(customView.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        String key = getKey(customView.modulename);
        int index = 1;
        if (!Utils.isEmpty(customView.id)) {
            this.clear(customView.modulename, customView.id);
            Content conn = XN_Content.load(customView.id, "customviews");
            if(customView.deleted == 0) {
                conn.add("viewname", customView.viewname)
                        .add("authorize", customView.authorize)
                        .add("orderby", customView.orderby)
                        .add("order", customView.order)
                        .add("deleted", customView.deleted)
                        .add("isdefault", customView.isdefault ? "1" : "0")
                        .add("privateuser", customView.privateuser)
                        .save("customviews");
            } else {
                conn.set("deleted",customView.deleted).save("customviews");
            }
            List<Object> query;
            do {
                query = XN_Query.contentQuery().tag("cvcolumnlists")
                        .filter("type", "eic", "cvcolumnlists")
                        .filter("my.record", "=", customView.id)
                        .begin(0).end(100).execute();
                XN_Content.delete(query, "cvcolumnlists");
            } while (query.size() == 100);
            if(customView.deleted == 0) {
                List<String> columnlist = customView.columnlist;
                for (Object column : columnlist) {
                    Content cvcolumnlists = XN_Content.create("cvcolumnlists", "");
                    cvcolumnlists.add("privateuser", customView.privateuser).add("modulename", customView.modulename).add("columnname", column).add("sequence", index).add("record", customView.id).save("cvcolumnlists");
                    index++;
                }
            }
        } else {
            List<String> columnlist = customView.columnlist;
            if (Utils.isEmpty(columnlist)) {
                throw new Exception("显示列不能为空！");
            }
            Content tabs = XN_Content.create("customviews", "");
            String viewid = tabs.add("modulename", customView.modulename)
                    .add("viewname", customView.viewname)
                    .add("authorize", customView.authorize)
                    .add("orderby", customView.orderby)
                    .add("order", customView.order)
                    .add("privateuser", customView.privateuser)
                    .add("isdefault", customView.isdefault ? "1" : "0")
                    .save("customviews").id;
            for (Object column : columnlist) {
                Content cvcolumnlists = XN_Content.create("cvcolumnlists", "");
                cvcolumnlists.add("privateuser", customView.privateuser).add("modulename", customView.modulename).add("columnname", column).add("sequence", index).add("record", viewid).save("cvcolumnlists");
                index++;
            }
            customView.id = viewid;
        }
        if(customView.deleted == 0) {
            if (!cacheCustomViews.containsKey(key)) {
                cacheCustomViews.put(key, new ArrayList<>());
            }
            cacheCustomViews.get(key).add(customView);
        }
    }
}
