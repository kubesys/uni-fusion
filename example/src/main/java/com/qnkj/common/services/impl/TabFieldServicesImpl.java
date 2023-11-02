package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.TabField;
import com.qnkj.common.services.ITabFieldServices;
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
public class TabFieldServicesImpl implements ITabFieldServices {

    private static Map<String,Map<String, List<TabField>>> cacheFields = new HashMap<>();

    private void updateCache(TabField tabField) {
        String application = BaseSaasConfig.getApplication();
        if (cacheFields.containsKey(application)) {
            Map<String, List<TabField>> fields = cacheFields.get(application);
            if (!fields.containsKey(tabField.modulename)) {
                fields.put(tabField.modulename, new ArrayList<>());
            }
            fields.get(tabField.modulename).add(tabField);
            fields.get(tabField.modulename).sort((o1, o2) -> {
                int diff = o1.sequence - o2.sequence;
                return Integer.compare(diff, 0);
            });
        }

    }

    @Override
    public void clear() {
        cacheFields.clear();
    }

    @Override
    public void clear(String modulename) {
        String application = BaseSaasConfig.getApplication();
        if (cacheFields.containsKey(application)) {
            cacheFields.get(application).remove(modulename);
        }
    }

    @Override
    public void clear(String modulename, TabField field) {
        String application = BaseSaasConfig.getApplication();
        if (cacheFields.containsKey(application)) {
            Map<String, List<TabField>> fields = cacheFields.get(application);
            if (fields.containsKey(modulename)) {
                fields.get(modulename).removeIf(tabField -> tabField.id.equals(field.id));
            }
        }
    }

    @Override
    public TabField get(String modulename, String fieldname) {
        String application = BaseSaasConfig.getApplication();
        if (!cacheFields.containsKey(application)) {
            list(modulename);
        }
        Map<String, List<TabField>> allfields = cacheFields.get(application);
        if (!allfields.containsKey(modulename)) {
            list(modulename);
        }
        if (allfields.containsKey(modulename)) {
            List<TabField> fields = allfields.get(modulename);
            for (TabField field : fields) {
                if (field.fieldname.equals(fieldname)) {
                    return field;
                }
            }
        }
        return null;
    }

    @Override
    public TabField load(String fieldid) {
        try {
            for (Map.Entry<String, Map<String, List<TabField>>> entry : cacheFields.entrySet()) {
                for (Map.Entry<String, List<TabField>> item : entry.getValue().entrySet()) {
                    List<TabField> fields = item.getValue();
                    for (TabField field : fields) {
                        if (field.id.equals(fieldid)) {
                            return field;
                        }
                    }
                }
            }
            Content conn = XN_Content.load(fieldid, "fields");
            TabField tabField = new TabField(conn);
            updateCache(tabField);
            return tabField;
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public List<TabField> list(String modulename) {
        try {
            String application = BaseSaasConfig.getApplication();
            Map<String, List<TabField>> allfields;
            if (cacheFields.containsKey(application)) {
                allfields = cacheFields.get(application);
            } else {
                allfields = new HashMap<>();
            }
            if (!allfields.containsKey(modulename) || allfields.get(modulename).isEmpty()) {
                List<Object> query = XN_Query.contentQuery().tag("fields")
                        .filter("type", "eic", "fields")
                        .filter("my.modulename", "eic", modulename)
                        .order("my.sequence", "A_N").notDelete()
                        .end(-1).execute();
                if (!query.isEmpty()) {
                    List<TabField> result = new ArrayList<>();
                    for (Object item : query) {
                        result.add(new TabField(item));
                    }
                    query.clear();
                    allfields.put(modulename, result);
                }
            }
            cacheFields.put(application,allfields);
            if (allfields.containsKey(modulename)) {
                return allfields.get(modulename);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public List<TabField> list(String modulename, String blockid) {
        try {
            list(modulename);
            String application = BaseSaasConfig.getApplication();
            Map<String, List<TabField>> allfields;
            if (cacheFields.containsKey(application)) {
                allfields = cacheFields.get(application);
                if (allfields.containsKey(modulename)) {
                    List<TabField> fields = allfields.get(modulename);
                    List<TabField> result = new ArrayList<>();
                    for (TabField item : fields) {
                        if (blockid.equals(item.block.toString())) {
                            result.add(item);
                        }
                    }
                    result.sort((o1, o2) -> {
                        int diff = o1.sequence - o2.sequence;
                        return Integer.compare(diff, 0);
                    });
                    return result;
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void update(TabField tabField) throws Exception {
        if (Utils.isEmpty(tabField.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        this.clear(tabField.modulename, tabField);
        if (Utils.isEmpty(tabField.id)) {
            Content conn = XN_Content.create("fields", "");
            tabField.toContent(conn);
            conn.save("fields");
            tabField.fromContent(conn);
        } else {
            Content conn = XN_Content.load(tabField.id, "fields");
            if(tabField.deleted == 0) {
                tabField.toContent(conn);
                conn.save("fields");
            } else {
                conn.delete("fields");
            }
        }
        if(tabField.deleted == 0) {
            updateCache(tabField);
        }
    }

    @Override
    public void update(List<TabField> tabFields) throws Exception {
        for (TabField tabField : tabFields) {
            if (Utils.isEmpty(tabField.modulename)) {
                throw new Exception("模块名不能为空！");
            }
        }
        this.clear();
        List<Object> create = new ArrayList<>();
        List<Object> update = new ArrayList<>();
        for (TabField tabField : tabFields) {
            if (Utils.isEmpty(tabField.id)) {
                Content conn = XN_Content.create("fields", "");
                tabField.toContent(conn);
                create.add(conn);
            } else {
                Content conn = XN_Content.load(tabField.id, "fields");
                tabField.toContent(conn);
                update.add(conn);
            }
        }
        if (!create.isEmpty()) {
            XN_Content.batchsave(create, "fields");
        }
        if (!update.isEmpty()) {
            XN_Content.batchsave(update, "fields");
        }
    }
}
