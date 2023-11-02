package com.qnkj.core.base.controller;

import com.github.restapi.XN_Rest;
import com.github.restapi.models.Profile;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.*;
import com.qnkj.core.base.entitys.ProfileSettings;
import com.qnkj.core.base.modules.management.notices.services.INoticesService;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.base.services.*;
import com.qnkj.core.base.services.impl.ValidateCodeService;
import com.qnkj.core.utils.AuthorizeUtils;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.annotation.Log;
import com.qnkj.core.webconfigs.configure.GlobalConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller("BasicEntrance")
@RequiredArgsConstructor
@Api(tags = "框架：基础页面接口")
public class EntranceController {
    private final IProfileSettingsService profileSettingsService;
    private final ValidateCodeService validateCodeService;
    private final IBaseEntityService baseEntityService;
    private final INoticesService noticesService;
    private final IMenuService menuService;
    private final ISystemInfo systemInfo;

    @GetMapping("login")
    @ApiOperation("显示登录界面")
    @Log("显示登录界面")
    public Object login(HttpServletRequest request, Model model) {
        if (Utils.isAjaxRequest(request)) {
            throw new ExpiredSessionException("no login");
        } else {
            if (validateCodeService.checkLoginErrorSign(request)) {
                model.addAttribute("LOGINERRORSIGN", true);
            } else {
                model.addAttribute("LOGINERRORSIGN", false);
            }
            model.addAttribute("APPID", XN_Rest.getApplication());

            return WebViews.view("login");
        }
    }

    @GetMapping("register")
    @ApiOperation("显示注册界面")
    public Object register(HttpServletRequest request, Model model) {
        if (Utils.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
            if(saasUtils.isRegister()) {
                model.addAttribute("autoapprovalregister", GlobalConfig.getAutoApprovalRegister());
                model.addAttribute("APPID", XN_Rest.getApplication());
                return WebViews.view("register");
            } else {
                return WebViews.view("/error/403");
            }
        }
    }

    @GetMapping("unauthorized")
    @ApiOperation("显示403页面（未认证）")
    public String unauthorized() {
        return WebViews.view("/error/403");
    }

    @GetMapping("/")
    @ApiOperation("跳转首页")
    public String redirectIndex( HttpServletResponse response) {
        String filePath = "templates/cms/index.html";
        ClassPathResource resource = new ClassPathResource(filePath);
        if (resource.exists()) {
            try {
                String html = "";
                try{
                    Object result = CallbackUtils.invoke("renderIndex", "com.qnkj.cms.core.services", IBaseService.class);
                    if (Utils.isNotEmpty(result)) {
                        html = result.toString();
                    }
                }catch (Exception e) {
                    log.warn("==========================================================");
                    log.warn("回调错误 ： renderIndex");
                    log.warn("回调错误 ： package：{}", "com.qnkj.cms.core.services");
                    log.warn("回调错误 ： Error：{}", e.getMessage());
                    log.warn("==========================================================");
                    throw e;
                }
                ServletOutputStream sos = response.getOutputStream();
                sos.write(html.getBytes());
                sos.close();
                response.flushBuffer();
                return null;
            } catch (Exception ignored) {}
        }
        if (ProfileUtils.isLogoned()) {
            return "redirect:/index";
        }
        return "redirect:/login";
    }

    @GetMapping("index")
    @ApiOperation("显示首页")
    @Log("显示首页")
    public String index(Model model) {
        Profile user = ProfileUtils.getCurrentUser();
        model.addAttribute("user", Utils.objectToJson(user));
        try {
            model.addAttribute("profilesettings", Utils.objectToJson(profileSettingsService.get(user.id)));
        } catch (Exception e) {
            model.addAttribute("profilesettings", Utils.objectToJson(new ProfileSettings()));
        }
        model.addAttribute("application", BaseSaasConfig.getApplication());
        model.addAttribute("permissions", "{}");
        model.addAttribute("roles", "{}");
        String filePath = "templates/views/custom.html";
        ClassPathResource resource = new ClassPathResource(filePath);
        if (resource.exists()) {
            model.addAttribute("HASINCLUDECUSTOMHTML","/views/custom.html");
        }
        String iconfontPath = "static/iconfont/iconfont.css";
        ClassPathResource iconfontResource = new ClassPathResource(iconfontPath);
        if (iconfontResource.exists()) {
            model.addAttribute("HASICONFONT",true);
        }
        model.addAttribute("APPID", XN_Rest.getApplication());
        return "Index";
    }

    @ApiOperation("显示index页面")
    @GetMapping("base/index")
    public Object pageIndex(Model model) {
        try {
            if (ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                model.addAttribute("systemInfo", systemInfo.get());
                return WebViews.view("baseindex");
            } else {
                String homePage = Objects.requireNonNull(CallbackUtils.invoke("getHomePage", ProfileUtils.getCurrentUser())).toString();
                if("/base/index".equals(homePage)){
                    model.addAttribute("systemInfo", systemInfo.get());
                    return WebViews.view("baseindex");
                }else {
                    return "forward:/" + homePage;
                }
            }
        } catch (Exception e){
            log.error("GetHomePage: {} ",e.getMessage());
            model.addAttribute("systemInfo", new HashMap<>());
            return WebViews.view("baseindex");
        }
    }

    @ApiOperation("显示首页布局页面")
    @GetMapping("/layout")
    public Object layout(Model model, HttpServletResponse response) {
        Profile user = ProfileUtils.getCurrentUser();
        try {
            menuService.findMenuGroups(false);
            List<Map<String, Object>> menugroup;
            ProfileSettings profileSettings = profileSettingsService.get(user.id);
            try{
                menugroup = menuService.findMenuGroups(user.id,user.type,false);
                if(!ContextUtils.isJar() && (ProfileUtils.isAdmin() || ProfileUtils.isAssistant())){
                    if(profileSettings.isDev){
                        if(!"lcdp".equals(profileSettings.menu)){
                            profileSettings.menu = "lcdp";
                            profileSettingsService.updateSettings(user.id,profileSettings);
                        }
                        menuService.findMenuGroups(true);
                        menugroup.clear();
                    } else {
                        if("lcdp".equals(profileSettings.menu)){
                            if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                                profileSettings.menu = "supplier";
                            }else if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                                profileSettings.menu = "settings";
                            }
                            profileSettingsService.updateSettings(user.id,profileSettings);
                        }
                    }
                }
            }catch (Exception e) {
                return WebViews.body(response,new WebResponse().alert(e.getMessage()));
            }
            Object managementMenuIsHidden = CallbackUtils.invoke("getManagementMenuIsHidden");
            if (Utils.isNotEmpty(managementMenuIsHidden) && (Boolean) managementMenuIsHidden) {
                menugroup = menugroup.stream().filter( v -> !Arrays.asList("management","suppliermanagement").contains(v.get("name").toString())).collect(Collectors.toList());
            }
            if (menugroup.size() == 1 && !profileSettings.isDev) {
                String selectMenu = menugroup.get(0).get("name").toString();
                if (!selectMenu.equals(profileSettings.menu)) {
                    profileSettings.menu = selectMenu;
                    Profile currentUser = ProfileUtils.getCurrentUser();
                    this.profileSettingsService.updateSettings(currentUser.id, profileSettings);
                }
            }  else if (menugroup.size() == 0 && !profileSettings.isDev && ProfileUtils.isAdmin()) {
                if (!profileSettings.menu.equals("settings")) {
                    profileSettings.menu = "settings";
                    Profile currentUser = ProfileUtils.getCurrentUser();
                    this.profileSettingsService.updateSettings(currentUser.id, profileSettings);
                }
            }
            model.addAttribute("profilesettings", profileSettings);
            model.addAttribute("menugroups", menugroup);
            model.addAttribute("isdev",!ContextUtils.isJar() && (ProfileUtils.isAssistant() || ProfileUtils.isAdmin()));
            if (ProfileUtils.isSupplier()) {
                model.addAttribute("supplierinfo", Utils.classToData(SupplierUtils.getSupplierInfo()));
            } else {
                model.addAttribute("supplierinfo", new HashMap<>());
            }
            if (profileSettings.isDev && ProfileUtils.isAdmin() && !ContextUtils.isJar()) {
               List<Object> friendlylinks = Arrays.asList(
                        new HashMap<String, String>() {{
                            put("link","/wiki/");
                            put("icon","lcdp-icon-kaifawendang");
                            put("target","_blank");
                            put("label","开发文档");
                        }},
                       new HashMap<String, String>() {{
                           put("link","/tools/");
                           put("icon","lcdp-icon-tools");
                           put("target","_blank");
                           put("label","REST工具");
                       }}
                );
                model.addAttribute("friendlylinks", friendlylinks);
            } else {
                try {
                    Object friendlyLinks = CallbackUtils.invoke("getFriendlyLinks");
                    model.addAttribute("friendlylinks", friendlyLinks);
                } catch (Exception e){
                    log.error("GetFriendlylinks: {} ",e.getMessage());
                    model.addAttribute("friendlylinks", new ArrayList<>());
                }
            }


            long  noticecount = 0;
            try {
                if (ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                    noticecount = noticesService.getSystemUnReadCount(ProfileUtils.getCurrentProfileId());
                } else if (ProfileUtils.isManager()) {
                    if (AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(),"auditor")) {
                        noticecount = noticesService.getApprovalUnReadCount(ProfileUtils.getCurrentProfileId());
                    } else {
                        noticecount = noticesService.getProfileUnReadCount(ProfileUtils.getCurrentProfileId());
                    }
                } else {
                    noticecount = noticesService.getSupplierUnReadCount(SupplierUtils.getSupplierid(),ProfileUtils.getCurrentProfileId());
                }
            }catch (Exception ignored){}
            model.addAttribute("noticecount", noticecount);

        } catch (Exception e) {
            log.error("layout Exception:{}", e.getMessage());
            model.addAttribute("profilesettings", new ProfileSettings());
            model.addAttribute("supplierinfo", new HashMap<>());
            model.addAttribute("menugroups", new ArrayList());
        }

        if (ProfileUtils.isSupplier()) {
            SupplierUtils.initPickLists();
        }
        model.addAttribute("APPID", XN_Rest.getApplication());
        return WebViews.view("layout");
    }

    @ApiOperation("开发模式切换")
    @GetMapping("/layout/changedev")
    @ResponseBody
    public Object changeDevmode() {
        Profile user = ProfileUtils.getCurrentUser();
        try {
            ProfileSettings profileSettings = profileSettingsService.get(user.id);
            profileSettings.isDev = !profileSettings.isDev;
            profileSettingsService.updateSettings(user.id,profileSettings);
        } catch (Exception e) {
            return new WebResponse().fail("切换失败");
        }
        return new WebResponse().success();
    }

    @ApiOperation("显示402页面")
    @GetMapping("/base/402")
    public String error402() {
        return WebViews.view("/error/402");
    }

    @ApiOperation("显示404页面")
    @GetMapping("/base/404")
    public String error404() {
        return WebViews.view("error/404");
    }

    @ApiOperation("显示403页面")
    @GetMapping("/base/403")
    public String error403() {
        return WebViews.view("/error/403");
    }

    @ApiOperation("显示500页面")
    @GetMapping("/base/500")
    public String error500() {
        return WebViews.view("error/500");
    }

    @ApiOperation("超级删除功能")
    @PostMapping("/superdeleteAction/ajax")
    @ResponseBody
    public Object superDelete(HttpServletRequest request) {
        try {
            baseEntityService.superDelete(request);
            return new WebResponse().code(0).refresh();
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation("用户选择弹窗")
    @PostMapping("/base/popupview/users")
    public void popupUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(ProfileUtils.isSupplier()){
            request.getRequestDispatcher("/supplierdepartments/popupview/users").forward(request, response);
        } else {
            request.getRequestDispatcher("/departments/popupview/users").forward(request, response);
        }
    }
    @ApiOperation("部门选择弹窗")
    @PostMapping("/base/popupview/trees")
    public void popupDepar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(ProfileUtils.isSupplier()){
            request.getRequestDispatcher("/supplierdepartments/popupview/trees").forward(request, response);
        } else {
            request.getRequestDispatcher("/departments/popupview/trees").forward(request, response);
        }
    }
    @ApiOperation("部门下拉选择")
    @PostMapping("/base/popupview/tree")
    @ResponseBody
    public void selectDepar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(ProfileUtils.isSupplier()){
            request.getRequestDispatcher("/supplierdepartments/popupview/tree").forward(request, response);
        } else {
            request.getRequestDispatcher("/departments/popupview/tree").forward(request, response);
        }
    }

    @ApiOperation("导出Excel功能")
    @PostMapping("/exportexcelAction/ajax")
    @SuppressWarnings("unchecked")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HashMap<String, Object> result = baseEntityService.getExportExcelData(request);
            ExcelUtils.SheetMain<String> sheetMain = new ExcelUtils.SheetMain<>();
            sheetMain.init(0, 0, Arrays.asList(15, 15, 15, 15, 15, 15, 15),
                    ExcelUtils.SheetMain.createCellStyle((short) 16, true, true, true, true),
                    ExcelUtils.SheetMain.createCellStyle((short) 11, true, true, true, true),
                    ExcelUtils.SheetMain.createCellStyle((short) 11, false, true, true, true),
                    ExcelUtils.SheetMain.createCellStyle((short) 11, false, false, false, true));
            sheetMain.setSheetName(result.get("title").toString());
            sheetMain.setSheetTitle(result.get("title").toString());
            sheetMain.setSheetHeads((List<String>) result.get("fieldnames"));
            if(!Utils.isEmpty(result.get("result"))) {
                sheetMain.setTs((List<String>)result.get("result"));
            }
            response.reset();
            //设置response的Header
            String fileName = java.net.URLEncoder.encode(result.get("title").toString(), "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            ExcelUtils.createExcelFile(xssfWorkbook, os, sheetMain);
            os.flush();
            os.close();
            xssfWorkbook.close();
        } catch (Exception e) {
            WebViews.body(response,new WebResponse().fail().message(e.getMessage()));
        }
    }

    @ApiOperation("JavaVM是否启动完成")
    @GetMapping("/base/waitjavavm")
    @ResponseBody
    public Object waitJavaVm() {
        try{
            Object vmStarted = RedisUtils.get("JAVA_VM_START_COMPLETE");
            if(!Utils.isEmpty(vmStarted)){
                RedisUtils.del("JAVA_VM_START_COMPLETE");
                return new WebResponse().success();
            }
        }catch (Exception ignored){ }
        return new WebResponse().fail();
    }
}
