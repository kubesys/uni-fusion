package com.qnkj.clouds.modules.VmImageServers.controller;

import com.qnkj.clouds.modules.VmImageServers.services.IVmImageServersService;
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
@Controller("VmImageServers")
@RequiredArgsConstructor
@Api(tags = "低代码演示：镜像服务器")
@Scope("prototype")
@RequestMapping("VmImageServers")
public class VmImageServersController {
    private final IVmImageServersService vmimageserversService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

}
