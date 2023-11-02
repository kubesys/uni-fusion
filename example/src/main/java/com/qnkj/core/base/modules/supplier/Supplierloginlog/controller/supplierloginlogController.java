package com.qnkj.core.base.modules.supplier.Supplierloginlog.controller;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.supplier.Supplierloginlog.services.IsupplierloginlogService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

/**
 * create by Auto Generator
 * create date 2021-05-23
 */

@Validated
@Controller("supplierloginlog")
@RequiredArgsConstructor
@Api(tags = "企业管理：登录日志")
@Scope("prototype")
@RequestMapping("supplierloginlog")
public class supplierloginlogController {
    private final IsupplierloginlogService supplierloginlogService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

}
