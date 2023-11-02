package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.ParentTab;
import com.qnkj.common.services.IModuleMenuServices;
import com.qnkj.common.services.IParentTabServices;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/6
 */

@Service
public class ModuleMenuServicesImpl implements IModuleMenuServices {
    private final IParentTabServices parentTabServices;

    private static Map<String,HashMap<String, HashMap<String, List<ModuleMenu>>>> cacheModuleMenus = new HashMap<>();

    public ModuleMenuServicesImpl(IParentTabServices parentTabServices) {
        this.parentTabServices = parentTabServices;
    }

    private void updateCache(ModuleMenu moduleMenu) {
        String application = BaseSaasConfig.getApplication();
        if (cacheModuleMenus.containsKey(application)) {
            HashMap<String, HashMap<String, List<ModuleMenu>>> moduleMenus = cacheModuleMenus.get(application);
            if (!moduleMenus.containsKey(moduleMenu.program)) {
                moduleMenus.put(moduleMenu.program, new HashMap<>());
            }
            if (!moduleMenus.get(moduleMenu.program).containsKey(moduleMenu.parent)) {
                moduleMenus.get(moduleMenu.program).put(moduleMenu.parent, new ArrayList<>());
            }
            moduleMenus.get(moduleMenu.program).get(moduleMenu.parent).add(moduleMenu);
            moduleMenus.get(moduleMenu.program).get(moduleMenu.parent).sort((o1, o2) -> {
                int diff = o1.order - o2.order;
                return Integer.compare(diff, 0);
            });
        }

    }

    @Override
    public void clear() {
        cacheModuleMenus.clear();
    }

    @Override
    public void clear(String modulename) {
        String application = BaseSaasConfig.getApplication();
        for (Map.Entry<String,HashMap<String, HashMap<String, List<ModuleMenu>>>> entry : cacheModuleMenus.entrySet()) {
            if (application.compareTo(entry.getKey()) == 0) {
                for (String program : entry.getValue().keySet()) {
                    for (String parent : entry.getValue().get(program).keySet()) {
                        entry.getValue().get(program).get(parent).removeIf(moduleMenu -> moduleMenu.modulename.equals(modulename));
                    }
                }
            }
        }

    }

    @Override
    public void clear(String program, String parent, String modulename) {
        String application = BaseSaasConfig.getApplication();
        if (cacheModuleMenus.containsKey(application)) {
            HashMap<String, HashMap<String, List<ModuleMenu>>> localmoduleMenus = cacheModuleMenus.get(application);
            if (localmoduleMenus.containsKey(program) && localmoduleMenus.get(program).containsKey(parent)) {
                List<ModuleMenu> moduleMenus = localmoduleMenus.get(program).get(parent);
                moduleMenus.removeIf(moduleMenu -> moduleMenu.modulename.equals(modulename));
            }
        }

    }

    @Override
    public ModuleMenu get(String modulename) {
        try {
            init();
            String application = BaseSaasConfig.getApplication();
            for (Map.Entry<String,HashMap<String, HashMap<String, List<ModuleMenu>>>> entry : cacheModuleMenus.entrySet()) {
                if (application.compareTo(entry.getKey()) == 0) {
                    for (String program : entry.getValue().keySet()) {
                        for (String parent : entry.getValue().get(program).keySet()) {
                            for (ModuleMenu moduleMenu : entry.getValue().get(program).get(parent)) {
                                if (moduleMenu.modulename.equals(modulename)) {
                                    return moduleMenu;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }


    @Override
    public ModuleMenu get(String program, String parent, String label) {
        try {
            init();
            String application = BaseSaasConfig.getApplication();
            if (!cacheModuleMenus.containsKey(application)) {
                this.list(program, parent);
            }
            HashMap<String, HashMap<String, List<ModuleMenu>>> localmoduleMenus = cacheModuleMenus.get(application);
            if (!localmoduleMenus.containsKey(program) || !localmoduleMenus.get(program).containsKey(parent)) {
                this.list(program, parent);
            }
            List<ModuleMenu> moduleMenus = localmoduleMenus.get(program).get(parent);
            for (ModuleMenu moduleMenu : moduleMenus) {
                if (moduleMenu.label.equals(label)) {
                    return moduleMenu;
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public ModuleMenu load(String menuid) {
        try {
            init();
            String application = BaseSaasConfig.getApplication();
            for (Map.Entry<String,HashMap<String, HashMap<String, List<ModuleMenu>>>> entry : cacheModuleMenus.entrySet()) {
                if (application.compareTo(entry.getKey()) == 0) {
                    for (String program : entry.getValue().keySet()) {
                        for (String parent : entry.getValue().get(program).keySet()) {
                            List<ModuleMenu> moduleMenus = entry.getValue().get(program).get(parent);
                            for (ModuleMenu moduleMenu : moduleMenus) {
                                if (moduleMenu.id.equals(menuid)) {
                                    return moduleMenu;
                                }
                            }
                        }
                    }
                }
            }
            Content conn = XN_Content.load(menuid, "parenttabs");
            ModuleMenu moduleMenu = new ModuleMenu(conn);
            updateCache(moduleMenu);
            return moduleMenu;
        } catch (Exception ignored) {
        }
        return null;
    }

    private void init() {
        try {
            String application = BaseSaasConfig.getApplication();
            if (!cacheModuleMenus.containsKey(application)) {
                HashMap<String, HashMap<String, List<ModuleMenu>>> localmoduleMenus = new HashMap<>();
                List<Object> query = XN_Query.contentQuery().tag("parenttabs")
                        .filter("type", "eic", "parenttabs")
                        .filter("my.program","!=","")
                        .filter("my.parent","!=","")
                        .order("my.order", "A_N").notDelete()
                        .end(-1).execute();
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        Content content = (Content) item;
                        if (!Utils.isEmpty(content.my.get("program")) && !Utils.isEmpty(content.my.get("parent"))) {
                            String program = content.my.get("program").toString();
                            String parent = content.my.get("parent").toString();
                            if (localmoduleMenus.containsKey(program)) {
                                HashMap<String, List<ModuleMenu>> parents = localmoduleMenus.get(program);
                                if (parents.containsKey(parent)) {
                                    List<ModuleMenu> result = parents.get(parent);
                                    result.add(new ModuleMenu(item));
                                    parents.put(parent, result);
                                    localmoduleMenus.put(program, parents);
                                } else {
                                    List<ModuleMenu> result = new ArrayList<>();
                                    result.add(new ModuleMenu(item));
                                    parents.put(parent, result);
                                    localmoduleMenus.put(program, parents);
                                }
                            } else {
                                HashMap<String, List<ModuleMenu>> parents = new HashMap<>();
                                List<ModuleMenu> result = new ArrayList<>();
                                result.add(new ModuleMenu(item));
                                parents.put(parent, result);
                                localmoduleMenus.put(program, parents);
                            }
                        }
                    }
                }
                cacheModuleMenus.put(application,localmoduleMenus);
            }
        } catch (Exception ignored) { }
    }

    @Override
    public List<ModuleMenu> list(String program, String parent) {
        try {
            init();
            String application = BaseSaasConfig.getApplication();
            if (cacheModuleMenus.containsKey(application)) {
                HashMap<String, HashMap<String, List<ModuleMenu>>> localmoduleMenus = cacheModuleMenus.get(application);
                if (localmoduleMenus.containsKey(program) && localmoduleMenus.get(program).containsKey(parent)) {
                    return localmoduleMenus.get(program).get(parent);
                }
            }
        } catch (Exception ignored) { }
        return null;
    }

    @Override
    public Boolean isModulenameExist(String modulename) {
        try {
            init();
            String application = BaseSaasConfig.getApplication();
            for (Map.Entry<String,HashMap<String, HashMap<String, List<ModuleMenu>>>> entry : cacheModuleMenus.entrySet()) {
                if (application.compareTo(entry.getKey()) == 0) {
                    for (String program : entry.getValue().keySet()) {
                        for (String parent : entry.getValue().get(program).keySet()) {
                            for (ModuleMenu moduleMenu : entry.getValue().get(program).get(parent)) {
                                if (moduleMenu.modulename.equalsIgnoreCase(modulename)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { }
        return false;
    }
    @Override
    public void update(ModuleMenu moduleMenu) throws Exception {
        if (Utils.isEmpty(moduleMenu.id)) {
            if (Utils.isEmpty(moduleMenu.program)) {
                throw new Exception("应用组名称不能为空！");
            }
            if (Utils.isEmpty(moduleMenu.parent)) {
                throw new Exception("菜单组名称不能为空！");
            }
            if (isModulenameExist(moduleMenu.modulename)) {
                throw new Exception("模块名称已经存在，请更换后再试！");
            }
            if (Utils.isEmpty(moduleMenu.parentid)) {
                ParentTab parentTab = parentTabServices.get(moduleMenu.program, moduleMenu.parent);
                if (Utils.isEmpty(parentTab)) {
                    throw new Exception("菜单组不存在！");
                }
                moduleMenu.parentid = parentTab.id;
            }
            this.clear(moduleMenu.program, moduleMenu.parent, moduleMenu.modulename);
            Content conn = XN_Content.create("parenttabs", "");
            moduleMenu.toContent(conn);
            conn.save("parenttabs");
            moduleMenu.fromContent(conn);
        } else {
            this.clear(moduleMenu.program, moduleMenu.parent, moduleMenu.modulename);
            Content conn = XN_Content.load(moduleMenu.id, "parenttabs");
            if(moduleMenu.deleted == 0) {
                if (Utils.isEmpty(moduleMenu.parentid)) {
                    moduleMenu.parentid = conn.my.get("parentid").toString();
                }
                if (Utils.isEmpty(moduleMenu.parent)) {
                    moduleMenu.parent(conn.my.get("parent").toString());
                }
                if (Utils.isEmpty(moduleMenu.program)) {
                    moduleMenu.program(conn.my.get("program").toString());
                }
                moduleMenu.toContent(conn);
                conn.save("parenttabs");
            } else {
                conn.delete("parenttabs");
            }
        }
        if(moduleMenu.deleted == 0) {
            String application = BaseSaasConfig.getApplication();
            if (cacheModuleMenus.containsKey(application)) {
                HashMap<String, HashMap<String, List<ModuleMenu>>> localmoduleMenus = cacheModuleMenus.get(application);
                if (!Utils.isEmpty(moduleMenu.program) && !Utils.isEmpty(moduleMenu.parent)) {
                    if (localmoduleMenus.containsKey(moduleMenu.program)) {
                        HashMap<String, List<ModuleMenu>> parents = localmoduleMenus.get(moduleMenu.program);
                        if (parents.containsKey(moduleMenu.parent)) {
                            List<ModuleMenu> result = parents.get(moduleMenu.parent);
                            result.add(moduleMenu);
                            parents.put(moduleMenu.parent, result);
                            localmoduleMenus.put(moduleMenu.program, parents);
                        } else {
                            List<ModuleMenu> result = new ArrayList<>();
                            result.add(moduleMenu);
                            parents.put(moduleMenu.parent, result);
                            localmoduleMenus.put(moduleMenu.program, parents);
                        }
                    } else {
                        HashMap<String, List<ModuleMenu>> parents = new HashMap<>();
                        List<ModuleMenu> result = new ArrayList<>();
                        result.add(moduleMenu);
                        parents.put(moduleMenu.parent, result);
                        localmoduleMenus.put(moduleMenu.program, parents);
                    }
                }

                if (localmoduleMenus.containsKey(moduleMenu.program) && localmoduleMenus.get(moduleMenu.program).containsKey(moduleMenu.parent)) {
                    localmoduleMenus.get(moduleMenu.program).get(moduleMenu.parent).sort((o1, o2) -> {
                        int diff = o1.order - o2.order;
                        return Integer.compare(diff, 0);
                    });
                }
            }
        }
    }
}
