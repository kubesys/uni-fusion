package com.qnkj.clouds.modules.NoVNC.controller;


import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.SaaSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * create by Auto Generator
 * create date 2021-06-02
 */

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@Api(tags = "NoVNC")
@Scope("prototype")
public class NoVncController {

    private String port;

    @Value("${server.port:8081}")
    private void setPort(String value) {
        port = value;
    }

    @ApiOperation(value = "novnc")
    @GetMapping("/novnc/")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "port", value = "端口", required = true, dataType = "String", paramType = "query")
    })
    public Object novnc(@RequestParam(required=true,name="port") String port,Model model) {
        String filePath = "templates/vnc.html";
        ClassPathResource resource = new ClassPathResource(filePath);
        if (resource.exists()) {
            try {
                model.addAttribute("port", port);
            }catch (Exception ignored) {}

            model.addAttribute("port", port);
            return "vnc";
        }
        return null;
    }





}
