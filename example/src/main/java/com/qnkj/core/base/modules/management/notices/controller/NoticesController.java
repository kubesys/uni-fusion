package com.qnkj.core.base.modules.management.notices.controller;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.management.notices.services.INoticesService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

/**
 * create by Auto Generator
 * create date 2020-11-17
 */

@Validated
@Controller("basenotices")
@RequiredArgsConstructor
@Api(tags = "管理平台：通知")
@Scope("prototype")
@RequestMapping("notices")
public class NoticesController {
    private final INoticesService noticesService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }


}
