package com.qnkj.core.base.modules.supplier.Supplieroperationlog.controller;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.supplier.Supplieroperationlog.services.IsupplieroperationlogService;
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
@Controller("supplieroperationlog")
@RequiredArgsConstructor
@Api(tags = "企业管理：操作日志")
@Scope("prototype")
@RequestMapping("supplieroperationlog")
public class supplieroperationlogController {
    private final IsupplieroperationlogService supplieroperationlogService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

}
