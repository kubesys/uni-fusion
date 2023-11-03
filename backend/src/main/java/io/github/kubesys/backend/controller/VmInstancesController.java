package io.github.kubesys.backend.controller;


import io.github.kubesys.backend.services.IVmInstancesService;

import io.github.kubesys.backend.utils.WebViews;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author bingshuai@nj.iscas.ac.cn
 * @since 11.01
 */

@Slf4j
@Validated
@Controller("VmInstances")
@RequiredArgsConstructor
@Api(tags = "低代码演示：云主机")
@Scope("prototype")
@RequestMapping("VmInstance")
public class VmInstancesController {
    private final IVmInstancesService vminstancesService;
//    private BaseEntityUtils viewEntitys = null;

//    @PostConstruct
//    public void init() { viewEntitys = BaseEntityUtils.init(this); }

    @ApiOperation("显示控制台")
    @GetMapping("/viewNoVnc")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "record", value = "记录ID", required = true, dataType = "String", paramType = "query")
//    })
    public Object viewAddProduct( Model model) {
        try {
            Integer port = vminstancesService.startWebsockifyServer();
            model.addAttribute("port", port);
            return WebViews.view("/VmInstances/viewNoVnc");
        }catch (Exception e) {
            return e;
        }
    }

}
