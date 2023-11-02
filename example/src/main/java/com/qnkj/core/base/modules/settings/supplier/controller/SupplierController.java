package com.qnkj.core.base.modules.settings.supplier.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;
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
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2020-11-23
 */

@Validated
@Controller("supplier")
@RequiredArgsConstructor
@Api(tags = "系统设置：子平台")
@Scope("prototype")
@RequestMapping("supplier")
public class SupplierController {
    private final ISupplierService supplierService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "用户弹窗请求接口")
    @PostMapping("/popupview")
    public Object popupView(HttpServletRequest request, Model model) {
        model.addAttribute("ENTITYURL", "supplier/listentity/ajax");
        model.addAttribute("ISPOPUP",true);
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        return viewEntitys.popupView(httpRequest, model);
    }

}
