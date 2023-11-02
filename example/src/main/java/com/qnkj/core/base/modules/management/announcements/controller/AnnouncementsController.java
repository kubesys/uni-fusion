package com.qnkj.core.base.modules.management.announcements.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.management.announcements.services.IAnnouncementsService;
import com.qnkj.core.webconfigs.WebViews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2020-11-17
 */

@Validated
@Controller("announcements")
@RequiredArgsConstructor
@Api(tags = "管理平台：公告")
@Scope("prototype")
@RequestMapping("announcements")
public class AnnouncementsController {
    private final IAnnouncementsService announcementsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }


    @ApiOperation(value = "公告置顶请求接口")
    @PostMapping("/settopAction/ajax")
    public Object settopAction(HttpServletRequest request, Model model) {
        try{
            String body = "<div style='height:60px;padding-top: 25px;'>" +
                    "<div class='layui-block'>\n" +
                    "    <label class='layui-form-label'>是否置顶</label>\n" +
                    "    <div class='layui-input-block'>\n" +
                    "        <input type='radio' name='istop' value='0' verify_value='on' title='否'/>\n" +
                    "        <input type='radio' checked name='istop' value='1' verify_value='off' title='是'/>\n" +
                    "    </div>\n" +
                    "</div></div>";
            String actions = "<button type='submit' class='layui-btn layui-btn-sm "+viewEntitys.getTabName()+"_popup_form_submit'>确定</button>";
            String script = "" +
                    "$('#"+viewEntitys.getTabName()+"_PopDialog').find('.layui-card-footer > ."+viewEntitys.getTabName()+"_popup_form_submit').on('click',function () {\n" +
                    "            if(typeof form.PopupDialogCallback[layerindex] === 'function'){\n" +
                    "                var data = form.val('"+viewEntitys.getTabName()+"_PopDialog')\n" +
                    "                if(data.istop) {" +
                    "                   form.PopupDialogCallback[layerindex]({type:'submit',data:data})\n" +
                    "                } else {" +
                    "                   layui.layer.msg('请选择是否置顶!', {icon: 5,anim: 6,time:1500});" +
                    "                }" +
                    "            }\n" +
                    "        })";
            model.addAttribute("MODULE",viewEntitys.getTabName());
            model.addAttribute("DIALOGBODY",body);
            model.addAttribute("ACTIONS",actions);
            model.addAttribute("SCRIPT",script);
            return WebViews.view("PopupDialog");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "公告置顶保存请求接口")
    @PostMapping("/settopAction/save")
    @ResponseBody
    public Object setTop(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            announcementsService.setTop(httpRequest,viewEntitys);
            return new WebResponse().success(0,"设置置顶成功！").put("refresh",true);
        }catch (Exception e) {
            return new WebResponse().fail("设置置顶失败，请稍候再试。。。");
        }
    }

    @ApiOperation(value = "公告撤消发布请求接口")
    @PostMapping("/cancelreleaseAction/ajax")
    @ResponseBody
    public Object cancelRelease(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            announcementsService.cancelRelease(httpRequest,viewEntitys);
            return new WebResponse().success(0,"撤消发布成功！").put("refresh",true);
        }catch (Exception e) {
            return new WebResponse().fail("撤消发布失败，请稍候再试。。。");
        }
    }

}
