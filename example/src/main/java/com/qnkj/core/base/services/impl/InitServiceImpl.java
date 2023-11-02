package com.qnkj.core.base.services.impl;

import com.github.restapi.*;
import com.github.restapi.models.Application;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.qnkj.common.configs.*;
import com.qnkj.common.entitys.*;
import com.qnkj.common.services.*;
import com.qnkj.common.utils.*;
import com.qnkj.core.base.CacheBaseEntitys;
import com.qnkj.core.base.services.IBaseService;
import com.qnkj.core.base.services.IInitService;
import com.qnkj.core.plugins.feign.FeignApi;
import com.qnkj.core.webconfigs.configure.WebConstant;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * create by 徐雁
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class InitServiceImpl implements IInitService {
    private final IProgramServices programServices;
    private final IParentTabServices parentTabServices;
    private final IModuleMenuServices moduleMenuServices;
    private final ITabServices tabServices;
    private final IBlockServices blockServices;
    private final ICustomViewServices customViewServices;
    private final ITabFieldServices tabFieldServices;
    private final ISearchColumnServices searchColumnServices;
    private final IEntityLinkServices entityLinkServices;
    private final IOutsideLinkServices outsideLinkServices;
    private final IActionServices actionServices;
    private final IPopupDialogServices popupDialogServices;
    private final IPickListServices pickListServices;
    private final IModentityNumServices modentityNumServices;
    private final ILayoutServices layoutServices;
    private final IDataPermissionServices dataPermissionServices;
    private final IApiServices apiServices;

    @Override
    public Object getStatus() {
        Object status = RedisUtils.get(WebConstant.INIT_STATUS);
        if (!Utils.isEmpty(status)) {
            InitStatusResponse response = InitStatusResponse.fromJson(status.toString());
            if (response.get("code").equals(200)) {
                RedisUtils.del(WebConstant.INIT_STATUS);
            }
            return response;
        } else {
            return new InitStatusResponse().fail("未开始初始化");
        }
    }

    @Override
    public void clearStatus() {
        Object status = RedisUtils.get(WebConstant.INIT_STATUS);
        if (!Utils.isEmpty(status)) {
            RedisUtils.del(WebConstant.INIT_STATUS);
        }
    }

    @Override
    public void initpt() {
        if (!Utils.isEmpty(RedisUtils.get(WebConstant.INIT_STATUS))) {
            throw new WebException("已经在初始化系统，请稍候。。。");
        }
        String domain = BaseSaasConfig.getDomain();
        ExecutorService pool = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        pool.execute(() -> {
            BaseSaasConfig.existDomain(domain);
            RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(0, "准备初始化。。。").toJson(), (long) 50);
            List<Class<T>> initClass = FindClassUtils.getSonClass(BaseDataConfig.class);
            if (!Utils.isEmpty(initClass)) {
                FeignApi.clearAllModuleCacheInfo();
                CacheBaseEntitys.clear();
                programServices.clear();
                parentTabServices.clear();
                moduleMenuServices.clear();
                tabServices.clear();
                blockServices.clear();
                customViewServices.clear();
                tabFieldServices.clear();
                searchColumnServices.clear();
                entityLinkServices.clear();
                outsideLinkServices.clear();
                actionServices.clear();
                popupDialogServices.clear();
                pickListServices.clear();
                modentityNumServices.clear();
                layoutServices.clear();
                apiServices.clear();
                String author = initApplication();
                initMenus(10, author);
                initSleep();
                for (Class<?> clazz : initClass) {
                    double min = 10 + 60.0 / initClass.size() * initClass.indexOf(clazz);
                    double max = 10 + 60.0 / initClass.size() * (initClass.indexOf(clazz)+1);
                    try {
                        BaseDataConfig dataConfig = (BaseDataConfig) ClassUtils.create(clazz);
                        if(dataConfig != null) {
                            initModule(dataConfig, min, max, author);
                            initSleep();
                        }
                    } catch (Exception ignored) {}
                }
                initDepartment(80, author);
                initSleep();
                initRoles(85, author);
                initSleep();
                initPicklists(90,author);
                initSleep();
                initPagedesigns(92,author);
                initSleep();
                initReportSetting(95);
                initSleep();
                RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().success(100,"初始化完成").toJson(), (long) 50);
            } else {
                RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().fail("未找到需初始化的模块").toJson(), (long) 50);
            }
        });
        pool.shutdown();
    }

    @Override
    public void initdata(String modulename) throws Exception {
        if (!Utils.isEmpty(RedisUtils.get(WebConstant.INIT_STATUS))) {
            throw new WebException("已经在初始化系统，请稍候。。。");
        }
        String domain = BaseSaasConfig.getDomain();
        ExecutorService pool = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        pool.execute(() -> {
            BaseSaasConfig.existDomain(domain);
            RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(0, "准备初始化。。。").toJson(), (long) 50);
            List<Class<T>> initClass = FindClassUtils.getSonClass(BaseDataConfig.class);
            if (!Utils.isEmpty(initClass)) {
                FeignApi.clearModuleCacheInfo(modulename);
                CacheBaseEntitys.clear(modulename);
                programServices.clear();
                parentTabServices.clear();
                moduleMenuServices.clear();
                tabServices.clear(modulename);
                blockServices.clear(modulename);
                customViewServices.clear(modulename);
                tabFieldServices.clear(modulename);
                searchColumnServices.clear(modulename);
                entityLinkServices.clear(modulename);
                outsideLinkServices.clear(modulename);
                actionServices.clear(modulename);
                popupDialogServices.clear(modulename);
                pickListServices.clear();
                modentityNumServices.clear(modulename);
                layoutServices.clear(modulename);
                apiServices.clear(modulename);
                this.clearModuleInfo(modulename,false);
                initSleep();
                boolean isfind = false;
                for (Class<T> clazz : initClass) {
                    BaseDataConfig dataConfig;
                    try {
                        dataConfig = (BaseDataConfig) ClassUtils.create(clazz);
                        if(dataConfig != null && dataConfig.tabs.modulename.equals(modulename)) {
                            isfind = true;
                            initModule(dataConfig, 0,80, this.getAdmin());
                            initSleep();
                        }
                    } catch (Exception ignored) {}
                }
                initRoles(90, this.getAdmin());
                initSleep();
                initPicklists(100,this.getAdmin());
                initSleep();
                if(isfind) {
                    RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().success(100,"初始化完成").toJson(), (long) 50);
                } else {
                    RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().code(404).message("模块名称不存在！").toJson(), (long) 50);
                }
            } else {
                RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().fail("未找到需初始化的模块").toJson(), (long) 50);
            }
        });
        pool.shutdown();
    }

    private void initSleep() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getAdmin() {
        try{
            String applicationName = XN_Rest.getApplication();
            Application application = XN_Application.load(applicationName, "application");
            XN_Rest.setViewer(application.getAuthor());
            return application.getAuthor();
        }catch (Exception e){
            return "";
        }
    }
    private String initApplication() {
        try {
            String applicationName = XN_Rest.getApplication();
            RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(1, "正在初始化应用:" + applicationName).toJson(), (long) 50);
            Application application;
            try {
                application = XN_Application.load(applicationName, "application");
            } catch (Exception e) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                application = XN_Application.create(applicationName, "oldhand " + dateFormat.format(date) + "create.");
                application.setActive("true");
                SimpleDateFormat trialdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(Calendar.YEAR, 1); //把日期往后增加一年
                date = calendar.getTime(); //这个时间就是日期往后推一年的结果
                application.setTrialtime(dateFormat.format(date));
                application.save("application");
            }
            boolean needinitprofile = false;
            if (application.getAuthor().isEmpty()) {
                needinitprofile = true;
            } else {
                try {
                    XN_Profile.load(application.getAuthor(), "profile");
                } catch (Exception e) {
                    needinitprofile = true;
                }
            }
            if (needinitprofile) {
                Profile profile = null;
                List<Object> list = XN_Query.create("Profile").tag("profile")
                        .filter(XN_Filter.all(XN_Filter.filter("regioncode", "=", "86"),
                                XN_Filter.filter("mobile", "=", "15974160308")))
                        .filter("type","in",Arrays.asList("admin","pt","supplier"))
                        .end(1).execute();
                if(!list.isEmpty()){
                    profile = (Profile) list.get(0);
                }else {
                    profile = XN_Profile.create("oldhand@tezan.cn", "123qwe");
                    profile.fullname = "admin";
                    profile.type = "admin";
                    profile.regioncode = "86";
                    profile.mobile = "15974160308";
                    profile.status = true;
                    profile.givenname = "admin";
                    profile.link = "";
                    profile.system = "";
                    profile.browser = "";
                    profile.reg_ip = "";
                    profile.save("profile");
                }
                application.setAuthor(profile.id);
                list = XN_Query.contentQuery().tag("users")
                        .filter("type","eic","users")
                        .notDelete()
                        .filter("my.mobile","=",profile.mobile)
                        .end(1).execute();
                if(list.isEmpty()){
                    Content user = XN_Content.create("users", "", profile.id);
                    user.add("account", profile.fullname)
                            .add("deleted", "0")
                            .add("username", profile.givenname)
                            .add("departmentid", "")
                            .add("roleid", "")
                            .add("directsuperior", "")
                            .add("mailbox", "oldhand@tezan.cn")
                            .add("mobile", profile.mobile)
                            .add("status", "Active")
                            .add("sequence", "1")
                            .add("is_admin", "admin")
                            .add("usertype", "system")
                            .add("userno", "")
                            .add("profileid", profile.id)
                            .save("users");
                }
                application.save("application");
            }
            XN_Rest.setViewer(application.getAuthor());
            return application.getAuthor();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    private void clearModuleInfo(String modulename, Boolean initMenu) {
        try{
            List<Object> resutl;
            XN_Query query;
            if(initMenu){
                do {
                    resutl = XN_Query.contentQuery().tag("programs")
                            .filter("type", "eic", "programs")
                            .begin(0).end(100).execute();
                    XN_Content.delete(resutl, "programs");
                } while (resutl.size() == 100);
                resutl.clear();
            }
            do {
                query = XN_Query.contentQuery().tag("parenttabs")
                        .filter("type", "eic", "parenttabs")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "parenttabs");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("tabs")
                        .filter("type", "eic", "tabs")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "tabs");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("blocks")
                        .filter("type", "eic", "blocks")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "blocks");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("customviews")
                        .filter("type", "eic", "customviews")
                        .filter(XN_Filter.any(
                                XN_Filter.filter("my.privateuser","=",""),
                                XN_Filter.filter("my.privateuser","=",null)))
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "customviews");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("cvcolumnlists")
                        .filter("type", "eic", "cvcolumnlists")
                        .filter(XN_Filter.any(
                                XN_Filter.filter("my.privateuser","=",""),
                                XN_Filter.filter("my.privateuser","=",null)))
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "cvcolumnlists");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("popupdialogs")
                        .filter("type", "eic", "popupdialogs")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "popupdialogs");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("popupdialogcolumns")
                        .filter("type", "eic", "popupdialogcolumns")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "popupdialogcolumns");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("fields")
                        .filter("type", "eic", "fields")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "fields");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("searchcolumns")
                        .filter("type", "eic", "searchcolumns")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "searchcolumns");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("entitynames")
                        .filter("type", "eic", "entitynames")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "entitynames");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("fieldmodulerels")
                        .filter("type", "eic", "fieldmodulerels")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "fieldmodulerels");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("modentity_nums")
                        .filter("type", "eic", "modentity_nums")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.semodule","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "modentity_nums");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("customactions")
                        .filter("type", "eic", "customactions")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "customactions");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                resutl = XN_Query.contentQuery().tag("picklists")
                        .filter("type", "eic", "picklists")
                        .filter("my.builtin", "=", "1")
                        .begin(0).end(100).execute();
                XN_Content.delete(resutl, "picklists");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("layouts")
                        .filter("type", "eic", "layouts")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "layouts");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("formdesigns")
                        .filter("type", "eic", "formdesigns")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "formdesigns");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("role2tabs")
                        .filter("type", "eic", "role2tabs")
                        .isDelete().begin(0).end(100);
                resutl = query.execute();
                XN_Content.delete(resutl, "role2tabs");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("api")
                        .filter("type", "eic", "api")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "api");
            } while (resutl.size() == 100);
            resutl.clear();
            do {
                query = XN_Query.contentQuery().tag("filterfield")
                        .filter("type", "eic", "filterfield")
                        .begin(0).end(100);
                if(!Utils.isEmpty(modulename)){
                    query.filter("my.modulename","eic",modulename);
                }
                resutl = query.execute();
                XN_Content.delete(resutl, "filterfield");
            } while (resutl.size() == 100);
            resutl.clear();

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private void initMenus(double progress, String author) {
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(progress, "初始化菜单组").toJson(), (long) 50);
        try {
            //region 清除所有记录
            this.clearModuleInfo(null,true);
            //endregion
            List<Program> programs = BaseMenuConfig.getProgramSettings();
            HashMap<String, Program> programGroup = new HashMap<>();
            if(!Utils.isEmpty(programs)) {
                for (Program program : programs) {
                    if (!Utils.isEmpty(program.id)) {
                        program.id = "";
                    }
                    try {
                        programServices.update(program);
                        programGroup.put(program.group, program);
                    } catch (Exception ignored) {
                    }
                }
            }
            List<ParentTab> groups = BaseMenuConfig.getParentMenus();
            HashMap<String, ParentTab> parentGroup = new HashMap<>();
            if(!Utils.isEmpty(groups)) {
                for (ParentTab parentTab : groups) {
                    if (Utils.isEmpty(parentTab.program) && Utils.isEmpty(parentTab.name)) {
                        continue;
                    }
                    if (Utils.isEmpty(programGroup.get(parentTab.program)) && Utils.isEmpty(parentGroup.get(parentTab.program))) {
                        continue;
                    }
                    if (!Utils.isEmpty(parentTab.id)) {
                        parentTab.id = "";
                    }
                    try {
                        if (!Utils.isEmpty(programGroup.get(parentTab.program))) {
                            parentTab.programid = programGroup.get(parentTab.program).id;
                        } else if (!Utils.isEmpty(parentGroup.get(parentTab.program))) {
                            parentTab.programid = parentGroup.get(parentTab.program).id;
                        }
                        parentTabServices.update(parentTab);
                        parentGroup.put(parentTab.name, parentTab);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("initMenus Error:{}",e.getMessage());
        }
    }

    private void initModule(BaseDataConfig dataConfig, double min, double max, String author) {
        if (Utils.isEmpty(dataConfig.tabs.modulename)) {
            return;
        }
        String modulename = dataConfig.tabs.modulename;
        String menuLabel = (Utils.isEmpty(dataConfig.moduleMenu) || Utils.isEmpty(dataConfig.moduleMenu.label)) ? "<未配置菜单>" : dataConfig.moduleMenu.label;
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        List<Object> query;
        List<Object> saved = new ArrayList<>();
        //region 初始化菜单
        if (!Utils.isEmpty(dataConfig.moduleMenu)) {
            try {
                dataConfig.moduleMenu.tabname = !Utils.isEmpty(dataConfig.tabs.tabname) ? dataConfig.tabs.tabname.toLowerCase() : "";
                dataConfig.moduleMenu.modulename = modulename;
                moduleMenuServices.update(dataConfig.moduleMenu);
            } catch (Exception e) {
                log.error("init_module." + modulename + ".menu." + menuLabel + " error ：" + e);
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化数据基本信息 tabs
        try {
            do {
                query = XN_Query.contentQuery().tag("tabs")
                        .filter("type", "eic", "tabs")
                        .filter("my.modulename", "eic", modulename)
                        .begin(0).end(100).execute();
                XN_Content.delete(query, "tabs");
            } while (query.size() == 100);
            query.clear();
            Content tabs = XN_Content.create("tabs", "", author);
            dataConfig.tabs.toContent(tabs);
            tabs.save("tabs");
        } catch (Exception e) {
            log.error("init_module." + modulename + ".moduleinfo error ：" + e.toString());
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*2, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化数据块 blocks
        if (!Utils.isEmpty(dataConfig.blocks)) {
            try {
                do {
                    query = XN_Query.contentQuery().tag("blocks")
                            .filter("type", "eic", "blocks")
                            .filter("my.modulename", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "blocks");
                } while (query.size() == 100);
                query.clear();
                int index = 0;
                for (Block item : dataConfig.blocks) {
                    Content tabs = XN_Content.create("blocks", "", author);
                    item.modulename(modulename).sequence(index).toContent(tabs);
                    saved.add(tabs);
                    index++;
                }
                if (!saved.isEmpty()) {
                    XN_Content.batchsave(saved, "blocks");
                    saved.clear();
                }
            } catch (Exception e) {
                log.error("init_module." + modulename + ".blocks error ：" + e);
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*3, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化布局信息 layouts
        if(!Utils.isEmpty(dataConfig.layouts)){
            try {
                do {
                    query = XN_Query.contentQuery().tag("layouts")
                            .filter("type", "eic", "layouts")
                            .filter("my.modulename", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "layouts");
                } while (query.size() == 100);
                query.clear();
                int index = 0;
                for (Layout item : dataConfig.layouts) {
                    Content layout = XN_Content.create("layouts", "", author);
                    item.modulename(modulename).sequence(index).toContent(layout);
                    saved.add(layout);
                    index++;
                }
                if (!saved.isEmpty()) {
                    XN_Content.batchsave(saved, "layouts");
                    saved.clear();
                }
            }catch (Exception e) {
                log.error("init_module." + modulename + ".layouts error ：" + e);
            }

        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*4, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化数据列表信息 customviews
        if (!Utils.isEmpty(dataConfig.customViews)) {
            try {
                do {
                    query = XN_Query.contentQuery().tag("customviews")
                            .filter("type", "eic", "customviews")
                            .filter("my.modulename", "eic", modulename)
                            .filter(XN_Filter.any(
                                    XN_Filter.filter("my.privateuser","=",""),
                                    XN_Filter.filter("my.privateuser","=",null)))
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "customviews");
                } while (query.size() == 100);
                query.clear();
                do {
                    query = XN_Query.contentQuery().tag("cvcolumnlists")
                            .filter("type", "eic", "cvcolumnlists")
                            .filter("my.modulename", "eic", modulename)
                            .filter(XN_Filter.any(
                                    XN_Filter.filter("my.privateuser","=",""),
                                    XN_Filter.filter("my.privateuser","=",null)))
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "cvcolumnlists");
                } while (query.size() == 100);
                query.clear();
                for (CustomView item : dataConfig.customViews) {
                    List<String> columnlist = item.columnlist;
                    if (Utils.isEmpty(columnlist)) {
                        continue;
                    }
                    Content tabs = XN_Content.create("customviews", "", author);
                    String viewid = tabs.add("modulename", modulename)
                            .add("viewname", item.viewname)
                            .add("authorize", item.authorize)
                            .add("orderby", item.orderby)
                            .add("order", item.order)
                            .add("privateuser",item.privateuser)
                            .add("isdefault", item.isdefault ? "1" : "0")
                            .save("customviews").id;
                    int index = 1;
                    for (Object column : columnlist) {
                        Content cvcolumnlists = XN_Content.create("cvcolumnlists", "", author);
                        cvcolumnlists.add("privateuser",item.privateuser).add("modulename", modulename).add("columnname", column).add("sequence", index).add("record", viewid);
                        saved.add(cvcolumnlists);
                        index++;
                    }
                    if (!saved.isEmpty()) {
                        XN_Content.batchsave(saved, "cvcolumnlists");
                        saved.clear();
                    }
                }
            } catch (Exception e) {
                log.error("init_module." + modulename + ".customViews error ：" + e.toString());
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*5, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化数据字段 fields
        if (!Utils.isEmpty(dataConfig.fields)) {
            try {
                do {
                    query = XN_Query.contentQuery().tag("fields")
                            .filter("type", "eic", "fields")
                            .filter("my.modulename", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "fields");
                } while (query.size() == 100);
                query.clear();
                int index = 1;
                for (TabField item : dataConfig.fields) {
                    List<Object> isfind = new ArrayList<>();
                    for(Object tmp : saved){
                        if(((Content)tmp).my.get("fieldname").equals(item.fieldname)){
                            isfind.add(tmp);
                            break;
                        }
                    }
                    saved.removeAll(isfind);
                    Content tabs = XN_Content.create("fields", "", author);
                    item.modulename(modulename).sequence(index).toContent(tabs);
                    saved.add(tabs);
                    index++;
                }
                if (!saved.isEmpty()) {
                    XN_Content.batchsave(saved, "fields");
                    saved.clear();
                }
            } catch (Exception e) {
                log.error("init_module." + modulename + ".fields error ：" + e.toString());
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*6, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化数据查询信息 searchcolumns
        if (!Utils.isEmpty(dataConfig.searchColumn)) {
            try {
                do {
                    query = XN_Query.contentQuery().tag("searchcolumns")
                            .filter("type", "eic", "searchcolumns")
                            .filter("my.modulename", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "searchcolumns");
                } while (query.size() == 100);
                query.clear();
                if (!Utils.isEmpty(dataConfig.searchColumn)) {
                    int index = 1;
                    for (SearchColumn item : dataConfig.searchColumn) {
                        Content tabs = XN_Content.create("searchcolumns", "", author);
                        item.order(index).modulename(modulename).toContent(tabs);
                        saved.add(tabs);
                        index++;
                    }
                    if (!saved.isEmpty()) {
                        XN_Content.batchsave(saved, "searchcolumns");
                        saved.clear();
                    }
                }
            } catch (Exception e) {
                log.error("init_module." + modulename + ".searchColumn error ：" + e.toString());
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*7, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化数据关联字段 entitynames
        if (!Utils.isEmpty(dataConfig.entityNames)) {
            try {
                do {
                    query = XN_Query.contentQuery().tag("entitynames")
                            .filter("type", "eic", "entitynames")
                            .filter("my.modulename", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "entitynames");
                } while (query.size() == 100);
                query.clear();
                if (!Utils.isEmpty(dataConfig.entityNames.fieldname)) {
                    Content tabs = XN_Content.create("entitynames", "", author);
                    dataConfig.entityNames.modulename(modulename).toContent(tabs);
                    tabs.save("entitynames");
                }
            } catch (Exception e) {
                log.error("init_module." + modulename + ".entityNames error ：" + e.toString());
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*8, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化外部数据链接 fieldmodulerels
        if (!Utils.isEmpty(dataConfig.outsideLinks)) {
            try {
                do {
                    query = XN_Query.contentQuery().tag("fieldmodulerels")
                            .filter("type", "eic", "fieldmodulerels")
                            .filter("my.modulename", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "fieldmodulerels");
                } while (query.size() == 100);
                query.clear();
                for (OutsideLink item : dataConfig.outsideLinks) {
                    Content tabs = XN_Content.create("fieldmodulerels", "", author);
                    item.modulename(modulename).toContent(tabs);
                    saved.add(tabs);
                }
                if (!saved.isEmpty()) {
                    XN_Content.batchsave(saved, "fieldmodulerels");
                    saved.clear();
                }
            } catch (Exception e) {
                log.error("init_module." + modulename + ".outsideLinks error ：" + e.toString());
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*9, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化弹窗 PopupDialog
        if (!Utils.isEmpty(dataConfig.popupDialog.columns)) {
            try {
                do {
                    query = XN_Query.contentQuery().tag("popupdialogs")
                            .filter("type", "eic", "popupdialogs")
                            .filter("my.modulename", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "popupdialogs");
                } while (query.size() == 100);
                query.clear();
                do {
                    query = XN_Query.contentQuery().tag("popupdialogcolumns")
                            .filter("type", "eic", "popupdialogcolumns")
                            .filter("my.modulename", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "popupdialogcolumns");
                } while (query.size() == 100);
                query.clear();

                List<String> columnlist = dataConfig.popupDialog.columns;
                Content tabs = XN_Content.create("popupdialogs", "");
                String viewid = tabs.add("modulename", modulename)
                        .add("search", dataConfig.popupDialog.search)
                        .save("popupdialogs").id;
                int index = 1;
                for (Object column : columnlist) {
                    Content cvcolumnlists = XN_Content.create("popupdialogcolumns", "");
                    cvcolumnlists.add("modulename", modulename).add("columnname", column).add("sequence", index).add("record", viewid).save("popupdialogcolumns");
                    index++;
                }
            } catch (Exception e) {
                log.error("init_module." + modulename + ".popupDialog error ：" + e.toString());
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*10, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化数据权限
        if(Utils.isNotEmpty(dataConfig.dataPermission) && Utils.isNotEmpty(dataConfig.dataPermission.getExpressions())) {
            try {
                int page = 0;
                List<Object> validationroles;
                do {
                    validationroles = XN_Query.contentQuery().tag("datapermissions")
                            .filter("type", "eic", "datapermissions")
                            .filter("my.modulename", "eic", modulename)
                            .begin(page * 100).end((page + 1) * 100)
                            .notDelete().end(-1).execute();
                    if(!validationroles.isEmpty()) {
                        XN_Content.delete(validationroles, "datapermissions");
                    }
                    page++;
                }while (validationroles.size() == 100);

                dataConfig.dataPermission.setModulename(modulename);
                dataPermissionServices.update(dataConfig.dataPermission);
            }catch (Exception e) {
                log.error("init_module." + modulename + ".dataPermission error ：" + e);
            }
        }
        //endregion
        //region 初始化自动编号 modentity_nums
        if (!Utils.isEmpty(dataConfig.modentityNums.prefix)) {
            try {
                do {
                    query = XN_Query.contentQuery().tag("modentity_nums")
                            .filter("type", "eic", "modentity_nums")
                            .filter("my.semodule", "eic", modulename)
                            .begin(0).end(100).execute();
                    XN_Content.delete(query, "modentity_nums");
                } while (query.size() == 100);
                query.clear();
                Content tabs = XN_Content.create("modentity_nums", "", author);
                dataConfig.modentityNums.modulename(modulename).toContent(tabs);
                tabs.add("active", 1)
                        .add("tabid", DateTimeUtils.getCurrentTimeStamp())
                        .add("date", DateTimeUtils.getDatetime("yyMMdd"))
                        .save("modentity_nums");
            } catch (Exception e) {
                log.error("init_module." + modulename + ".modentityNums error ：" + e.toString());
            }
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*11, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化API接口配置
        if (!Utils.isEmpty(dataConfig.api)) {
            if (dataConfig.api.iscreate || dataConfig.api.isdelete || dataConfig.api.islist || dataConfig.api.isupdate || dataConfig.api.ispagelimit) {
                try {
                    query = XN_Query.contentQuery().tag("api")
                            .filter("type", "eic", "api")
                            .filter("my.modulename", "=", modulename)
                            .end(1).execute();
                    XN_Content.delete(query, "api");
                    query.clear();
                    Content obj = XN_Content.create("api", "", XN_Rest.getViewer());
                    obj.my.put("modulename", dataConfig.api.modulename);
                    obj.my.put("iscreate", dataConfig.api.iscreate ? 1 : 0);
                    obj.my.put("isload", dataConfig.api.isload ? 1 : 0);
                    obj.my.put("isdelete", dataConfig.api.isdelete ? 1 : 0);
                    obj.my.put("islist", dataConfig.api.islist ? 1 : 0);
                    obj.my.put("isupdate", dataConfig.api.isupdate ? 1 : 0);
                    obj.my.put("isupdaterecord", dataConfig.api.isupdaterecord ? 1 : 0);
                    obj.my.put("ispagelimit", dataConfig.api.ispagelimit ? 1 : 0);
                    obj.my.put("createfields", dataConfig.api.createfields);
                    obj.my.put("updatefields", dataConfig.api.updatefields);
                    obj.my.put("updaterecordfields", dataConfig.api.updaterecordfields);
                    obj.my.put("listfields", dataConfig.api.listfields);
                    obj.save("api");
                    if (dataConfig.api.filterfields.size() > 0) {
                        for (FilterField item : dataConfig.api.filterfields) {
                            Content createObj = XN_Content.create("filterfield", "", XN_Rest.getViewer());
                            createObj.my.put("modulename", dataConfig.api.modulename);
                            createObj.my.put("name", item.name);
                            createObj.my.put("describe", item.describe);
                            createObj.my.put("fieldName", item.fieldName);
                            createObj.my.put("defaultValue", item.defaultValue);
                            createObj.my.put("required", item.required ? 1 : 0);
                            createObj.my.put("symbol", item.symbol.getValue());
                            createObj.save("filterfield");
                        }
                    }
                } catch (Exception e) {
                    log.error("init_module." + modulename + ".api error ：" + e.toString());
                }
            }

        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(min + (max-min)/12.0*11, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
        //region 初始化操作配置 customactions
        try {
            List<Class<T>> initClass = FindClassUtils.getSonClass(dataConfig.getClass().getPackage().getName(), BaseActionsConfig.class);
            if (!initClass.isEmpty()) {
                for (Class<T> clazz : initClass) {
                    try {
                        BaseActionsConfig actionsConfig = (BaseActionsConfig) ClassUtils.create(clazz);;
                        try {
                            if (actionsConfig != null && !actionsConfig.actions.isEmpty()) {
                                try {
                                    do {
                                        query = XN_Query.contentQuery().tag("customactions")
                                                .filter("type", "eic", "customactions")
                                                .filter("my.modulename", "eic", modulename)
                                                .begin(0).end(100).execute();
                                        XN_Content.delete(query, "customactions");
                                    } while (query.size() == 100);
                                    query.clear();
                                    int index = 1;
                                    for (Action item : actionsConfig.actions) {
                                        Content tabs = XN_Content.create("customactions", "", author);
                                        if(item.securitycheck && !Utils.isEmpty(item.funpara) && Utils.isEmpty(item.funclass)){
                                            item.funclass(IBaseService.class.getName());
                                        }
                                        item.order(index).modulename(modulename).toContent(tabs);
                                        saved.add(tabs);
                                        index++;
                                    }
                                    if (!saved.isEmpty()) {
                                        XN_Content.batchsave(saved, "customactions");
                                        saved.clear();
                                    }
                                } catch (Exception e) {
                                    log.error("init_module." + modulename + ".actions error ：" + e.toString());
                                }
                            }
                        } catch (Exception e) {
                            log.error("init_module." + modulename + ".BaseActionsConfig error ：" + e.toString());
                        }
                    } catch (Exception e) {
                        log.error("init_module." + modulename + ".getSonClass.BaseActionsConfig error ：" + e.toString());
                    }
                }
            }
        } catch (Exception e) {
            log.error("init_module." + modulename + ".getSonClass.BaseActionsConfig error ：" + e.toString());
        }
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(max, "正在初始化模块:" + menuLabel).toJson(), (long) 50);
        //endregion
    }

    public void initPicklists(double progress, String author) {
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(progress, "初始化字典数据").toJson(), (long) 50);
        //region 初始化字典数据 picklists
        List<PickList> pickLists = BasePicklistConfig.getPicklists();
        if (!Utils.isEmpty(pickLists)) {
            try {
                List<String> picklistnames = new ArrayList<>();
                for (PickList item : pickLists) {
                    if (Utils.isEmpty(item.entitys)) {
                        continue;
                    }
                    if(picklistnames.contains(item.name)) {
                        continue;
                    }
                    picklistnames.add(item.name);

                    int index = 0;
                    List<Object> saved = new ArrayList<>();

                    if (!item.builtin) {
                        List<Object> customPicklists = XN_Query.contentQuery().tag("picklists")
                                .filter("type", "eic", "picklists")
                                .filter("builtin", "=", "0")
                                .filter("picklistname", "=", item.name)
                                .filter("author", "=", "system")
                                .notDelete()
                                .begin(0).end(-1).execute();
                        XN_Content.delete(customPicklists, "picklists");
                    }
                    for (PickListEntity picklist : item.entitys) {
                        Content tabs = XN_Content.create("picklists", "", "system");
                        tabs.add("picklistname", item.name)
                                .add("builtin", item.builtin?"1":"0")
                                .add("strval", picklist.strval)
                                .add("label", picklist.label)
                                .add("intval", picklist.intval)
                                .add("styclass", picklist.styclass)
                                .add("sequence", index)
                                .add("picklistlabel", item.label);
                        saved.add(tabs);
                        index++;
                    }
                    if (!saved.isEmpty()) {
                        try {
                            XN_Content.batchsave(saved, "picklists");
                        } catch (Exception e) {
                            log.error("init_picklists " + item.name + "error ：" + e.toString());
                        }
                        saved.clear();
                    }

                    RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(progress, "初始化字典数据").toJson(), (long) 50);
                }
            } catch (Exception e) {
                log.error("init_picklists error ：" + e);
            }
        }
        //endregion
    }

    public void initPagedesigns(double progress, String author) {
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(progress, "初始化页面设计").toJson(), (long) 50);
        //region 初始化页面设计
        if (!Utils.isEmpty(BasePageDesignConfig.pageDesignList)) {
            try {
                for (PageDesignInitInfo item : BasePageDesignConfig.pageDesignList) {
                    List<Object> pagedesigns = XN_Query.contentQuery().tag("pagedesigns")
                            .filter("type", "eic", "pagedesigns")
                            .notDelete()
                            .filter("my.modulename", "=", item.modulename)
                            .end(1).execute();
                    if (!pagedesigns.isEmpty()) {
                        Content pagedesign = (Content)pagedesigns.get(0);
                        pagedesign.my.put("program",item.program);
                        pagedesign.my.put("parent",item.parent);
                        pagedesign.my.put("module",item.module);
                        pagedesign.my.put("template_editor",item.template_editor);
                        pagedesign.save("pagedesigns");
                    } else {
                        Content pagedesign = XN_Content.create("pagedesigns", "", author);
                        pagedesign.my.put("modulename",item.modulename);
                        pagedesign.my.put("generate","0");
                        pagedesign.my.put("program",item.program);
                        pagedesign.my.put("parent",item.parent);
                        pagedesign.my.put("module",item.module);
                        pagedesign.my.put("template_editor",item.template_editor);
                        pagedesign.save("pagedesigns");
                    }
                }
            } catch (Exception e) {
                log.error("init_pagedesigns error ：" + e.toString());
            }
        }
        //endregion
    }

    /**
     * 初始化部门
     *
     * @param author 创建人porofileid
     */
    public void initDepartment(double progress, String author) {
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(progress, "初始化部门结构").toJson(), (long) 50);
        SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
        String departmentRoot = saasUtils.getCompany();
        try {
            String departmentid = "";
            List<Object> resultQuery = XN_Query.contentQuery().tag("departments")
                    .filter("type", "eic", "departments")
                    .end(-1).execute();
            if (resultQuery.isEmpty()) {
                Content department = XN_Content.create("departments", "", author);
                department.add("ishide", "0");
                department.add("parentid", "");
                department.add("departmentname", departmentRoot);
                department.add("sequence", "1");
                department = department.save("departments");
                String parentid = department.id;
                department = XN_Content.create("departments", "", author);
                department.add("ishide", "0");
                department.add("parentid", parentid);
                department.add("departmentname", "CEO");
                department.add("sequence", "1");
                department.save("departments");
                departmentid = department.id;
            } else {
                departmentid = ((Content)resultQuery.get(0)).id;
            }
            List<Object> list = XN_Query.contentQuery().tag("users")
                    .filter("type","eic","users")
                    .notDelete()
                    .filter("my.profileid","=",XN_Rest.getViewer())
                    .filter(XN_Filter.any(
                            XN_Filter.filter("my.departmentid","=",""),
                            XN_Filter.filter("my.departmentid","=",null)))
                    .end(1).execute();
            if(!list.isEmpty()){
                Content user = (Content)list.get(0);
                user.my.put("departmentid",departmentid);
                user.save("users");
            }
        } catch (Exception e) {
            log.error("init_department error ：" + e);
        }
    }

    /**
     * 初始化权限
     *
     * @param author 创建人porofileid
     */
    public void initRoles(double progress, String author) {
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(progress, "初始化默认权限").toJson(), (long) 50);
        try {
            List<Object> rolesQuery = XN_Query.contentQuery().tag("roles")
                    .filter("type", "eic", "roles")
                    .filter("my.rolename", "=", "超级删除")
                    .notDelete().execute();
            if (rolesQuery.isEmpty()) {
                Content roles = XN_Content.create("roles", "", author);
                roles.add("rolename", "超级删除")
                        .add("description", "BOSS权限，超级删除控制")
                        .add("allowdeleted", "0")
                        .add("superdelete", "0")
                        .save("roles");
            } else {
                ((Content) rolesQuery.get(0)).my.put("superdelete", "0");
                ((Content) rolesQuery.get(0)).save("roles");
            }
            boolean isview = true;
            boolean isedit = false;
            rolesQuery = XN_Query.contentQuery().tag("roles")
                    .filter("type", "eic", "roles")
                    .filter("my.rolename", "=", "标准读写权限")
                    .notDelete().execute();
            String roleid = "";
            if (rolesQuery.isEmpty()) {
                Content roles = XN_Content.create("roles", "", author);
                roleid = roles.add("rolename", "标准读写权限")
                        .add("description", "所有模块只读")
                        .add("allowdeleted", "0")
                        .add("globalview", "1")
                        .add("globaledit", "0")
                        .add("supplierid", "0")
                        .save("roles").id;
            } else {
                roleid = ((Content)rolesQuery.get(0)).id;
                isview = !"0".equals(((Content) rolesQuery.get(0)).my.getOrDefault("globalview", "0"));
                isedit = !"0".equals(((Content) rolesQuery.get(0)).my.getOrDefault("globaledit", "0"));
            }
            CacheBaseEntitys.clearRoles(roleid);
            updateRole2tabs(roleid,null,isview,isedit,false);
            isview = true;
            isedit = true;
            rolesQuery = XN_Query.contentQuery().tag("roles")
                    .filter("type", "eic", "roles")
                    .filter("my.rolename", "=", "企业权限")
                    .notDelete().execute();
            if (rolesQuery.isEmpty()) {
                Content roles = XN_Content.create("roles", "", author);
                roleid = roles.add("rolename", "企业权限")
                        .add("description", "所有企业权限")
                        .add("allowdeleted", "0")
                        .add("globalview", "1")
                        .add("globaledit", "1")
                        .add("supplierid", "0")
                        .save("roles").id;
            } else {
                roleid = ((Content)rolesQuery.get(0)).id;
                isview = !"0".equals(((Content) rolesQuery.get(0)).my.getOrDefault("globalview", "0"));
                isedit = !"0".equals(((Content) rolesQuery.get(0)).my.getOrDefault("globaledit", "0"));
            }
            CacheBaseEntitys.clearRoles(roleid);
            updateRole2tabs(roleid,"settings",isview,isedit,false,true);
            isview = true;
            isedit = false;
            rolesQuery = XN_Query.contentQuery().tag("roles")
                    .filter("type", "eic", "roles")
                    .filter("my.rolename", "=", "未审核企业权限")
                    .notDelete().execute();
            if (rolesQuery.isEmpty()) {
                Content roles = XN_Content.create("roles", "", author);
                roleid = roles.add("rolename", "未审核企业权限")
                        .add("description", "未审核企业权限")
                        .add("allowdeleted", "0")
                        .add("globalview", "1")
                        .add("globaledit", "0")
                        .add("supplierid", "0")
                        .save("roles").id;
            } else {
                roleid = ((Content)rolesQuery.get(0)).id;
                isview = !"0".equals(((Content) rolesQuery.get(0)).my.getOrDefault("globalview", "0"));
                isedit = !"0".equals(((Content) rolesQuery.get(0)).my.getOrDefault("globaledit", "0"));
            }
            CacheBaseEntitys.clearRoles(roleid);
            updateRole2tabs(roleid,"settings",isview,isedit,false,true);
            rolesQuery = XN_Query.contentQuery().tag("roles")
                    .filter("type", "eic", "roles")
                    .filter("my.rolename", "!in", Arrays.asList("未审核企业权限","企业权限","标准读写权限","超级删除"))
                    .notDelete().end(-1).execute();
            if(!rolesQuery.isEmpty()){
                for(Object item: rolesQuery){
                    isview = !"0".equals(((Content) item).my.getOrDefault("globalview", "0"));
                    isedit = !"0".equals(((Content) item).my.getOrDefault("globaledit", "0"));
                    CacheBaseEntitys.clearRoles(((Content)item).id);
                    updateRole2tabs(((Content)item).id,"settings",isview, isedit,false);
                }
            }
            //同步企业自定义权限设置
            rolesQuery = XN_Query.contentQuery().tag("supplier_roles")
                    .filter("type", "eic", "supplier_roles")
                    .notDelete().end(-1).execute();
            if(!rolesQuery.isEmpty()){
                for(Object item: rolesQuery){
                    isview = !"0".equals(((Content) item).my.getOrDefault("globalview", "0"));
                    isedit = !"0".equals(((Content) item).my.getOrDefault("globaledit", "0"));
                    CacheBaseEntitys.clearRoles(((Content)item).id);
                    updateRole2tabs(((Content)item).id,"settings",isview, isedit,false);
                }
            }
        } catch (Exception e) {
            log.error("init_roles error ：" + e);
        }
    }

    private void updateRole2tabs(String roleid, String exclude, boolean isview, boolean isedit, boolean isdelete){
        updateRole2tabs(roleid,exclude,isview,isedit,isdelete,false);
    }
    private void updateRole2tabs(String roleid, String exclude, boolean isview, boolean isedit, boolean isdelete,boolean isSupplier) {
        if(Utils.isEmpty(roleid)) {
            return;
        }
        try {
            List<Object> role2tabs = new ArrayList<>();
            List<Object> query;
            int page = 0;
            do {
                query = XN_Query.contentQuery().tag("role2tabs")
                        .filter("type", "eic", "role2tabs")
                        .filter("my.record", "=", roleid)
                        .notDelete()
                        .begin(page).end((page+1)*100).execute();
                role2tabs.addAll(query);
                page++;
            } while (query.size() == 100);
            query.clear();
            List<Object> create = new ArrayList<>();
            List<Program> programs = programServices.list();
            if(!Utils.isEmpty(programs)) {
                for (Program program : programs) {
                    if(isSupplier && !("general".equals(program.authorize) || "supplier".equals(program.authorize))) {
                        continue;
                    }
                    List<ParentTab> parentTabs = parentTabServices.list(program.group);
                    create.addAll(updateRole2tabs(parentTabs,role2tabs,roleid,program.group,exclude,isview,isedit,isdelete));
                }
            }
            if (!create.isEmpty()) {
                XN_Content.batchsave(create, "role2tabs");
                create.clear();
            }
        } catch (Exception e) {
            log.error("init_roles.update_role2tabs error ：" + e);
        }
    }

    private List<Object> updateRole2tabs(List<ParentTab> parentTabs,List<Object> role2tabs,String roleid, String program, String exclude, boolean isview, boolean isedit, boolean isdelete) {
        List<Object> create = new ArrayList<>();
        List<Object> update = new ArrayList<>();
        if(!Utils.isEmpty(parentTabs)) {
            for (ParentTab parentTab : parentTabs) {
                List<ParentTab> subParent = parentTabServices.list(parentTab.name);
                if(!Utils.isEmpty(subParent)){
                    create.addAll(updateRole2tabs(subParent,role2tabs,roleid,program,exclude,isview,isedit,isdelete));
                }
                List<ModuleMenu> moduleMenus = moduleMenuServices.list(parentTab.program, parentTab.name);
                if(!Utils.isEmpty(moduleMenus)) {
                    for (ModuleMenu moduleMenu : moduleMenus) {
                        boolean isfind = false;
                        for(Object item: role2tabs){
                            if(((Content)item).my.get("module").equals(moduleMenu.modulename)){
                                isfind = true;
                                ((Content)item).my.put("parentid",moduleMenu.parentid);
                                ((Content)item).my.put("moduleid",moduleMenu.id);
                                update.add(item);
                                break;
                            }
                        }
                        if(!isfind && !(!Utils.isEmpty(exclude) && program.equals(exclude))) {
                            Content newConn = XN_Content.create("role2tabs", "");
                            newConn.add("record", roleid);
                            newConn.add("deleted", "0");
                            newConn.add("classify", !Utils.isEmpty(moduleMenu.program) ? moduleMenu.program : "");
                            newConn.add("parentid", !Utils.isEmpty(moduleMenu.parentid) ? moduleMenu.parentid : "");
                            newConn.add("moduleid", !Utils.isEmpty(moduleMenu.id) ? moduleMenu.id : "");
                            newConn.add("module", !Utils.isEmpty(moduleMenu.modulename) ? moduleMenu.modulename : "");
                            newConn.add("isview", "1");
                            newConn.add("isedit", isedit ? "1" : "0");
                            newConn.add("isdelete", isdelete ? "1" : "0");
                            create.add(newConn);
                        }
                    }
                }
            }
        }
        if (!update.isEmpty()) {
            try {
                XN_Content.batchsave(update, "role2tabs");
            }catch (Exception e){
                log.error(e.getMessage());
            }
            update.clear();
        }
        return create;
    }

    /**
     * 初始化报表设置
     */
    public void initReportSetting(double progress) {
        RedisUtils.set(WebConstant.INIT_STATUS, new InitStatusResponse().progress(progress, "初始化报表设置").toJson(), (long) 50);
        try{
            CallbackUtils.invokeByModule("reportsettings","reportInit",IBaseService.class);
        }catch (Exception e) {
            log.error("init_ReportSetting error ：" + e);
        }
        try{
            CallbackUtils.invokeByModule("invoiceprint","printInit",IBaseService.class);
        }catch (Exception e) {
            log.error("init_ReportSetting error ：" + e);
        }
    }
}
