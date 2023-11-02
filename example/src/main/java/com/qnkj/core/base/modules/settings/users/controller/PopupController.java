package com.qnkj.core.base.modules.settings.users.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Validated
@Controller("Settings-Users-Popup")
@RequiredArgsConstructor
@Scope("prototype")
@Api(tags = "系统设置：用户管理")
@RequestMapping("users")
public class PopupController {
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init(){
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "列表弹窗请求接口")
    @PostMapping("/popupview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("ENTITYURL", "users/listentity/ajax");
        model.addAttribute("ISPOPUP",true);
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        return viewEntitys.popupView(httpRequest, model);
    }

    @ApiOperation(value = "用户弹窗请求接口")
    @PostMapping("/popupview/users")
    public void popuUsers(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            request.getRequestDispatcher("/departments/popupview/users").forward(request, response);
        } catch (ServletException | IOException ignored) {}
    }

}
