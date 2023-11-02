package com.qnkj.core.base.modules.settings.visitors.controller;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.visitors.services.IvisitorsService;
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
 * create date 2023-05-31
 * @author Auto Generator
 */

@Slf4j
@Validated
@Controller("html/views/visitors")
@RequiredArgsConstructor
@Api(tags = "系统设置：开放授权")
@Scope("prototype")
@RequestMapping("visitors")
public class visitorsController {
    private final IvisitorsService visitorsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

}
