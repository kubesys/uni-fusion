package com.qnkj.clouds.modules.VmZones.controller;

import com.qnkj.clouds.modules.VmZones.services.IVmZonesService;
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
@Controller("VmZones")
@RequiredArgsConstructor
@Api(tags = "低代码演示：区域")
@Scope("prototype")
@RequestMapping("VmZones")
public class VmZonesController {
    private final IVmZonesService vmzonesService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

}
