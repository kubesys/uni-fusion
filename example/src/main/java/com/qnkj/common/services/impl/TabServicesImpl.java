package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.Tab;
import com.qnkj.common.services.ITabServices;
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
public class TabServicesImpl implements ITabServices {

    private static Map<String,Map<String,Tab>> cacheTabs = new HashMap<>();

    @Override
    public void clear() {
        cacheTabs.clear();
    }

    @Override
    public void clear(String modulename) {
        String application = BaseSaasConfig.getApplication();
        if (cacheTabs.containsKey(application)) {
            cacheTabs.get(application).remove(modulename);
        }
    }

    @Override
    public Tab get(String modulename) {
        String application = BaseSaasConfig.getApplication();
        if (!cacheTabs.containsKey(application)) {
            this.load();
        }
        Map<String,Tab> tabs = cacheTabs.get(application);
        if (tabs.containsKey(modulename)) {
            return tabs.get(modulename);
        } else {
            try {
                List<Object> query = XN_Query.contentQuery().tag("tabs")
                        .filter("type", "eic", "tabs")
                        .filter("my.modulename","eic",modulename)
                        .end(-1).execute();
                if (!query.isEmpty()) {
                    for(Object item: query){
                        Tab tab = new Tab(item);
                        tabs.put(tab.modulename,tab);
                    }
                }
                if (tabs.containsKey(modulename)) {
                    return tabs.get(modulename);
                }
            }catch (Exception ignored){}
        }
        return null;
    }

    @Override
    public Tab load(String tabid) {
        try{
            for (Map.Entry<String, Map<String,Tab>> entry : cacheTabs.entrySet()) {
                for (Map.Entry<String, Tab> item : entry.getValue().entrySet()) {
                    if(item.getValue().id.equals(tabid)){
                        return item.getValue();
                    }
                }
            }
            String application = BaseSaasConfig.getApplication();
            if (cacheTabs.containsKey(application)) {
                Map<String, Tab> tabs = cacheTabs.get(application);
                Content conn = XN_Content.load(tabid,"tabs");
                Tab tab = new Tab(conn);
                tabs.put(tab.modulename,tab);
                return tab;
            }
        }catch (Exception ignored){}
        return null;
    }

    @Override
    public List<Tab> load() {
        try {
            String application = BaseSaasConfig.getApplication();
            if (!cacheTabs.containsKey(application)) {
                List<Object> query = XN_Query.contentQuery().tag("tabs")
                        .filter("type", "eic", "tabs")
                        .notDelete().end(-1).execute();
                if (!query.isEmpty()) {
                    Map<String,Tab> tabs = new HashMap<>();
                    for(Object item: query){
                        Tab tab = new Tab(item);
                        tabs.put(tab.modulename,tab);
                    }
                    cacheTabs.put(application,tabs);
                    return new ArrayList<>(tabs.values());
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void update(Tab tab) throws Exception {
        if(Utils.isEmpty(tab.tabname) && Utils.isEmpty(tab.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        if(Utils.isEmpty(tab.modulename)) {
            tab.modulename(tab.tabname);
        }
        if(Utils.isEmpty(tab.modulelabel)) {
            tab.modulename(tab.modulelabel);
        }
        this.clear(tab.modulename);
        if(Utils.isEmpty(tab.id)){
            Content conn = XN_Content.create("tabs","");
            tab.toContent(conn);
            conn.save("tabs");
            tab.fromContent(conn);
        }else {
            Content conn = XN_Content.load(tab.id,"tabs");
            if(tab.deleted == 0) {
                tab.toContent(conn);
                conn.save("tabs");
            } else {
                conn.delete("tabs");
            }
        }
        if(tab.deleted == 0) {
            String application = BaseSaasConfig.getApplication();
            if (cacheTabs.containsKey(application)) {
                Map<String,Tab> tabs = cacheTabs.get(application);
                tabs.put(tab.modulename, tab);
            }
        }
    }
}
