package com.qnkj.core.base.modules.settings.importdata.controller;

import com.alibaba.fastjson.JSONArray;
import com.qnkj.common.entitys.Module;
import com.qnkj.common.entitys.TabField;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.modules.lcdp.formdesign.service.IFormdesignService;
import com.qnkj.core.base.modules.settings.departments.service.IDepartmentsService;
import com.qnkj.core.base.modules.settings.importdata.Util.CommonUtil;
import com.qnkj.core.base.modules.settings.importdata.Util.SortObject;
import com.qnkj.core.base.modules.settings.importdata.services.IImportdataSetService;
import com.qnkj.core.base.modules.settings.importdata.services.impl.MyThread;
import com.qnkj.core.utils.PickListUtils;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * create by Auto Generator
 * create date 2021-09-02
 */
@Slf4j
@Validated
@Controller("importdataSet")
@RequiredArgsConstructor
@Api(tags = "框架：导入功能")
@Scope("prototype")
@RequestMapping("importdataSet")
public class ImportdataController {
    @Autowired
    private IImportdataSetService importdataSetService;
    @Autowired
    private IFormdesignService formdesignService;
    @Autowired
    private IDepartmentsService departmentsService;

    /**
     * 调入配置内容设置及已设置内容呈现
     **/
    @ApiOperation(value = "导入配置按钮请求接口")
    @PostMapping("/importdataAction/ajax")
    @Log("导入配置按钮请求")
    public Object importdataAction(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            if (Utils.isNotEmpty(httpRequest.get("record"))) {
                String record = httpRequest.get("record").toString();
                Object formDesigns = formdesignService.getDesignInfo(httpRequest);
                if(!Utils.isEmpty(formDesigns)){
                    Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                    if(!Utils.isEmpty(module.Fields) && !module.Fields.isEmpty()) {
                        List<?> linkLst = module.OutsideLinks;
                        List<TabField> fields = module.Fields;
                        List<TabField> newFields = fields.stream().filter( v -> {
                            if (v.fieldname.equals("supplierid")) {
                                return false;
                            }
                            return true;
                        }).collect(Collectors.toList());
                        model.addAttribute("FIELDS", Utils.classToDataDc(newFields));
                        List<HashMap<String,Object>> list = (ArrayList)model.getAttribute("FIELDS");
                        List<HashMap<String,Object>> listNew = importdataSetService.getImportdataSetUI(list,linkLst);
                        model.addAttribute("FIELDS", listNew);
                    }
                    model.addAttribute("record", record);
                    model.addAttribute("modulename", module.Tabinfo.modulename);
                    model.addAttribute("VIEWMODULENAME", module.Tabinfo.modulename);
                    model.addAttribute("AUTHORIZE", PickListUtils.getPickList("authorize"));
                    model.addAttribute("ISPUBLIC", false);
                    importdataSetService.getImportdataSet(module.Tabinfo.modulename,model);
                }
            }
        }catch(Exception ignored) {}
        return WebViews.view("modules/settings/importdata/EditView");
    }

    @ApiOperation(value = "导入配置按钮请求重复条件设置接口")
    @PostMapping("/addConditons")
    @Log("导入配置按钮请求重复条件设置")
    public Object addConditons(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        httpRequest.put("modulename",httpRequest.get("module"));
        httpRequest.put("multiselect","false");
        httpRequest.put("digwidth","auto");
        httpRequest.put("digheight","auto");
        httpRequest.put("record",httpRequest.get("record"));
        if (Utils.isNotEmpty(httpRequest.get("record"))) {
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                if(!Utils.isEmpty(module.Fields) && !module.Fields.isEmpty()) {
                    model.addAttribute("FIELDS", Utils.classToDataDc(module.Fields));
                }
            }
        }
        List<HashMap<String,Object>> list = (ArrayList)model.getAttribute("FIELDS");
        List<HashMap<String,Object>> listNew = new ArrayList<>();
        if(Utils.isNotEmpty(list)) {
            for (HashMap<String, Object> map : list) {
                if ("3".equals(map.get("uitype").toString()) || "4".equals(map.get("uitype").toString()) || "22".equals(map.get("uitype").toString()) ||
                        "9".equals(map.get("uitype").toString()) || "17".equals(map.get("uitype").toString()) || "18".equals(map.get("uitype").toString()) ||
                        "24".equals(map.get("uitype").toString()) || "26".equals(map.get("uitype").toString()) || "29".equals(map.get("uitype").toString()) ||
                        "30".equals(map.get("uitype").toString()) || "31".equals(map.get("uitype").toString()) || "12".equals(map.get("uitype").toString())) {
                    continue;
                }
                listNew.add(map);
            }
        }
        model.addAttribute("FIELDS", listNew);
        model.addAttribute("modulename", httpRequest.get("modulename"));
        model.addAttribute("VIEWMODULENAME", httpRequest.getOrDefault("modulename", ""));
        model.addAttribute("AUTHORIZE", PickListUtils.getPickList("authorize"));
        model.addAttribute("ISPUBLIC", false);
        return WebViews.view("modules/settings/importdata/addConditons");
    }

    @ApiOperation(value = "导入配置保存请求接口")
    @PostMapping("/importdataAction/save")
    @ResponseBody
    @Log("导入配置保存功能")
    public Object importdataSave(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = CommonUtil.getRequestQuery(request);
            List<String> columnlist = new ArrayList<>();
            if (httpRequest.get("columnlist") instanceof List) {
                columnlist = new ArrayList<String>((List)httpRequest.get("columnlist"));
            }else if (httpRequest.get("columnlist") instanceof String) {
                String colName = (String)httpRequest.get("columnlist");
                columnlist.add(colName);
            }
            httpRequest.put("modulename",httpRequest.get("module"));
            Object fields = null;
            List<?> linkLst = null;
            if (Utils.isNotEmpty(httpRequest.get("record"))) {
                Object formDesigns = formdesignService.getDesignInfo(httpRequest);
                if(!Utils.isEmpty(formDesigns)){
                    Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                    linkLst = module.OutsideLinks;
                    if(!Utils.isEmpty(module.Fields) && !module.Fields.isEmpty()) {
                        fields = Utils.classToDataDc(module.Fields);
                    }
                }
            }
            importdataSetService.saveExcelset(httpRequest, columnlist, fields, linkLst);
            return new WebResponse().close().success(0,"设置完成");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    /**
     * 导出设置好的模板信息
     **/
    @ApiOperation(value = "导出模板按钮请求接口")
    @PostMapping("/downloadExcelAction/ajax")
    @ResponseBody
    @Log("导出模板按钮请求")
    public void downloadExcelAction(HttpServletRequest request, HttpServletResponse response) {
        try{
            importdataSetService.downloadExcel(request,response);
        }catch (Exception ignored){}
    }

    /**
     * 数据导入弹出窗口
     **/
    @ApiOperation(value = "Excel导入按钮请求接口")
    @PostMapping("/importAction/ajax")
    @Log("Excel导入按钮请求")
    public Object importAction(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", httpRequest.get("modulename"));
        return WebViews.view("modules/settings/importdata/uploadExcel");
    }

    @ApiOperation(value = "Excel导入保存请求接口")
    @PostMapping("/uploadSave")
    @ResponseBody
    @Log("Excel导入保存功能")
    public Object uploadSave(HttpServletRequest request, @RequestParam(name="file") MultipartFile file) {
        try{
            String drfs = request.getParameter("drfs");
            String modulename = request.getParameter("modulename");
            List<SortObject> list = importdataSetService.getExcelset(modulename);
            Collections.sort(list);
            String[] titleNames = new String[list.size()];
            String[] titleCodes = new String[list.size()];
            String[] titleUitype = new String[list.size()];
            String[] titlePicklist = new String[list.size()];
            String[] titlesBt = new String[list.size()];
            HashMap<String,String> titlesCf = new HashMap<>();
            String[] titlesMrz = new String[list.size()];
            String[] titlesLink = new String[list.size()];
            String[] titlesTypeData = new String[list.size()];
            int ii = 0;
            for (SortObject obj : list) {
                titleNames[ii] = obj.getName();
                titleCodes[ii] = obj.getCode();
                titleUitype[ii] = obj.getUitype();
                titlePicklist[ii] = obj.getPicklist();
                if ("5".equals(obj.getUitype())) {
                    titlePicklist[ii] = "sfList";
                }
                titlesBt[ii] = obj.getBitian();
                titlesMrz[ii] = obj.getMrz();
                titlesLink[ii] = obj.getLink();
                titlesTypeData[ii] = obj.getTypeofdata();
                if (null != obj.getCongfuchk() && !"".equals(obj.getCongfuchk())) {
                    String key = obj.getCongfuchk();
                    if (key.contains(",")) {
                        String[] keyAry = key.split(",");
                        for (String keyVal : keyAry) {
                            if (!titlesCf.containsKey(keyVal)) {
                                titlesCf.put(keyVal, obj.getCode());
                            } else {
                                titlesCf.put(keyVal, titlesCf.get(keyVal) + "," + obj.getCode());
                            }
                        }
                    } else {
                        if (!titlesCf.containsKey(obj.getCongfuchk())) {
                            titlesCf.put(obj.getCongfuchk(), obj.getCode());
                        } else {
                            titlesCf.put(obj.getCongfuchk(), titlesCf.get(obj.getCongfuchk()) + "," + obj.getCode());
                        }
                    }
                }
                ii++;
            }
            File importFile = CommonUtil.mutipartFileToFile(file);
            ExecutorService exec = Executors.newCachedThreadPool(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e){
                            System.out.println("捕获到异常："+e.getMessage());
                        }
                    });
                    return t;
                }
            });
            Object departObj = departmentsService.getRoleTree(null,true);
            JSONArray jsonArray = null;
            if (null!=departObj) {
                jsonArray = JSONArray.parseArray((String) departObj);
            }
            MyThread mt1 = new MyThread(importFile,modulename,titleNames,titleCodes,titleUitype,titlePicklist,titlesBt,titlesCf,titlesMrz,titlesLink,titlesTypeData,drfs,jsonArray);
            Future<?> future = exec.submit(mt1);
            exec.shutdown();
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                String msg = e.getMessage().substring(27);
                if (msg.indexOf("已成功导入：")>0) {
                    return new WebResponse().fail(msg.substring(msg.indexOf("</br>")+5).trim());
                }else {
                    return new WebResponse().fail(msg.trim());
                }
            }
            return new WebResponse().close().success(0,"导入完成");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return new WebResponse().fail(e.getMessage().substring(27).trim());
        }
    }
}
