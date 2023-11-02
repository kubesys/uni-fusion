package com.qnkj.clouds.modules.VmSecurityGroups.controller;

import com.qnkj.clouds.modules.VmSecurityGroups.services.IVmSecurityGroupsService;
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
 * create date 2023-07-09
 * @author Auto Generator
 */

@Slf4j
@Validated
@Controller("VmSecurityGroups")
@RequiredArgsConstructor
@Api(tags = "低代码演示：安全")
@Scope("prototype")
@RequestMapping("VmSecurityGroups")
public class VmSecurityGroupsController {
    private final IVmSecurityGroupsService vmsecuritygroupsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

}
