package com.qnkj.core.base.controller;

import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.modules.lcdp.developments.service.IDevelopmentService;
import com.qnkj.core.base.services.IInitService;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * create by 徐雁
 * @author 徐雁
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("base")
@Controller("Base-Init")
@Api(tags = "框架：初始化系统")
public class InitController {
    private final IInitService initService;
    private final IDevelopmentService developmentService;

    @Value("${init.password}")
    private String initpassword;

    @GetMapping("/initpt")
    @ApiOperation("初始所有数据")
    public Object index(Model model) {
        model.addAttribute("TYPE","pt");
        model.addAttribute("DEV", !ContextUtils.isJar());
        return WebViews.view("initpt");
    }

    @GetMapping("/initdata")
    @ApiOperation("初始所有数据")
    public Object initData(Model model) {
        model.addAttribute("TYPE","module");
        model.addAttribute("DEV", !ContextUtils.isJar());
        model.addAttribute("SERVICECLASS", developmentService.getConfigModule());
        return WebViews.view("initpt");
    }

    @PostMapping("/init/start")
    @ApiOperation("初始所有数据")
    @ResponseBody
    public Object startAll(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if(ContextUtils.isJar()) {
            if(!initpassword.equals(httpRequest.get("password"))){
                return new WebResponse().fail("密码错误，禁止初始化");
            }
        }
        try {
            if (!Utils.isEmpty(httpRequest.get("modulename"))) {
                initService.initdata(httpRequest.get("modulename").toString());
            } else {
                initService.initpt();
            }
            return new WebResponse().success();
        }catch (WebException e) {
            return new WebResponse().fail(e.getMessage());
        }catch (Exception e) {
            return new WebResponse().fail("初始化失败，请联系管理员。。。");
        }
    }

    @PostMapping("/init/clear")
    @ApiOperation("初始所有数据")
    @ResponseBody
    public Object clearInit() throws Exception {
        initService.clearStatus();
        return new WebResponse().success();
    }

    @GetMapping("/init/getstatus")
    @ApiOperation("获取初始化状态")
    @ResponseBody
    public Object initStatus() throws Exception {
        return initService.getStatus();
    }

}
