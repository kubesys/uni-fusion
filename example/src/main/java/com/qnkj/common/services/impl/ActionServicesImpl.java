package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.WebException;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.Action;
import com.qnkj.common.services.IActionServices;
import com.qnkj.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

@Slf4j
@Service
public class ActionServicesImpl implements IActionServices {
    private static final Map<String, List<Action>> cacheActions = new HashMap<>();
    private static final String TABLE_NAME_KEY = "customactions";

    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }

    private void updateCache(Action action) {
        String key = getKey(action.modulename);
        cacheActions.computeIfAbsent(key, k -> new ArrayList<>(1));
        cacheActions.get(key).add(action);
        cacheActions.get(key).sort((o1, o2) -> {
            int diff = o1.order - o2.order;
            return Integer.compare(diff, 0);
        });
    }

    @Override
    public void clear() {
        cacheActions.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        if (cacheActions.containsKey(key)) {
            cacheActions.remove(key);
        }
    }

    @Override
    public void clear(String modulename, String actionkey) {
        String key = getKey(modulename);
        if (cacheActions.containsKey(key)) {
            cacheActions.get(key).removeIf(action -> action.actionkey.equals(actionkey));
        }
    }

    @Override
    public Action get(String modulename, String actionkey) {
        String key = getKey(modulename);
        if (!cacheActions.containsKey(key)) {
            this.list(key, -1);
        }
        if (cacheActions.containsKey(key)) {
            List<Action> actions = cacheActions.get(key);
            for (Action action : actions) {
                if (actionkey.equals(action.actionkey)) {
                    return action;
                }
            }
        }
        return null;
    }

    @Override
    public Action load(String actionid) {
        if (!Utils.isEmpty(actionid)) {
            try {
                for (Map.Entry<String, List<Action>> entry : cacheActions.entrySet()) {
                    List<Action> actions = entry.getValue();
                    for (Action action : actions) {
                        if (actionid.equals(action.id)) {
                            return action;
                        }
                    }
                }
                Content conn = XN_Content.load(actionid, TABLE_NAME_KEY);
                Action action = new Action(conn);
                updateCache(action);
                return action;
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }
        return null;
    }

    @Override
    public List<Action> list(String modulename, int moduletype) {
        try {
            String key = getKey(modulename);
            if (!cacheActions.containsKey(key)||!(cacheActions.get(key)).isEmpty()) {
                List<Action> actions = new ArrayList<>();
                List<Object> query = XN_Query.contentQuery().tag(TABLE_NAME_KEY)
                        .filter("type", "eic", TABLE_NAME_KEY)
                        .filter("my.modulename", "eic", modulename)
                        .order("my.order", "A_N").notDelete()
                        .end(-1).execute();
                for (Object item : query) {
                    actions.add(new Action(item));
                }
                cacheActions.put(key, actions);
            }
            if (cacheActions.containsKey(key)) {
                return cacheActions.get(key);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>(1);
    }

    @Override
    public void update(Action action) throws Exception {
        if (Utils.isEmpty(action.modulename)) {
            throw new WebException("模块名不能为空！");
        }
        this.clear(action.modulename, action.actionkey);
        if (!Utils.isEmpty(action.id)) {
            Content conn = XN_Content.load(action.id, TABLE_NAME_KEY);
            if(action.deleted == 0) {
                action.toContent(conn);
                conn.save(TABLE_NAME_KEY);
            } else {
                conn.delete(TABLE_NAME_KEY);
            }
        } else {
            Content conn = XN_Content.create(TABLE_NAME_KEY, "");
            action.toContent(conn);
            conn.save(TABLE_NAME_KEY);
            action.fromContent(conn);
        }
        if(action.deleted == 0) {
            updateCache(action);
        }
    }
}
