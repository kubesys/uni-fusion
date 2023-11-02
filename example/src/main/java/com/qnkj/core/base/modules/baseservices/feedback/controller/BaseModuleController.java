package com.qnkj.core.base.modules.baseservices.feedback.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.baseservices.feedback.entitys.feedback;
import com.qnkj.core.base.modules.baseservices.feedback.services.IfeedbackService;
import com.qnkj.core.webconfigs.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
@Controller("base-feedback")
@RequiredArgsConstructor
@Api(tags = "低代码演示：申请反馈")
@Scope("prototype")
@RequestMapping("feedback")
public class BaseModuleController {
    private final IfeedbackService feedbackService;
    private BaseEntityUtils viewEntitys = null;
    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

    @ApiOperation(value = "列表请求接口")
    @GetMapping("/listview")
    @Log("显示申请反馈列表视图")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = feedbackService.getListViewEntity(request,viewEntitys, feedback.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "保存请求接口")
    @PostMapping("/save")
    @ResponseBody
    @Log("保存申请反馈")
    public Object editViewSave(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            viewEntitys.formatRequest(httpRequest);
            feedbackService.save(httpRequest,viewEntitys,feedback.class);
            String recordId = httpRequest.getOrDefault("record","").toString();
            if(Boolean.TRUE.equals(viewEntitys.getWorkflowStatus().isDefineflow()) && (viewEntitys.getWorkflowStatus().isDealwith() || !viewEntitys.getWorkflowStatus().isStartflow())){
                return new WebResponse().refresh().record(recordId).success(0,"保存完成");
            } else {
                return new WebResponse().close().success(0,"保存完成");
            }
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }
}
