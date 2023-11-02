package com.qnkj.core.base.modules.baseservices.feedback.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.baseservices.feedback.services.IfeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-12-17
 */

@Slf4j
@Validated
@Controller("feedback")
@RequiredArgsConstructor
@Api(tags = "低代码演示：申请反馈")
@Scope("prototype")
@RequestMapping("feedback")
public class feedbackController {
    private final IfeedbackService feedbackService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }


    @ApiOperation(value = "保存申请反馈接口")
    @PostMapping("/submit")
    @ResponseBody
    public Object submit(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            feedbackService.saveFeedBack(httpRequest,viewEntitys);
            return new WebResponse().refresh().success(0, "保存成功");
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

}


