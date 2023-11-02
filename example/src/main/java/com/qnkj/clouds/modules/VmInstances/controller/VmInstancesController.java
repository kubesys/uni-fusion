package com.qnkj.clouds.modules.VmInstances.controller;

import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.clouds.modules.VmInstances.services.IVmInstancesService;
import com.qnkj.common.entitys.PickListEntity;
import com.qnkj.common.entitys.TabField;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.webconfigs.WebViews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */

@Slf4j
@Validated
@Controller("VmInstances")
@RequiredArgsConstructor
@Api(tags = "低代码演示：云主机")
@Scope("prototype")
@RequestMapping("VmInstance")
public class VmInstancesController {
    private final IVmInstancesService vminstancesService;
    private BaseEntityUtils viewEntitys = null;

//    @PostConstruct
//    public void init() { viewEntitys = BaseEntityUtils.init(this); }

    @ApiOperation("显示控制台")
    @GetMapping("/viewNoVnc")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "record", value = "记录ID", required = true, dataType = "String", paramType = "query")
//    })
    public Object viewAddProduct( HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Integer port = vminstancesService.startWebsockifyServer();
            model.addAttribute("port", port);
            log.info("port是什么: {}", port);
            return WebViews.view("/VmInstances/viewNoVnc");
        }catch (Exception e) {
            return WebViews.body(response,new WebResponse().alert().message("无法获得控制台端口"));
        }
    }

    @ApiOperation("启动虚拟机")
    @PostMapping("/startVm/ajax")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "record", value = "记录ID", required = true, dataType = "String", paramType = "query")
    })
    public Object startVm(@RequestParam(required=true,name="record") String record, Model model) {
        try {
            vminstancesService.startVm(record);
            return new WebResponse().success("启动虚拟机成功");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation("重启虚拟机")
    @PostMapping("/restartVm/ajax")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "record", value = "记录ID", required = true, dataType = "String", paramType = "query")
    })
    public Object restartVm(@RequestParam(required=true,name="record") String record, Model model) {
        try {
            vminstancesService.restartVm(record);
            return new WebResponse().success("重启虚拟机成功");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }


    @ApiOperation("停止虚拟机")
    @PostMapping("/stopVm/ajax")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "record", value = "记录ID", required = true, dataType = "String", paramType = "query")
    })
    public Object stopVm(@RequestParam(required=true,name="record") String record, Model model) {
        try {
            vminstancesService.stopVm(record);
            return new WebResponse().success("停止虚拟机成功");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation("关闭电源")
    @PostMapping("/forceStopVm/ajax")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "record", value = "记录ID", required = true, dataType = "String", paramType = "query")
    })
    public Object forceStopVm(@RequestParam(required=true,name="record") String record, Model model) {
        try {
            vminstancesService.forceStopVm(record);
            return new WebResponse().success("虚拟机关闭电源成功");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "显示创建镜像对话框")
    @GetMapping("/viewCreateDiskImage")
    public Object viewCreateDiskImage(HttpServletRequest request, Model model) {
        Map<String, Object> Request = Utils.getRequestQuery(request);
        log.info("Request : {}",Request);
        List<TabField> fields = Arrays.asList(
                new TabField().fieldname("name").fieldlabel("镜像名称").typeofdata("NS~M").issort(true),
                new TabField().fieldname("describe").fieldlabel("简介").uitype(23).issort(true),
                new TabField().fieldname("targetpool").fieldlabel("主存储").uitype(19).typeofdata("V~M").issort(true)
        );
        Map<String,Object> picklists = new HashedMap();
        picklists.put("targetpool",getPicklist());
        return viewEntitys.popupCustomEditView(fields, picklists, model,Request.get("record").toString());
    }
    /**
     * 提供预置的字典信息
     **/
    private List<Object> getPicklist() {
        List<Object> lists = new ArrayList<>();
        try{
            List<Object> vm_pools = XN_Query.create("Content").tag("vm_pools")
                    .filter("type","eic","vm_pools")
                    .filter("my.deleted","=","0")
                    .filter("my.status","=","Active")
                    .filter("my.content","=","vmdi")
                    .end(-1).execute();
            for(Object item: vm_pools){
                Content content = (Content)item;
                PickListEntity entity = new PickListEntity().strval(content.id).intval(Integer.parseInt(content.id)).label(content.my.get("name").toString());
                lists.add(Arrays.asList(entity.strval,entity.label));
            }
        }catch (Exception ignored){ }
        return lists;
    }

    @ApiOperation(value = "保存创建镜像接口")
    @PostMapping("/viewCreateDiskImage/save")
    @ResponseBody
    public Object saveCreateDiskImage(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> Request = Utils.getRequestQuery(request);
            log.info("saveCreateDiskImage : {}",Request);
            /**
             *  保存的具体实现
             **/
            vminstancesService.createDiskImageFromDisk(Request);
            return new WebResponse().close().success(0,"创建镜像成功");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }



    @ApiOperation("生成UUID")
    @PostMapping("/makeUUid/ajax")
    @ResponseBody
    public Object makeUUid() {
        try {
            String uuid = UUID.randomUUID().toString();
            return new WebResponse().success("ok").data(uuid);
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

}
