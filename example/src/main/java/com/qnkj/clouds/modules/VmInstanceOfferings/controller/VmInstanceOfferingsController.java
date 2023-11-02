package com.qnkj.clouds.modules.VmInstanceOfferings.controller;

import com.qnkj.clouds.modules.VmInstanceOfferings.services.IVmInstanceOfferingsService;
import com.qnkj.core.base.BaseEntityUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */

@Slf4j
@Validated
@Controller("VmInstanceOfferings")
@RequiredArgsConstructor
@Api(tags = "低代码演示：计算规格")
@Scope("prototype")
@RequestMapping("VmInstanceOfferings")
public class VmInstanceOfferingsController {
    private final IVmInstanceOfferingsService vminstanceofferingsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

}
