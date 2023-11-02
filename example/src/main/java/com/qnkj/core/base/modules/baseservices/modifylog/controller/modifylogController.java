package com.qnkj.core.base.modules.baseservices.modifylog.controller;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.baseservices.modifylog.services.ImodifylogService;
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
 * create date 2021-09-18
 */

@Slf4j
@Validated
@Controller("modifylog")
@RequiredArgsConstructor
@Api(tags = "系统专属模块演示：编辑日志")
@Scope("prototype")
@RequestMapping("modifylog")
public class modifylogController {
    private final ImodifylogService modifylogService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

}
