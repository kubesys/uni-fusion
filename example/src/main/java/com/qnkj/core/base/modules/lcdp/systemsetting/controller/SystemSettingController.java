package com.qnkj.core.base.modules.lcdp.systemsetting.controller;

import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.lcdp.systemsetting.service.ISystemSettingService;
import com.qnkj.core.webconfigs.WebViews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2021/3/5
 */

@Validated
@Controller("lcdp-SystemSetting")
@RequiredArgsConstructor
@Scope("prototype")
@Api(tags = "低代码开发：系统参数")
@RequestMapping("/systemsetting")
public class SystemSettingController {
    private BaseEntityUtils viewEntitys = null;
    private final ISystemSettingService settingService;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "页面请求接口")
    @GetMapping("/listview")
    public Object listView(Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return WebViews.view("modules/settings/systemsetting/ListView");
    }

    @ApiOperation(value = "页面请求接口")
    @PostMapping("/editview")
    public Object editView(HttpServletRequest request,Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if(!Utils.isEmpty(httpRequest.get("domain"))) {
            model.addAttribute("MODULE", viewEntitys.getModuleName());
            model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
            model.addAttribute("SETTINGS", settingService.getSetting(httpRequest.get("domain").toString()));
            model.addAttribute("PLATFORMAUTHORIZES",Utils.objectToJson(settingService.getPlatformAuthorizes(httpRequest.get("domain").toString())));
            model.addAttribute("USERAUTHORIZES",Utils.objectToJson(settingService.getUserAuthorizes(httpRequest.get("domain").toString())));
            model.addAttribute("DOMAIN",httpRequest.get("domain"));
            model.addAttribute("TIMESTAMP", DateTimeUtils.gettimeStamp());
            return WebViews.view("modules/settings/systemsetting/EditView");
        } else {
            return WebViews.view("error/404");
        }
    }

    @ApiOperation(value = "获取域名树")
    @PostMapping("/tree")
    @ResponseBody
    public Object getTree() {
        return settingService.getTree();
    }

    @ApiOperation(value = "页面请求接口")
    @PostMapping("/addview")
    public Object addView(Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return WebViews.view("modules/settings/systemsetting/AddView");
    }

    @ApiOperation(value = "页面保存接口")
    @PostMapping("/save")
    @ResponseBody
    public Object save(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            settingService.save(httpRequest);
            return new WebResponse().refresh().success(0,"保存完成！");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "页面删除接口")
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            String host = request.getServerName();
            String quest = httpRequest.getOrDefault("domain","").toString();
            if(!Utils.isEmpty(quest)){
                if (quest.equals(host) || "localhost".equals(quest)){
                    return new WebResponse().fail("不能删除当前域名");
                }
                settingService.delete(httpRequest);
                return new WebResponse().refresh().success(0,"删除完成！");
            } else {
                return new WebResponse().fail("不能删除当前域名");
            }
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

}
