package com.qnkj.core.base.modules.baseservices.ask.controller;

import com.qnkj.common.entitys.TabField;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.baseservices.ask.entitys.askMessage;
import com.qnkj.core.base.modules.baseservices.ask.services.IaskService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.WebViews;
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
import java.util.*;

/**
 * create by Auto Generator
 * create date 2021-08-18
 */
@Slf4j
@Validated
@Controller("ask")
@RequiredArgsConstructor
@Api(tags = "内置：在线咨询")
@Scope("prototype")
@RequestMapping("ask")
public class askController {
    private final IaskService askService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

    /**
     * 回复按钮示例代码，请添加相关业务代码后，自行转移到askController文件
     *
     * */
    @ApiOperation(value = "回复按钮请求接口")
    @PostMapping("/replyAction/ajax")
    @Log("回复按钮请求")
    public Object replyAction(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if (httpRequest.containsKey("ids")) {
            String record = httpRequest.get("ids").toString();
            List<TabField> fields = Collections.singletonList(
                    new TabField().fieldname("replybody").fieldlabel("回复内容").uitype(23).issort(true)
            );
            Map<String,Object> picklists = new HashMap<>(1);
            return viewEntitys.popupCustomEditView(fields, picklists, model,record);
        } else {
            return WebViews.view("error/403");
        }

    }

    @ApiOperation(value = "保存回复接口")
    @PostMapping("/replyAction/save")
    @ResponseBody
    @Log("保存回复")
    public Object saveReplyAction(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            log.info("saveReplyAction : {}",httpRequest);
            if (httpRequest.containsKey("record") && httpRequest.containsKey("replybody")) {
                askService.reply(httpRequest.get("record").toString(),httpRequest.get("replybody").toString());
                return new WebResponse().close().success(0,"回复保存成功");
            }
            throw new Exception("回复保存失败");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    /**
     * 消息记录按钮示例代码，请添加相关业务代码后，自行转移到askController文件
     **/
    @ApiOperation(value = "消息记录页面接口")
    @GetMapping("/askmessageAction/ajax")
    @Log("消息记录页面")
    public Object askMessageAction(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        if(!Utils.isEmpty(httpRequest.get("record"))){
            model.addAttribute("RECORD", httpRequest.get("record").toString());
            Map<String,Object> configs = new HashMap<>(1);
            configs.put("height", 300);
            configs.put("size", "");
            configs.put("haspage", true);
            return viewEntitys.listCustomView(getAskMessageConfig(),configs,request, model);
        } else {
            return WebViews.view("error/403");
        }

    }

    @ApiOperation(value = "消息记录列表记录")
    @GetMapping("/askmessageAction/list")
    @ResponseBody
    public Object askMessageList(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if(!Utils.isEmpty(httpRequest.get("record"))){
            Map<String, Object> listEntitys = askService.getAskMessage(httpRequest);
            return viewEntitys.listCustomEntity(getAskMessageConfig(),listEntitys, askMessage.class);
        }
        Map<String,Object> info = new HashMap<>(1);
        info.put("code", 0);
        info.put("msg", "");
        info.put("count", 0);
        info.put("data", new ArrayList<>(1));
        return info;
    }

    private List<TabField> getAskMessageConfig() {
        return Arrays.asList(
                new TabField().fieldname("from").fieldlabel("发送人").uitype(25).issort(true).listwidth("120").align("center"),
                new TabField().fieldname("body").fieldlabel("内容"),
                new TabField().fieldname("isread").fieldlabel("是否签收").uitype(19).picklist("yesno").issort(true).listwidth("120").align("center"),
                new TabField().fieldname("issign").fieldlabel("是否已读").uitype(19).picklist("yesno").issort(true).listwidth("120").align("center"),
                new TabField().fieldname("readtime").fieldlabel("阅读时间").listwidth("180").align("center").issort(true),
                new TabField().fieldname("published").fieldlabel("发送时间").listwidth("180").align("center").issort(true)
        );
    }


    @ApiOperation(value = "查询全部消息记录")
    @GetMapping("/getAskMessages")
    @ResponseBody
    public Object getAskMessages(HttpServletRequest request) {
        String profileid = ProfileUtils.getCurrentProfileId();
        if(!Utils.isEmpty(profileid)){
            List<Object> listEntitys = askService.getAllAskMessage(profileid);
            return new WebResponse().data(listEntitys).success(200,"ok");
        }
        return new WebResponse().fail("没有用户信息");
    }

}
