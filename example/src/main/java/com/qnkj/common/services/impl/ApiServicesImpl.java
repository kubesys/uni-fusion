package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.XN_Rest;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.Api;
import com.qnkj.common.entitys.FilterField;
import com.qnkj.common.services.IApiServices;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

@Service
public class ApiServicesImpl implements IApiServices {

    private static Map<String, Api> cacheApis = new HashMap<>();

    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }

    @Override
    public void clear() {
        cacheApis.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        if(cacheApis.containsKey(key)){
            cacheApis.remove(key);
        }
    }

    @Override
    public Api get(String modulename) {
        String key = getKey(modulename);
        if (cacheApis.containsKey(key)) {
            return cacheApis.get(key);
        }
        try {
            List<Object> objs = XN_Query.contentQuery().tag("api")
                    .filter("type", "eic", "api")
                    .filter("my.modulename", "eic", modulename)
                    .notDelete().end(-1).execute();
            if (objs.size() > 0) {
                Api api = new Api(objs.get(0));
                List<Object> filterfield = XN_Query.contentQuery().tag("filterfield")
                        .filter("type", "eic", "filterfield")
                        .filter("my.modulename", "eic", modulename)
                        .notDelete().end(-1).execute();
                if (filterfield.size() > 0) {
                    List<FilterField> lists = filterfield.stream().map( v -> new FilterField(v)).collect(Collectors.toList());
                    api.filterfields(lists);
                    cacheApis.put(key, api);
                    return api;
                }
                cacheApis.put(key, api);
                return api;
            }
        } catch (Exception ignored) {}
        return null;
    }

    @Override
    public void update(Api api) throws Exception {
        if (Utils.isEmpty(api.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        try {
            if (Utils.isNotEmpty(api.id)) {
                this.clear(api.modulename);
                Content obj = XN_Content.load(api.id, "api");
                obj.my.put("iscreate", api.iscreate ? 1 : 0);
                obj.my.put("isdelete", api.isdelete ? 1 : 0);
                obj.my.put("islist", api.islist ? 1 : 0);
                obj.my.put("isupdate", api.isupdate ? 1 : 0);
                obj.my.put("isupdaterecord", api.isupdaterecord ? 1 : 0);
                obj.my.put("ispagelimit", api.ispagelimit ? 1 : 0);
                obj.my.put("createfields", api.createfields);
                obj.my.put("updatefields", api.updatefields);
                obj.my.put("updaterecordfields", api.updaterecordfields);
                obj.my.put("listfields", api.listfields);
                obj.my.put("filterfields", api.filterfields);
                obj.save("api");
            } else {
                Content obj = XN_Content.create("content", "", XN_Rest.getViewer());
                obj.my.put("modulename", api.modulename);
                obj.my.put("iscreate", api.iscreate ? 1 : 0);
                obj.my.put("isdelete", api.isdelete ? 1 : 0);
                obj.my.put("islist", api.islist ? 1 : 0);
                obj.my.put("isupdate", api.isupdate ? 1 : 0);
                obj.my.put("isupdaterecord", api.isupdaterecord ? 1 : 0);
                obj.my.put("ispagelimit", api.ispagelimit ? 1 : 0);
                obj.my.put("createfields", api.createfields);
                obj.my.put("updatefields", api.updatefields);
                obj.my.put("updaterecordfields", api.updaterecordfields);
                obj.my.put("listfields", api.listfields);
                obj.my.put("filterfields", api.filterfields);
                obj.save("api");
            }
        }catch (Exception e) {
            throw e;
        }
    }
}
