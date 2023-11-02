package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.EntityLink;
import com.qnkj.common.services.IEntityLinkServices;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

@Service
public class EntityLinkServicesImpl implements IEntityLinkServices {

    private static Map<String, EntityLink> cacheEntityLinks = new HashMap<>();


    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }

    @Override
    public void clear() {
        cacheEntityLinks.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        cacheEntityLinks.remove(key);
    }

    private void init() {
        try {
            String application = BaseSaasConfig.getApplication();
            Boolean inited = false;
            for(String item : cacheEntityLinks.keySet()) {
                if (item.indexOf(application) == 0) {
                    inited = true;
                    break;
                }
            }
            if (!inited) {
                List<Object> entitynames = XN_Query.contentQuery().tag("entitynames")
                        .filter("type", "eic", "entitynames")
                        .notDelete().end(-1).execute();
                if (!entitynames.isEmpty()) {
                    for (Object entityname : entitynames) {
                        Content item = (Content) entityname;
                        String modulename = item.my.get("modulename").toString();
                        String key = getKey(modulename);
                        cacheEntityLinks.put(key, new EntityLink(item));
                    }
                }
            }
        } catch (Exception ignored) { }
    }
    @Override
    public EntityLink get(String modulename) {
        try {
            init();
            String key = getKey(modulename);
            if(cacheEntityLinks.containsKey(key)) {
                return cacheEntityLinks.get(key);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public EntityLink load(String entityid) {
        if (!Utils.isEmpty(entityid)) {
            init();
            for (Map.Entry<String, EntityLink> entry : cacheEntityLinks.entrySet()) {
                EntityLink entitylink = entry.getValue();
                if (entityid.equals(entitylink.id)) {
                    return entitylink;
                }
            }
            try {
                Content conn = XN_Content.load(entityid, "entitynames");
                EntityLink entitylink = new EntityLink(conn);
                String key = getKey(entitylink.modulename);
                cacheEntityLinks.put(key,entitylink);
                return entitylink;
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public void update(EntityLink entityLink) throws Exception {
        if (Utils.isEmpty(entityLink.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        if (!Utils.isEmpty(entityLink.id)) {
            this.clear(entityLink.modulename);
            Content conn = XN_Content.load(entityLink.id, "entitynames");
            entityLink.toContent(conn);
            conn.save("entitynames");
        } else {
            List<Object> query = XN_Query.contentQuery().tag("entitynames")
                    .filter("type", "eic", "entitynames")
                    .filter("my.modulename", "eic", entityLink.modulename)
                    .notDelete().end(1).execute();
            if (!query.isEmpty()) {
                query.clear();
                throw new Exception("内联信息已经存在！");
            } else {
                this.clear(entityLink.modulename);
                Content conn = XN_Content.create("entitynames", "");
                if(entityLink.deleted == 0) {
                    entityLink.toContent(conn);
                    conn.save("entitynames");
                    entityLink.fromContent(conn);
                } else {
                    conn.delete("entitynames");
                }
            }
        }
        if(entityLink.deleted == 0) {
            String key = getKey(entityLink.modulename);
            cacheEntityLinks.put(key, entityLink);
        }
    }
}
