package com.qnkj.core.base.services.impl;


import com.alibaba.fastjson.JSONArray;
import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseMenuConfig;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.ParentTab;
import com.qnkj.common.entitys.Program;
import com.qnkj.common.entitys.Tab;
import com.qnkj.common.services.IModuleMenuServices;
import com.qnkj.common.services.IParentTabServices;
import com.qnkj.common.services.IProgramServices;
import com.qnkj.common.services.ITabServices;
import com.qnkj.common.utils.CallbackUtils;
import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.entitys.MenuTree;
import com.qnkj.core.base.entitys.TreeUtil;
import com.qnkj.core.base.services.IMenuService;
import com.qnkj.core.utils.AuthorizeUtils;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.RolesUtils;
import com.qnkj.core.webconfigs.configure.WebConstant;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Oldhand
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {
    private final IProgramServices programServices;
    private final IParentTabServices parentTabServices;
    private final IModuleMenuServices moduleMenuServices;
    private final ITabServices tabServices;

    public MenuServiceImpl(
                           IProgramServices programServices,
                           IParentTabServices parentTabServices,
                           IModuleMenuServices moduleMenuServices,
                           ITabServices tabServices) {
        this.programServices = programServices;
        this.parentTabServices = parentTabServices;
        this.moduleMenuServices = moduleMenuServices;
        this.tabServices = tabServices;
    }

    @Override
    public List<Map<String, Object>> findMenuGroups(Boolean isdev) throws Exception {
        if (!Utils.isEmpty(RedisUtils.get(WebConstant.INIT_STATUS))) {
            throw new Exception("正在初始化系统，请稍候。。。");
        }
        List<Map<String, Object>> lists = new ArrayList<>();
        List<Program> result = programServices.list();
        if(!Utils.isEmpty(result)) {
            BaseMenuConfig.clear();
            for (Program item : result) {
                if(!isdev && "lcdp".equals(item.group)) {
                    continue;
                }
                if((ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) && !("general".equals(item.authorize) || "system".equals(item.authorize))) {
                    continue;
                }
                if((ProfileUtils.isSupplier() || ProfileUtils.isSupplierAssistant()) && !("general".equals(item.authorize) || "supplier".equals(item.authorize))) {
                    continue;
                }
                if(ProfileUtils.isManager() && !(item.authorize.contains("general") || ("system".equals(item.authorize) && !"settings".equals(item.group)))) {
                    continue;
                }
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("name", item.group);
                infoMap.put("icon", item.icon);
                infoMap.put("desc", item.label);
                infoMap.put("mainpage", item.mainpage);
                infoMap.put("builtin", String.valueOf(item.builtin));
                lists.add(infoMap);
                updateUserMenus(item);
            }
        }
        return lists;
    }

    @Override
    public List<Map<String, Object>> findMenuGroups(String profileid,String usertype,Boolean isdev) throws Exception {
        if (!Utils.isEmpty(RedisUtils.get(WebConstant.INIT_STATUS))) {
            throw new Exception("正在初始化系统，请稍候。。。");
        }
        List<Map<String, Object>> lists = new ArrayList<>();
        try {
            lists = findMenuGroups(isdev);
            if(!Utils.isEmpty(lists)) {
                List<Map<String, Object>> tmp = new ArrayList<>();
                //清除没有模块的主菜单项
                for(Map<String, Object> item: lists){
                    if("report".equals(((HashMap<?,?>)item).get("name"))){
                        MenuTree<Object> reportMenu = TreeUtil.buildMenuTree(getReportMenu());
                        if(!reportMenu.getChilds().isEmpty()) {
                            tmp.add(item);
                        }
                        continue;
                    }
                    if("supplier".equals(((HashMap<?,?>)item).get("name"))){
                        if(ProfileUtils.isSupplier() && (ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant())){
                            tmp.add(item);
                        }
                    } else {
                        List<Object> roleMenus = getRoleMenu(profileid,usertype,((HashMap<?,?>)item).get("name").toString());
                        if(!roleMenus.isEmpty()){
                            for(Object rolemenu: roleMenus){
                                if(rolemenu instanceof ModuleMenu){
                                    tmp.add(item);
                                    break;
                                }
                            }
                        }
                    }
                }
                lists.clear();
                lists.addAll(tmp);
            }
        }catch (Exception ignored) {}
        return lists;
    }

    private void updateUserMenus(Object menu){
        if(menu instanceof Program){
            BaseMenuConfig.set(menu);
            List<ParentTab> result = parentTabServices.list(((Program)menu).group);
            if(Utils.isEmpty(result)) {
                return;
            }
            for(ParentTab item: result){
                BaseMenuConfig.set(item);
                updateUserMenus(item);
            }
        }else if(menu instanceof ParentTab){
            List<ParentTab> parentTabs = parentTabServices.list(((ParentTab)menu).name);
            if(!Utils.isEmpty(parentTabs)) {
                for (ParentTab item : parentTabs) {
                    BaseMenuConfig.set(item);
                    updateUserMenus(item);
                }
            }
            List<ModuleMenu> result = moduleMenuServices.list(((ParentTab) menu).program,((ParentTab) menu).name);
            if(Utils.isEmpty(result)) {
                return;
            }
            for (ModuleMenu item : result){
                BaseMenuConfig.set(item);
            }
        }
    }

    private List<Object> getRoleMenu(String profileid,String usertype, String menuType) {
        List<Object> roleGroupMenus = new ArrayList<>();
        try {
            if(Utils.isEmpty(menuType)) {
                menuType = usertype;
            }
            List<Object> groupMenus;
            try{
                groupMenus = BaseMenuConfig.get(menuType);
            }catch (Exception e){
                throw new WebException("您无权获取系统设置菜单："+menuType);
            }
            if(Utils.isEmpty(groupMenus)){
                throw new WebException("您无权获取系统设置菜单");
            }
            if(ContextUtils.isJar()){
                groupMenus.removeIf(item -> item instanceof ParentTab && "developmentmanager".equals(((ParentTab) item).name));
                groupMenus.removeIf(item -> item instanceof ModuleMenu && "developmentmanager".equals(((ModuleMenu) item).parent));
            }
            if (!"admin".equals(usertype)) {
                if(ProfileUtils.isSupplier() && (ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant())){
                    roleGroupMenus = groupMenus;
                } else {
                    List<Object> roledata;
                    if ("pt".equals(usertype) || "supplier".equals(usertype)) {
                        roledata = RolesUtils.getRoleByProfile(profileid);
                    } else {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("username", profileid);
                        params.put("usertype", usertype);
                        params.put("menutype", menuType);
                        try {
                            Object result = CallbackUtils.invoke("menuCallback", params);
                            if (Utils.isEmpty(result)) {
                                throw new Exception("获取菜单配置回调失败！");
                            }
                            roledata = RolesUtils.getRoleByName(result.toString());
                        } catch (Exception e) {
                            log.error("MenuCallback : {}",e.getMessage());
                            roledata = RolesUtils.getRoleByName("未审核企业权限");
                        }
                    }
                    for (Object item : groupMenus) {
                        if (item instanceof ParentTab) {
                            roleGroupMenus.add(item);
                        } else if (item instanceof ModuleMenu) {
                            Map<String, Object> rolemodules = Utils.getListByMap(roledata,((ModuleMenu) item).program);
                            if (!Utils.isEmpty(rolemodules)) {
                                Tab tab = tabServices.get(((ModuleMenu) item).modulename);
                                if(tab.datarole == 2 && !tab.dataauthorize.isEmpty() && AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(),tab.dataauthorize)){
                                    roleGroupMenus.add(item);
                                }else {
                                    String modulename = ((ModuleMenu) item).modulename;
                                    if(modulename.startsWith("MultipleEntry:")) {
                                        modulename = modulename.substring(14);
                                    }
                                    HashMap<String, Object> roleinfo = (HashMap<String,Object>)((HashMap<?,?>) rolemodules.get(((ModuleMenu) item).program)).get(modulename);
                                    if (!Utils.isEmpty(roleinfo) && Boolean.parseBoolean(roleinfo.getOrDefault("isview", false).toString())) {
                                        if(tab.datarole == 2 && !tab.dataauthorize.isEmpty() && !AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(),tab.dataauthorize)) {
                                            continue;
                                        }
                                        roleGroupMenus.add(item);
                                    }
                                }
                            }

                        }
                    }
                }
            } else {
                roleGroupMenus = groupMenus;
            }
            List<Object> filterGroupMenus = new ArrayList<>();
            for (Object item : roleGroupMenus) {
                if (item instanceof ParentTab) {
                    filterGroupMenus.add(item);
                } else if (item instanceof ModuleMenu) {
                    Tab tab = tabServices.get(((ModuleMenu) item).modulename);
                    if (tab.ishidden) {
                        continue;
                    }
                    if (null==((ModuleMenu)item).icon||"".equals(((ModuleMenu)item).icon)) {
                        ((ModuleMenu)item).icon = "lcdp-icon-baobiao";
                    }
                    filterGroupMenus.add(item);
                }
            }
            return filterGroupMenus;
        }catch (Exception e){
            log.error("getRoleMenu : {}",e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * 获取报表菜单
     * @return list
     */
    private List<MenuTree<Object>> getReportMenu() {
        List<MenuTree<Object>> trees = new ArrayList<>();
        try {
            List<Object> query = XN_Query.contentQuery().tag("reportsettingscategorys")
                    .filter("type","eic","reportsettingscategorys")
                    .notDelete()
                    .filter("my.categorys","=","TopN报表")
                    .end(1).execute();
            if(query.size() <= 0){
                Content content = XN_Content.create("reportsettingscategorys","");
                content.add("deleted",0)
                        .add("categorys","TopN报表")
                        .add("sequence","2")
                        .add("system","1")
                        .save("reportsettingscategorys");
            }

            query = XN_Query.contentQuery().tag("reportsettingscategorys")
                    .filter("type","eic","reportsettingscategorys")
                    .notDelete()
                    .filter("my.categorys","=","环比报表")
                    .end(1).execute();
            if(query.size() <= 0){
                Content content = XN_Content.create("reportsettingscategorys","");
                content.add("deleted",0)
                        .add("categorys","环比报表")
                        .add("sequence","3")
                        .add("system","1")
                        .save("reportsettingscategorys");
            }

            query = XN_Query.contentQuery().tag("reportsettingscategorys")
                    .filter("type","eic","reportsettingscategorys")
                    .notDelete()
                    .filter("my.categorys","=","同比报表")
                    .end(1).execute();
            if(query.isEmpty()){
                Content content = XN_Content.create("reportsettingscategorys","");
                content.add("deleted",0)
                        .add("categorys","同比报表")
                        .add("sequence","4")
                        .add("system","1")
                        .save("reportsettingscategorys");
            }

            query.clear();
            int page = 1;
            do {
                query = XN_Query.contentQuery().tag("reportsettingscategorys")
                        .filter("type", "eic", "reportsettingscategorys")
                        .order("my.sequence","A_N")
                        .notDelete().begin((page-1) * 100).end(page * 100).execute();
                for(Object item: query) {
                    String categorys = ((Content)item).my.getOrDefault("categorys","").toString();
                    MenuTree<Object> tree = new MenuTree<>();
                    tree.setId(((Content)item).id);
                    tree.setParentId("");
                    tree.setTitle(categorys);
                    if("综合报表".equals(categorys)){
                        tree.setIcon("lcdp-icon-baobiao");
                    }else if("TopN报表".equals(categorys)) {
                        tree.setIcon("lcdp-icon-TopN");
                    }else if("环比报表".equals(categorys)) {
                        tree.setIcon("lcdp-icon-huanbi");
                    }else if("同比报表".equals(categorys)) {
                        tree.setIcon("lcdp-icon-tongbi");
                    } else {
                        tree.setIcon("lcdp-icon-baobiao");
                    }
                    tree.setHasChild(true);
                    trees.add(tree);
                }
                page++;
            }while (query.size() == 100);

            query.clear();
            page = 1;
            HashMap<String,Object> reports = new HashMap<>(1);
            do {
                if (Boolean.TRUE.equals(ProfileUtils.isSupplier())) {
                    query = XN_Query.contentQuery().tag("reportsettings")
                            .filter("type", "eic", "reportsettings")
                            .filter("my.status","=","0")
                            .filter("my.ofmenu","in", Arrays.asList("general","supplier"))
                            .notDelete().begin((page-1) * 100).end(page * 100).execute();
                } else {
                    query = XN_Query.contentQuery().tag("reportsettings")
                            .filter("type", "eic", "reportsettings")
                            .filter("my.status","=","0")
                            .filter("my.ofmenu","in", Arrays.asList("general","system"))
                            .notDelete().begin((page-1) * 100).end(page * 100).execute();
                }

                for(Object item: query) {
                    String reporttype = ((Content)item).my.get("reporttype").toString();
                    String reportgroup = ((Content)item).my.get("reportgroup").toString();
                    if(Utils.isEmpty(reports.get(reporttype))){
                        Map<String,Object> infoMap = new HashMap<>(1);
                        List<Object> infoList = new ArrayList<>(1);
                        infoList.add(item);
                        infoMap.put(reportgroup,infoList);
                        reports.put(reporttype,infoMap);
                    } else {
                        List<Object> infoList = new ArrayList<>(1);
                        infoList.add(item);
                        if(Utils.isEmpty(((HashMap<?,?>)reports.get(reporttype)).get(reportgroup))){
                            ((HashMap)reports.get(reporttype)).put(reportgroup,infoList);
                        } else {
                            ((ArrayList)((HashMap)reports.get(reporttype)).get(reportgroup)).add(item);
                        }
                    }
                }
                page++;
            }while (query.size() == 100);

            for(Map.Entry<String,Object> entry: reports.entrySet()){
                for(String group: ((HashMap<String,Object>)entry.getValue()).keySet()){
                    MenuTree<Object> tree = new MenuTree<>();
                    tree.setId(entry.getKey()+group);
                    tree.setParentId(entry.getKey());
                    tree.setTitle(group);
                    tree.setIcon("lcdp-icon-fenzu");
                    tree.setHasChild(true);
                    trees.add(tree);
                    for(Object item: (ArrayList<?>)((HashMap<?,?>)entry.getValue()).get(group)) {
                        Content report= (Content) item;
                        String reportuser = report.my.get("reportuser").toString();
                        if (!Utils.isEmpty(reportuser)) {
                            try{
                                JSONArray reportUsers= JSONArray.parseArray(reportuser);
                                if(!reportUsers.isEmpty()&&!reportUsers.contains(ProfileUtils.getCurrentProfileId())){
                                    continue;
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        }
                        MenuTree<Object> repro = new MenuTree<>();
                        repro.setId(((Content)item).id);
                        repro.setParentId(entry.getKey()+group);
                        repro.setTitle(((Content)item).my.getOrDefault("reportname","").toString());
                        repro.setIcon("lcdp-icon-baobiaosheji");
                        repro.setHref("/report/template/record="+((Content)item).id);
                        trees.add(repro);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return trees;
    }

    @Override
    public MenuTree<Object> findUserMenus(String username, String usertype, String menuType) throws Exception {
        List<MenuTree<Object>> trees = this.convertMenus(getRoleMenu(username,usertype,menuType));
        return TreeUtil.buildMenuTree(trees);
    }

    @Override
    public MenuTree<Object> findReportMenus(String username, String usertype, String menuType) throws Exception {
        return TreeUtil.buildMenuTree(getReportMenu());
    }

    private List<MenuTree<Object>> convertMenus(List<Object> groupMenus) {
        List<MenuTree<Object>> trees = new ArrayList<>();
        List<Program> programs = BaseMenuConfig.getPrograms();
        groupMenus.forEach(menu -> {
            MenuTree<Object> tree = new MenuTree<>();
            if(menu instanceof ParentTab){
                boolean isfind = false;
                for(Program program: programs){
                    if(program.id.equals(((ParentTab)menu).programid)) {
                        isfind = true;
                        break;
                    }
                }
                tree.setId(((ParentTab)menu).id);
                tree.setParentId(isfind?"":((ParentTab)menu).programid);
                tree.setTitle(((ParentTab)menu).label);
                tree.setIcon(((ParentTab)menu).icon);
                tree.setHasChild(true);
            }else if(menu instanceof ModuleMenu){
                tree.setId(((ModuleMenu)menu).id);
                tree.setParentId(((ModuleMenu)menu).parentid);
                tree.setTitle(((ModuleMenu)menu).label);
                tree.setIcon(((ModuleMenu)menu).icon);
                tree.setHref(((ModuleMenu)menu).url);
            }
            tree.setData(menu);
            trees.add(tree);
        });
        return trees;
    }

}
