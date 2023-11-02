package com.qnkj.core.base.modules.settings.backupmanage.controller;

import com.github.restapi.XN_Backup;
import com.qnkj.common.entitys.PickListEntity;
import com.qnkj.common.entitys.TabField;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.backupmanage.entity.Backups;
import com.qnkj.core.base.modules.settings.backupmanage.service.IBackupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@Controller("Settings-Backupmanage")
@RequiredArgsConstructor
@Api(tags = "系统设置：备份管理")
@Scope("prototype")
@RequestMapping("backupmanage")
public class BackupController {
    private final IBackupService backupService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "备份列表请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("TABNAME", "备份管理");
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "备份列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String, Object> list = backupService.getListViewEntity(request, viewEntitys, Backups.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "新建备份")
    @PostMapping("/editviewAction/ajax")
    @ResponseBody
    public Object addBackup(HttpServletRequest request) {
        try {
            XN_Backup.create().save("backup");
            return new WebResponse().refresh().success(0, "备份完成");
        } catch (Exception e) {
            return new WebResponse().fail("备份失败");
        }
    }

    @ApiOperation(value = "删除备份")
    @PostMapping("/deleteAction/ajax")
    @ResponseBody
    public Object deleteBackup(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            if (!Utils.isEmpty(httpRequest.get("ids"))) {
                Object ids = httpRequest.get("ids");
                if (ids instanceof String) {
                    ids = Arrays.asList(((String) ids).split(","));
                }
                if (ids instanceof List) {
                    for(Object item : (List<?>)ids) {
                        XN_Backup.delete(item.toString(), "backup");
                    }
                }
                return new WebResponse().refresh().success(0, "删除完成");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new WebResponse().fail("删除失败");
    }

    @ApiOperation("显示备份策略")
    @PostMapping("/backuppolicyAction/ajax")
    public Object viewBackupPolicyAction(HttpServletRequest request, Model model) {
        Map<String, Object> Request = Utils.getRequestQuery(request);
        List<TabField> fields = Arrays.asList(
                new TabField().fieldname("database").fieldlabel("数据库").uitype(19).typeofdata("V~M").picklist("database"),
                new TabField().fieldname("backupmode").fieldlabel("备份方式").uitype(13).typeofdata("V~M").picklist("backupmode").defaultvalue("1"),
                new TabField().fieldname("backuppolicy").fieldlabel("备份策略").uitype(13).typeofdata("V~M").picklist("backuppolicy").defaultvalue("1"),
                new TabField().fieldname("path").fieldlabel("文件路径").typeofdata("V~M").defaultvalue(""),
                new TabField().fieldname("backupmethod").fieldlabel("同步到本地").uitype(13).typeofdata("V~M").picklist("yesno").defaultvalue("yes")
        );
        Map<String,Object> picklists = new HashedMap();
        picklists.put("database",getDatabase());
        picklists.put("backupmode",getBackupMode());
        picklists.put("backuppolicy",getBackupPolicy());
        return viewEntitys.popupCustomEditView(fields, picklists, model,"");
    }
    public List<Object> getDatabase() {
        List<Object> lists = new LinkedList<>();
        lists.add(new PickListEntity().strval("1").intval(1).label("数据资源池").toList());
        lists.add(new PickListEntity().strval("2").intval(2).label("评估系统").toList());
        return lists;
    }
    public List<Object> getBackupPolicy() {
        List<Object> lists = new LinkedList<>();
        lists.add(new PickListEntity().strval("1").intval(1).label("天").toList());
        lists.add(new PickListEntity().strval("2").intval(2).label("周").toList());
        lists.add(new PickListEntity().strval("3").intval(3).label("月").toList());
        return lists;
    }
    public List<Object> getBackupMode() {
        List<Object> lists = new LinkedList<>();
        lists.add(new PickListEntity().strval("1").intval(1).label("全局").toList());
        lists.add(new PickListEntity().strval("2").intval(2).label("增量").toList());
        return lists;
    }

    @ApiOperation("显示备份还原")
    @PostMapping("/restorebackupAction/ajax")
    public Object viewRestoreBackupAction(HttpServletRequest request, Model model) {
        Map<String, Object> Request = Utils.getRequestQuery(request);
        List<TabField> fields = Arrays.asList(
                new TabField().fieldname("database").fieldlabel("数据库").uitype(19).typeofdata("V~M").picklist("database"),
                new TabField().fieldname("path").fieldlabel("文件路径").typeofdata("V~M").defaultvalue(""),
                new TabField().fieldname("passwd").fieldlabel("安全密钥").uitype(3).typeofdata("V~M").defaultvalue("")
        );
        Map<String,Object> picklists = new HashedMap();
        picklists.put("database",getDatabase());
        model.addAttribute("BUTTONLABEL", "开始还原");
        return viewEntitys.popupCustomEditView(fields, picklists, model,Request.get("ids").toString());
    }
}
