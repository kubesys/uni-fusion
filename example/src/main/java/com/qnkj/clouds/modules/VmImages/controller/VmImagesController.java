package com.qnkj.clouds.modules.VmImages.controller;

import com.qnkj.clouds.modules.VmImageServers.services.IVmImageServersService;
import com.qnkj.clouds.modules.VmImages.services.IVmImagesService;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.webconfigs.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */

@Slf4j
@Validated
@Controller("VmImages")
@RequiredArgsConstructor
@Api(tags = "低代码演示：镜像")
@Scope("prototype")
@RequestMapping("VmImages")
public class VmImagesController {
    private final IVmImagesService vmimagesService;
    private final IVmImageServersService vmimageserversService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }



    @ApiOperation(value = "获得ISO文件列表")
    @GetMapping("/getIsoFileLists")
    @Log("获得ISO文件列表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "record", value = "因子ID", required = true, dataType = "String", paramType = "query")
    })
    public Object getIsoFileLists(@RequestParam(required=true,name="record") String record, HttpServletRequest request) {
        try {
            return new WebResponse().success("ok").data(vmimageserversService.getIsoFileLists(record));
        }catch (Exception ex) {
            return new WebResponse().fail(ex.getMessage());
        }
    }

}
