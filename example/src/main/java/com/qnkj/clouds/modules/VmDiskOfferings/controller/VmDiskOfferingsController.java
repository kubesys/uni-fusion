package com.qnkj.clouds.modules.VmDiskOfferings.controller;

import com.qnkj.clouds.modules.VmDiskOfferings.services.IVmDiskOfferingsService;
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
@Controller("VmDiskOfferings")
@RequiredArgsConstructor
@Api(tags = "低代码演示：云盘规格")
@Scope("prototype")
@RequestMapping("VmDiskOfferings")
public class VmDiskOfferingsController {
    private final IVmDiskOfferingsService vmdiskofferingsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

}
