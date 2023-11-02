package com.qnkj.clouds.modules.VmSnapshots.controller;

import com.qnkj.clouds.modules.VmSnapshots.services.IVmSnapshotsService;
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
@Controller("VmSnapshots")
@RequiredArgsConstructor
@Api(tags = "低代码演示：快照")
@Scope("prototype")
@RequestMapping("VmSnapshots")
public class VmSnapshotsController {
    private final IVmSnapshotsService vmsnapshotsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

}
