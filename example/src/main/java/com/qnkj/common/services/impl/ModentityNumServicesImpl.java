package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.ModentityNum;
import com.qnkj.common.services.IModentityNumServices;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/7
 */

@Service
public class ModentityNumServicesImpl implements IModentityNumServices {
    private static HashMap<String, ModentityNum> cacheModentityNum = new HashMap<>();

    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }

    @Override
    public void clear() {
        cacheModentityNum.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        cacheModentityNum.remove(key);
    }

    @Override
    public ModentityNum get(String modulename) {
        try{
            String key = getKey(modulename);
            if(!cacheModentityNum.containsKey(key)){
                List<Object> query = XN_Query.contentQuery().tag("modentity_nums")
                        .filter("type", "eic", "modentity_nums")
                        .filter("my.semodule", "eic", modulename)
                        .notDelete().end(1).execute();
                if (!query.isEmpty()) {
                    cacheModentityNum.put(key,new ModentityNum(query.get(0)));
                }
            }
            if(cacheModentityNum.containsKey(key)) {
                return cacheModentityNum.get(key);
            }
        }catch (Exception ignored){}
        return null;
    }

    @Override
    public ModentityNum load(String modentitynumid) {
        try{
            for(String modulename: cacheModentityNum.keySet()){
                String key = getKey(modulename);
                if(cacheModentityNum.get(key).id.equals(modentitynumid)){
                    return cacheModentityNum.get(key);
                }
            }
            Content conn = XN_Content.load(modentitynumid,"modentity_nums");
            ModentityNum modentityNum = new ModentityNum(conn);
            String key = getKey(modentityNum.semodule);
            cacheModentityNum.put(key,modentityNum);
            return modentityNum;
        }catch (Exception ignored) {}
        return null;
    }

    @Override
    public void update(ModentityNum modentityNum) throws Exception {
        this.clear(modentityNum.semodule);
        if(Utils.isEmpty(modentityNum.id)){
            Content conn = XN_Content.create("modentity_nums","");
            modentityNum.toContent(conn);
            conn.add("active",1)
                    .add("tabid", DateTimeUtils.getDatetime("ddss"))
                    .add("date",DateTimeUtils.getDatetime("yyMMdd"))
                    .save("modentity_nums");
            modentityNum.fromContent(conn);
        } else {
            Content conn = XN_Content.load(modentityNum.id,"modentity_nums");
            if(modentityNum.deleted == 0) {
                modentityNum.toContent(conn);
                conn.save("modentity_nums");
            } else {
                conn.delete("modentity_nums");
            }
        }
        if(modentityNum.deleted == 0) {
            String key = getKey(modentityNum.semodule);
            cacheModentityNum.put(key, modentityNum);
        }
    }
}
