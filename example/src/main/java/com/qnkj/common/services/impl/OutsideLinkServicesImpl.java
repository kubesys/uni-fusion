package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.OutsideLink;
import com.qnkj.common.services.IOutsideLinkServices;
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
public class OutsideLinkServicesImpl implements IOutsideLinkServices {

    private static Map<String, List<OutsideLink>> cacheOutsideLinks = new HashMap<>();

    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }

    @Override
    public void clear() {
        cacheOutsideLinks.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        if(cacheOutsideLinks.containsKey(key)){
            cacheOutsideLinks.remove(key);
        }
    }

    @Override
    public void clear(String modulename,String fieldname) {
        String key = getKey(modulename);
        if(cacheOutsideLinks.containsKey(key)){
            cacheOutsideLinks.get(key).removeIf(outsideLink -> outsideLink.fieldname.equals(fieldname));
        }
    }

    @Override
    public OutsideLink get(String modulename, String field) {
        String key = getKey(modulename);
        if (!cacheOutsideLinks.containsKey(key)) {
            this.list(modulename);
        }
        if (cacheOutsideLinks.containsKey(key)) {
            List<OutsideLink> outsidelinks = cacheOutsideLinks.get(key);
            for (OutsideLink outsidelink : outsidelinks) {
                if (field.equals(outsidelink.fieldname)) {
                    return outsidelink;
                }
            }
        }
        return null;
    }

    @Override
    public OutsideLink load(String linkid) {
        if (!Utils.isEmpty(linkid)) {
            for (Map.Entry<String, List<OutsideLink>> entry : cacheOutsideLinks.entrySet()) {
                List<OutsideLink> outsidelinks = entry.getValue();
                for (OutsideLink outsidelink : outsidelinks) {
                    if (linkid.equals(outsidelink.id)) {
                        return outsidelink;
                    }
                }
            }
            try {
                Content conn = XN_Content.load(linkid, "fieldmodulerels");
                OutsideLink outsideLink =  new OutsideLink(conn);
                String key = getKey(outsideLink.modulename);
                if(!cacheOutsideLinks.containsKey(key)){
                    cacheOutsideLinks.put(key,new ArrayList<>());
                }

                cacheOutsideLinks.get(key).add(outsideLink);
                return outsideLink;
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public List<OutsideLink> list(String modulename) {
        try {
            String key = getKey(modulename);
            if (!cacheOutsideLinks.containsKey(key)) {
                List<Object> query = XN_Query.contentQuery().tag("fieldmodulerels")
                        .filter("type", "eic", "fieldmodulerels")
                        .filter("my.modulename", "eic", modulename)
                        .notDelete().end(-1).execute();
                List<OutsideLink> outsideLinks = new ArrayList<>();
                for (Object item : query) {
                    outsideLinks.add(new OutsideLink(item));
                }
                cacheOutsideLinks.put(key,outsideLinks);
                query.clear();
            }
            if(cacheOutsideLinks.containsKey(key)) {
                return cacheOutsideLinks.get(key);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void update(OutsideLink outsideLink) throws Exception {
        if (Utils.isEmpty(outsideLink.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        this.clear(outsideLink.modulename, outsideLink.fieldname);
        if (Utils.isEmpty(outsideLink.id)) {
            Content conn = XN_Content.create("fieldmodulerels", "");
            outsideLink.toContent(conn);
            conn.save("fieldmodulerels");
            outsideLink.fromContent(conn);
        } else {
            Content conn = XN_Content.load(outsideLink.id, "fieldmodulerels");
            if(outsideLink.deleted == 0) {
                outsideLink.toContent(conn);
                conn.save("fieldmodulerels");
            } else {
                conn.delete("fieldmodulerels");
            }
        }
        if(outsideLink.deleted == 0) {
            String key = getKey(outsideLink.modulename);
            if (!cacheOutsideLinks.containsKey(key)) {
                cacheOutsideLinks.put(key, new ArrayList<>());
            }
            cacheOutsideLinks.get(key).add(outsideLink);
        }
    }

    @Override
    public List<OutsideLink> getRelMeLinkModules(String moduleName) {
        List<OutsideLink> outsideLinks = new ArrayList<>();
        try {
            List<Object> query = XN_Query.contentQuery().tag("fieldmodulerels")
                    .filter("type", "eic", "fieldmodulerels")
                    .filter("my.relmodule", "eic", moduleName)
                    .notDelete().end(-1).execute();
            for (Object item : query) {
                outsideLinks.add(new OutsideLink(item));
            }
        } catch (Exception ignored) {
        }
        return outsideLinks;
    }
}
