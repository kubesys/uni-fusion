package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.ParentTab;
import com.qnkj.common.entitys.Program;
import com.qnkj.common.services.IParentTabServices;
import com.qnkj.common.services.IProgramServices;
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
public class ParentTabServicesImpl implements IParentTabServices {

    private static Map<String, Map<String, List<ParentTab>>> cacheParentTabs = new HashMap<>();

    private final IProgramServices programServices;

    public ParentTabServicesImpl(IProgramServices programServices) {
        this.programServices = programServices;
    }

    @Override
    public void clear() {
        cacheParentTabs.clear();
    }

    @Override
    public void clear(String program, String parentname) {
        String application = BaseSaasConfig.getApplication();
        if (cacheParentTabs.containsKey(application)) {
            Map<String, List<ParentTab>> localParentTabs = cacheParentTabs.get(application);
            if (localParentTabs.containsKey(program)) {
                List<ParentTab> parentTabs = localParentTabs.get(program);
                parentTabs.removeIf(parentTab -> parentTab.name.equals(parentname));
            }
        }


    }

    private Boolean isExist(String program, String name) {
        try {
            for (Map.Entry<String, Map<String, List<ParentTab>>> entry : cacheParentTabs.entrySet()) {
                for (ParentTab parentTab : entry.getValue().get(program)) {
                    if (parentTab.name.equals(name)) {
                        return true;
                    }
                }
            }
            List<Object> query = XN_Query.contentQuery().tag("parenttabs")
                    .filter("type", "eic", "parenttabs")
                    .filter("my.name", "=", name).notDelete()
                    .filter("my.program", "=", program)
                    .end(1).execute();
            if (!query.isEmpty()) {
                ParentTab parentTab = new ParentTab(query.get(0));
                updateCache(parentTab);
                query.clear();
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private void updateCache(ParentTab parentTab) {
        String application = BaseSaasConfig.getApplication();
        if (cacheParentTabs.containsKey(application)) {
            Map<String, List<ParentTab>> localParentTabs = cacheParentTabs.get(application);
            if (!localParentTabs.containsKey(parentTab.program)) {
                localParentTabs.put(parentTab.program, new ArrayList<>());
            }
            localParentTabs.get(parentTab.program).add(parentTab);
            localParentTabs.get(parentTab.program).sort((o1, o2) -> {
                int diff = o1.order - o2.order;
                return Integer.compare(diff, 0);
            });
        }

    }

    @Override
    public ParentTab get(String program, String name) {
        try {
            init();
            for (Map.Entry<String, Map<String, List<ParentTab>>> entry : cacheParentTabs.entrySet()) {
                if (entry.getValue().containsKey(program)) {
                    for (ParentTab parentTab : entry.getValue().get(program)) {
                        if (parentTab.name.equals(name)) {
                            return parentTab;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public ParentTab load(String tabid) {
        try {
            for (Map.Entry<String, Map<String, List<ParentTab>>> entry : cacheParentTabs.entrySet()) {
                for (String program : entry.getValue().keySet()) {
                    for (ParentTab parentTab : entry.getValue().get(program)) {
                        if (parentTab.id.equals(tabid)) {
                            return parentTab;
                        }
                    }
                }
            }
            Content conn = XN_Content.load(tabid, "parenttabs");
            ParentTab parentTab = new ParentTab(conn);
            updateCache(parentTab);
            return parentTab;
        } catch (Exception ignored) {
        }
        return null;
    }

    private void init() {
        try {
            String application = BaseSaasConfig.getApplication();
            if (!cacheParentTabs.containsKey(application)) {
                Map<String, List<ParentTab>> localParentTabs = new HashMap<>();
                List<Object> query = XN_Query.contentQuery().tag("parenttabs")
                        .filter("type", "eic", "parenttabs")
                        .order("my.order", "A_N").notDelete()
                        .end(-1).execute();
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        Content content = (Content) item;
                        if (Utils.isEmpty(content.my.get("modulename"))) {
                            String program = content.my.get("program").toString();
                            if (localParentTabs.containsKey(program)) {
                                List<ParentTab> result = localParentTabs.get(program);
                                result.add(new ParentTab(item));
                                localParentTabs.put(program, result);
                            } else {
                                List<ParentTab> result = new ArrayList<>();
                                result.add(new ParentTab(item));
                                localParentTabs.put(program, result);
                            }
                        }
                    }
                }
                cacheParentTabs.put(application,localParentTabs);
            }
        } catch (Exception ignored) { }
    }

    @Override
    public List<ParentTab> list(String program) {
        try {
            init();
            String application = BaseSaasConfig.getApplication();
            if (cacheParentTabs.containsKey(application)) {
                if (cacheParentTabs.get(application).containsKey(program)) {
                    return cacheParentTabs.get(application).get(program);
                }
            }

        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void update(ParentTab parentTab) throws Exception {
        if (Utils.isEmpty(parentTab.id)) {
            if ((Utils.isEmpty(parentTab.program) && Utils.isEmpty(parentTab.programid)) || Utils.isEmpty(parentTab.name)) {
                throw new Exception("菜单组名称不能为空！");
            }
            if(!Utils.isEmpty(this.programServices.get(parentTab.name))) {
                throw new Exception("模块组名已存在！");
            }
            if (Utils.isEmpty(parentTab.programid)) {
                Program program = this.programServices.get(parentTab.program);
                if (Utils.isEmpty(program)) {
                    throw new Exception("菜单组不存在！");
                }
                parentTab.programid = program.id;
            } else if (Utils.isEmpty(parentTab.program)) {
                Program program = this.programServices.load(parentTab.programid);
                if (Utils.isEmpty(program)) {
                    throw new Exception("菜单组不存在！");
                }
                parentTab.program(program.group);
            }
            if (!this.isExist(parentTab.program, parentTab.name)) {
                this.clear(parentTab.program, parentTab.name);
                Content conn = XN_Content.create("parenttabs", "");
                parentTab.toContent(conn);
                conn.save("parenttabs");
                parentTab.fromContent(conn);
            } else {
                System.out.println(parentTab.name + "  模块组名已经存在");
                throw new Exception("模块组名已经存在");
            }
        } else {
            this.clear(parentTab.program, parentTab.name);
            Content conn = XN_Content.load(parentTab.id, "parenttabs");
            if(parentTab.deleted == 0) {
                if (Utils.isEmpty(parentTab.programid)) {
                    parentTab.programid = conn.my.get("programid").toString();
                    parentTab.program = conn.my.get("program").toString();
                }
                parentTab.toContent(conn);
                conn.save("parenttabs");
            } else {
                conn.delete("parenttabs");
            }
        }
        if(parentTab.deleted == 0) {
            updateCache(parentTab);
        }
    }

    @Override
    public void updateSubtab(ParentTab parentTab) throws Exception {
        if (Utils.isEmpty(parentTab.id)) {
            if (Utils.isEmpty(parentTab.programid)) {
                throw new Exception("父节点不能为空");
            }
            if (Utils.isEmpty(parentTab.name)) {
                throw new Exception("模块组名不能为空");
            }
            ParentTab parent = this.load(parentTab.programid);
            if (!this.isExist(parent.name, parentTab.name)) {
                this.clear(parent.name, parentTab.name);
                parentTab.program(parent.name);
                Content conn = XN_Content.create("parenttabs", "");
                parentTab.toContent(conn);
                conn.save("parenttabs");
                parentTab.fromContent(conn);
            } else {
                throw new Exception("模块组名已经存在");
            }
        } else {
            this.clear(parentTab.program, parentTab.name);
            Content conn = XN_Content.load(parentTab.id, "parenttabs");
            if(parentTab.deleted == 0) {
                parentTab.toContent(conn);
                conn.save("parenttabs");
            } else {
                conn.delete("parenttabs");
            }
        }
        if(parentTab.deleted == 0) {
            updateCache(parentTab);
        }
    }
}
