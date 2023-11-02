package com.qnkj.core.base.controller;

import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.plugins.compiler.CompilerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * create by oldhnd
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("base")
@Controller("Base-Compiler")
@Api(tags = "框架：动态编译")
public class CompilerController {

    @PostMapping("/compile")
    @ApiOperation("编译")
    @ResponseBody
    public Object compile() throws Exception {
        try {
            List<String> files = CompilerUtils.autoCompiler();
            return new WebResponse().success("编译成功").data(files);
        } catch (Exception e) {
            return new WebResponse().fail().message(e.getMessage());
        }
    }

    @PostMapping("/triggerReload")
    @ApiOperation("Java虚拟机重启")
    @ResponseBody
    public Object triggerReload() {
        try {
            Restarter.getInstance().restart();
            return new WebResponse().success();
        } catch (Exception e) {
            return new WebResponse().fail().message(e.getMessage());
        }
    }

}
