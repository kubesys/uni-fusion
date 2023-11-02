package com.qnkj.core.base.modules.settings.importdata.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.entitys.OutsideLink;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.departments.service.IDepartmentsService;
import com.qnkj.core.base.modules.settings.importdata.Util.CommonUtil;
import com.qnkj.core.base.modules.settings.importdata.Util.SortObject;
import com.qnkj.core.base.modules.settings.importdata.services.IImportdataSetService;
import com.qnkj.core.utils.PickListUtils;
import com.qnkj.core.utils.ProfileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * create by Auto Generator
 * create date 2021-09-02
 */
@Slf4j
@Service
public class ImportdataSetServiceImpl implements IImportdataSetService {
    @Autowired
    private IDepartmentsService departmentsService;
    @Override
    public Model getImportdataSet(String modulename, Model model)throws Exception {
        List<Object> suppliers = XN_Query.contentQuery().tag(CommonUtil.TABLE_NAME)
                .filter("type", "eic", CommonUtil.TABLE_NAME)
                .notDelete()
                .filter("my.tablename", "=", modulename)
                .end(-1).execute();
        if (!suppliers.isEmpty()) {
            List<SortObject> list = new ArrayList<>();
            List<SortObject> listBt = new ArrayList<>();
            List<SortObject> listMr = new ArrayList<>();
            HashMap<String,String> cfMap = new HashMap<>();
            suppliers.forEach(item -> {
                Map<String, Object> map = ((Content)item).my;
                SortObject obj = new SortObject((String)map.get("fieldname"), (String)map.get("fieldlabel"), (String)map.get("align"), map.get("uitype").toString(), (String)map.get("picklist"), Integer.valueOf(map.get("sequence").toString()), (String)map.get("bitian"), (String)map.get("congfuchk"), (String)map.get("mrz"), (String)map.get("linkmodule"), (String)map.get("typeofdata"));
                list.add(obj);
                if (null!=obj.getCongfuchk()&&!"".equals(obj.getCongfuchk())) {
                    String key = obj.getCongfuchk();
                    if (key.indexOf(",")>=0) {
                        String[] keyAry = key.split(",");
                        for (String keyVal:keyAry) {
                            if (!cfMap.containsKey(keyVal)) {
                                cfMap.put(keyVal,obj.getCode()+";"+obj.getName());
                            }else {
                                cfMap.put(keyVal,cfMap.get(keyVal)+","+obj.getCode()+";"+obj.getName());
                            }
                        }
                    }else {
                        if (!cfMap.containsKey(obj.getCongfuchk())) {
                            cfMap.put(obj.getCongfuchk(),obj.getCode()+";"+obj.getName());
                        }else {
                            cfMap.put(obj.getCongfuchk(),cfMap.get(obj.getCongfuchk())+","+obj.getCode()+";"+obj.getName());
                        }
                    }
                }
                if ("true".equals(obj.getBitian())) {
                    obj.setBitian(obj.getCode()+","+obj.getBitian());
                    listBt.add(obj);
                }
                if (null!=obj.getMrz()&&!"".equals(obj.getMrz())) {
                    obj.setMrz(obj.getCode()+","+obj.getMrz());
                    listMr.add(obj);
                }
            });
            Collections.sort(list);
            StringBuffer cfCode = new StringBuffer();
            StringBuffer cfName = new StringBuffer();
            for (Map.Entry<String, String> entryCf : cfMap.entrySet()) {
                String[] contentAry = entryCf.getValue().split(",");
                StringBuffer cfCodeItem = new StringBuffer();
                StringBuffer cfNameItem = new StringBuffer();
                for (int ii=0,size=contentAry.length; ii<size; ii++) {
                    String[] itemAry = contentAry[ii].split(";");
                    if (ii<size-1) {
                        cfCodeItem.append(itemAry[0]).append(",");
                        cfNameItem.append(itemAry[1]).append(",");
                    }else {
                        cfCodeItem.append(itemAry[0]);
                        cfNameItem.append(itemAry[1]);
                        cfCode.append(CommonUtil.unique(cfCodeItem.toString())).append(";");
                        cfName.append(CommonUtil.unique(cfNameItem.toString())).append(";");
                    }
                }
            }
            model.addAttribute("CUSTOMVIEW", list);
            model.addAttribute("CUSTOMVIEWBT", listBt);
            model.addAttribute("CUSTOMVIEWMR", listMr);
            model.addAttribute("CUSTOMVIEWCFCODE", cfCode);
            model.addAttribute("CUSTOMVIEWCFNAME", cfName);
        }
        return model;
    }

    /**
     * 获取存储的导入配置
     * @param list 表单栏位信息
     * @param linkLst 表单字段外连信息
     * @return 导入配置对应信息
     * @throws Exception
     */
    @Override
    public List getImportdataSetUI(List<HashMap<String,Object>> list, List linkLst)throws Exception {
        List<HashMap<String,Object>> listNew = new ArrayList<>();
        if (list==null) {
            return null;
        }
        for (HashMap map:list) {
            if ("3".equals(map.get("uitype").toString())||"4".equals(map.get("uitype").toString())||"22".equals(map.get("uitype").toString())||
                "9".equals(map.get("uitype").toString())||"17".equals(map.get("uitype").toString())||"18".equals(map.get("uitype").toString())||
                "24".equals(map.get("uitype").toString())||"26".equals(map.get("uitype").toString())||"29".equals(map.get("uitype").toString())||
                "30".equals(map.get("uitype").toString())||"31".equals(map.get("uitype").toString())||"12".equals(map.get("uitype").toString())) {//||"11".equals(map.get("uitype").toString())
                continue;
            }else if (null!=map.get("picklist")&&!"".equals(map.get("picklist"))) {
                StringBuffer optionStr = new StringBuffer();
                Object picklists = PickListUtils.getPickList(map.get("picklist").toString());
                List<Object> theList = (ArrayList)picklists;
                if (null!=theList) {
                    for (Object objPick:theList) {
                        List<Object> records = new ArrayList<>(Arrays.asList(objPick.toString().split(",")));
                        optionStr.append("<option value=\"").append(records.get(0).toString().trim().replaceAll("\\[","")).append("\">").append(records.get(1).toString().trim()).append("</option>");
                    }
                    map.put("picklistOptions",optionStr);
                }
            }else if ("10".equals(map.get("uitype").toString())) {
                Object departObj = departmentsService.getRoleTree(null,true);
                JSONArray jsonArray = JSONArray.parseArray((String)departObj);
                StringBuffer optionStr = new StringBuffer();
                for (int i=0,size=jsonArray.size(); i<size; i++) {
                    JSONObject obj = (JSONObject)jsonArray.get(i);
                    if (null!=obj) {
                        String departmentid = (String)obj.get("departmentid");
                        String departmentname = (String)obj.get("departmentname");
                        if (null!=departmentid&&!"".equals(departmentid)&&null!=departmentname&&!"".equals(departmentname)) {
                            optionStr.append("<option value=\"").append(departmentid).append("\">").append(departmentname).append("</option>");
                        }
                    }
                }
                map.put("picklist","bumen");
                map.put("picklistOptions",optionStr);
            }
            if (null!=map.get("typeofdata")&&!"".equals(map.get("typeofdata"))) {
                String typeofdata = map.get("typeofdata").toString();
                typeofdata = typeofdata.substring(0,typeofdata.indexOf("~"));
                map.put("typeofdata",typeofdata);
            }
            if (null!=linkLst) {
                for (Object obj:linkLst) {
                    OutsideLink link = (OutsideLink)obj;
                    if (null!=link&&map.get("fieldname").equals(link.fieldname)) {
                        List<Object> suppliersLnk = XN_Query.contentQuery().tag(link.relmodule)
                                .filter("type", "eic", link.relmodule)
                                .notDelete()
                                .begin(0)
                                .end(200).execute();
                        List<Object> query = XN_Query.contentQuery().tag("entitynames")
                                .filter("type", "eic", "entitynames")
                                .filter("my.modulename", "=", link.relmodule)
                                .notDelete().end(-1).execute();
                        String showFieldName = null;
                        if (!query.isEmpty()) {
                            for (Object item:query) {
                                Map<String, Object> mapLnkQ = ((Content)item).my;
                                showFieldName = mapLnkQ.get("fieldname").toString();
                            }
                        }
                        if (!suppliersLnk.isEmpty()) {
                            StringBuffer optionStr = new StringBuffer();
                            for (Object item:suppliersLnk) {
                                Map<String, Object> mapLnk = ((Content)item).my;
                                StringBuffer val = new StringBuffer();
                                for (Map.Entry<String, Object> entryLnk : mapLnk.entrySet()) {
                                    if (null!=entryLnk.getValue()&&!"".equals(entryLnk.getValue())&&!"0".equals(entryLnk.getValue())&&entryLnk.getKey().equals(showFieldName)) {
                                        val.append(entryLnk.getValue()).append(",");
                                    }
                                }
                                String valStr = val.toString();
                                if (valStr.lastIndexOf(",")>0) {
                                    optionStr.append("<option value=\"").append(((Content)item).id).append("\">").append(valStr.substring(0,valStr.lastIndexOf(","))).append("</option>");
                                }
                            }
                            map.put("outlink",link.relmodule);
                            map.put("picklistOptions",optionStr);
                        }
                        break;
                    }
                }
            }
            listNew.add(map);
        }
        return listNew;
    }

    /**
     * 保存导入配置
     * @param Request 页面请求的保存信息
     * @param columnlist 页面请求的保存栏位信息　
     * @param fields 表单栏位信息
     * @param linkLst 表单字段外连信息
     * @throws Exception
     */
    @Override
    public void saveExcelset(Map<String, Object> Request, List columnlist, Object fields, List linkLst)throws Exception {
        String tableNameImport = (String)Request.get("module");
        List<Object> suppliers = XN_Query.contentQuery().tag(CommonUtil.TABLE_NAME)
                .filter("type", "eic", CommonUtil.TABLE_NAME)
                .notDelete()
                .filter("my.tablename", "=", tableNameImport)
                .end(-1).execute();
        if (!suppliers.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            suppliers.forEach(item -> {
                ids.append(((Content)item).id).append(",");
            });
            List<Object> records = new ArrayList<>(Arrays.asList(ids.toString().split(",")));
            XN_Content.delete(records,CommonUtil.TABLE_NAME);
        }
        List<Object> fieldsList = (ArrayList)fields;
        HashMap<String, SortObject> fieldsAllMap = new HashMap<>();
        HashMap<String, List<HashMap<String,String>>> fieldsPicklistMap = new HashMap<>();
        for (Object obj:fieldsList) {
            HashMap map = (HashMap)obj;
            String typeofdata = map.get("typeofdata").toString();
            typeofdata = typeofdata.substring(0,typeofdata.indexOf("~"));
            fieldsAllMap.put((String)map.get("fieldname"),new SortObject((String)map.get("fieldname"), (String)map.get("fieldlabel"), (String)map.get("align"), map.get("uitype").toString(), (String)map.get("picklist"), Integer.valueOf(map.get("sequence").toString()), "", "", "", "", typeofdata));
            if (null!=map.get("picklist")&&!"".equals(map.get("picklist"))) {
                Object picklists = PickListUtils.getPickList(map.get("picklist").toString());
                List<Object> theList = (ArrayList)picklists;
                if (null!=theList) {
                    List<HashMap<String,String>> picklistMap = new ArrayList();
                    for (Object objPick:theList) {
                        List<Object> records = new ArrayList<>(Arrays.asList(objPick.toString().split(",")));
                        HashMap<String,String> pickMap = new HashMap<>();
                        pickMap.put(records.get(0).toString().trim().replaceAll("\\[",""), records.get(1).toString().trim());
                        picklistMap.add(pickMap);
                    }
                    fieldsPicklistMap.put(map.get("picklist").toString(), picklistMap);
                }
            }
            if ("10".equals(map.get("uitype"))) {
                Object departObj = departmentsService.getRoleTree(null,true);
                JSONArray jsonArray = JSONArray.parseArray((String)departObj);
                List<HashMap<String,String>> picklistMap = new ArrayList();
                for (int i=0,size=jsonArray.size(); i<size; i++) {
                    JSONObject objDep = (JSONObject)jsonArray.get(i);
                    if (null!=objDep) {
                        String departmentid = (String)objDep.get("departmentid");
                        String departmentname = (String)objDep.get("departmentname");
                        if (null!=departmentid&&!"".equals(departmentid)&&null!=departmentname&&!"".equals(departmentname)) {
                            HashMap<String,String> pickMap = new HashMap<>();
                            pickMap.put(departmentid, departmentname);
                            picklistMap.add(pickMap);
                        }
                    }
                }
            }
        }
        List<Object> objs = new ArrayList<>();
        int i = 1;
        for(Object col:columnlist) {
            if (null==col) {
                continue;
            }
            String fieldname = col.toString();
            SortObject sortObject = fieldsAllMap.get(fieldname);
            if (null!=sortObject) {
                Content content = XN_Content.create(CommonUtil.TABLE_NAME,"",ProfileUtils.getCurrentProfileId(),0);
                content.add("deleted","0");
                content.add("tablename",tableNameImport);
                content.add("fieldname",fieldname);
                content.add("fieldlabel",sortObject.getName());
                content.add("align",sortObject.getAlign());
                content.add("uitype",sortObject.getUitype());
                content.add("picklist",sortObject.getPicklist());
                content.add("typeofdata",sortObject.getTypeofdata());
                content.add("sequence",i);
                HashMap<String,String> mrzMap = new HashMap<>();
                for (Map.Entry<String, Object> entryMap : Request.entrySet()) {
                    if ("bt".equals(entryMap.getKey())) {
                        ArrayList listBt = (ArrayList)entryMap.getValue();
                        for (Object objBt:listBt) {
                            HashMap<String,String> mapBt = (HashMap)objBt;
                            for (Map.Entry<String, String> entryBt : mapBt.entrySet()) {
                                if (fieldname.equals(entryBt.getKey())&&"true".equals(entryBt.getValue())) {
                                    content.add("bitian","true");
                                }
                            }
                        }
                    }
                    if ("cf".equals(entryMap.getKey())) {
                        ArrayList listCf = (ArrayList)entryMap.getValue();
                        listCf = CommonUtil.uniqueListMap(listCf);
                        for (Object objCf:listCf) {
                            HashMap<String,String> mapCf = (HashMap)objCf;
                            for (Map.Entry<String, String> entryCf : mapCf.entrySet()) {
                                if (entryCf.getKey().indexOf(fieldname)>=0) {
                                    if (null!=content.my.get("congfuchk")&&!"".equals(content.my.get("congfuchk").toString())) {
                                        String val = content.my.get("congfuchk").toString()+","+entryCf.getValue();
                                        content.add("congfuchk", CommonUtil.unique(val));
                                    }else {
                                        content.add("congfuchk", CommonUtil.unique(entryCf.getValue()));
                                    }
                                }
                            }
                        }
                    }
                    if (entryMap.getKey().length()>=4&&"mrz".equals(entryMap.getKey().substring(0,3))) {
                        mrzMap.put(entryMap.getKey().substring(4,entryMap.getKey().length()-1),(String)entryMap.getValue());
                    }
                }
                for (Map.Entry<String, String> entryMr : mrzMap.entrySet()) {
                    if (fieldname.equals(entryMr.getKey())) {
                        String saveValue = entryMr.getValue();
                        if ("当前用户".equals(entryMr.getValue())) {
                            saveValue = "curuser";
                        }else if ("当前日期".equals(entryMr.getValue())) {
                            saveValue = "curdate";
                        }else if ("当前时间".equals(entryMr.getValue())) {
                            saveValue = "curtime";
                        }else if ("当前日期时间".equals(entryMr.getValue())) {
                            saveValue = "curdatetime";
                        }else if (null!=sortObject.getPicklist()&&!"".equals(sortObject.getPicklist())) {
                            List<HashMap<String,String>> picklistMap = fieldsPicklistMap.get(sortObject.getPicklist());
                            for (HashMap<String,String> pickMap:picklistMap) {
                                boolean getPickValue = false;
                                for (Map.Entry<String, String> entryPick : pickMap.entrySet()) {
                                    if (entryPick.getValue().equals(saveValue)||entryPick.getKey().equals(saveValue)) {
                                        getPickValue = true;
                                        saveValue = entryPick.getKey();
                                        break;
                                    }
                                }
                                if (getPickValue) {
                                    break;
                                }
                            }
                        }
                        content.add("mrz",saveValue);
                    }
                }
                if (null!=linkLst) {
                    for (Object obj:linkLst) {
                        OutsideLink link = (OutsideLink)obj;
                        if (null!=link&&fieldname.equals(link.fieldname)) {
                            content.add("linkmodule",link.relmodule);
                            break;
                        }
                    }
                }
                objs.add(content);
                i++;
            }
        }
        XN_Content.batchsave(objs, CommonUtil.TABLE_NAME);
    }

    @Override
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response)throws Exception {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        String tableNameImport = (String)httpRequest.get("module");
        List<Object> suppliers = XN_Query.contentQuery().tag(CommonUtil.TABLE_NAME)
                .filter("type", "eic", CommonUtil.TABLE_NAME)
                .notDelete()
                .filter("my.tablename", "=", tableNameImport)
                .end(-1).execute();
        List<SortObject> list = new ArrayList<SortObject>();
        if (!suppliers.isEmpty()) {
            suppliers.forEach(item -> {
                Map<String, Object> map = ((Content)item).my;
                list.add(new SortObject((String)map.get("fieldname"), (String)map.get("fieldlabel"), (String)map.get("align"), map.get("uitype").toString(), (String)map.get("picklist"), Integer.valueOf(map.get("sequence").toString()), (String)map.get("bitian"), (String)map.get("congfuchk"), (String)map.get("mrz"), (String)map.get("linkmodule"), (String)map.get("typeofdata")));
            });
        }
        Collections.sort(list);
        String[] titles = new String[list.size()];
        String[] titlesStyle = new String[list.size()];
        String[] titlesTyple = new String[list.size()];
        String[] titlesPicklist = new String[list.size()];
        String[] titlesBt = new String[list.size()];
        String[] titlesCf = new String[list.size()];
        String[] titlesMrz = new String[list.size()];
        String[] titlesLink = new String[list.size()];
        String[] titlesUitype = new String[list.size()];
        String[] titlesTypeData = new String[list.size()];
        HashMap<String,String> mapPicklist = new HashMap();
        int j = 0;
        for (int i=0,size=list.size(); i<size; i++) {
            SortObject obj = list.get(i);
            titlesStyle[j] = obj.getAlign();
            titlesBt[j] = obj.getBitian();
            titlesCf[j] = obj.getCongfuchk();
            titlesMrz[j] = obj.getMrz();
            titlesLink[j] = obj.getLink();
            titlesTypeData[j] = obj.getTypeofdata();
            if (!"15".equals(obj.getUitype())&&!"16".equals(obj.getUitype())) {
                titlesPicklist[j] = obj.getPicklist();
            }
            if ("5".equals(obj.getUitype())) {
                titlesPicklist[j] = "sfList";
            }
            if ("1".equals(obj.getUitype())) {
                titlesTyple[j] = "String";
            }else if ("6".equals(obj.getUitype())) {
                titlesTyple[j] = "yyyy-MM-dd";
            }else if ("7".equals(obj.getUitype())) {
                titlesTyple[j] = "yyyy-MM-dd HH:mm:ss";
            }else if ("8".equals(obj.getUitype())) {
                titlesTyple[j] = "HH:mm:ss";
            }else if ("27".equals(obj.getUitype())) {
                titlesTyple[j] = "Double";
            }else if ("15".equals(obj.getUitype())||"16".equals(obj.getUitype())) {
                titlesTyple[j] = "String";
            }else {
                titlesTyple[j] = "String";
            }
            titles[j] = obj.getName();
            titlesUitype[j] = obj.getUitype();
            if ("15".equals(obj.getUitype())||"16".equals(obj.getUitype())) {
                if (null!=obj.getPicklist()&&!"".equals(obj.getPicklist())) {
                    mapPicklist.put(obj.getName(), obj.getPicklist());
                    mapPicklist.put(obj.getName() + "_uitype", obj.getUitype());
                }else {
                    mapPicklist.put(obj.getName(),obj.getCode());
                    mapPicklist.put(obj.getName()+"_uitype",obj.getUitype());
                }
            }
            j++;
        }
        String sheetName = "导入模板信息表";
        List<Object> queryT = XN_Query.contentQuery().tag("formdesigns")
                .filter("type", "eic", "formdesigns")
                .filter("my.modulename", "=", tableNameImport)
                .notDelete().end(-1).execute();
        if (!queryT.isEmpty()) {
            for (Object item:queryT) {
                Map<String, Object> mapLnkQ = ((Content)item).my;
                sheetName = mapLnkQ.get("module").toString();
            }
        }
        response.setHeader("content-type", "application/octet-stream;charset=utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = new String(URLEncoder.encode(sheetName,"UTF-8"));
        response.setHeader("Content-disposition","attachment;filename="+fileName+".xlsx");
        try(XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet(sheetName);
            XSSFRow row = sheet.createRow(0);
            for (int i = 0, size = titles.length; i < size; i++) {
                Cell cell = row.createCell(i);
                XSSFCellStyle cellStyle = wb.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
                XSSFFont font = wb.createFont();
                font.setFontName("仿宋_GB2312");
                font.setBold(true);
                font.setFontHeightInPoints((short) 12);
                cellStyle.setFont(font);
                cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setLocked(true);
                cell.setCellStyle(cellStyle);
                StringBuffer titleStr = new StringBuffer(titles[i]);
                if ("true".equals(titlesBt[i])) {
                    if ("6".equals(titlesUitype[i])) {
                        titleStr.append("(yyyy-MM-dd").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append("必填)");
                    } else if ("7".equals(titlesUitype[i])) {
                        titleStr.append("(yyyy-MM-dd HH:mm:ss").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append("必填)");
                    } else if ("8".equals(titlesUitype[i])) {
                        titleStr.append("(HH:mm:ss").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append("必填)");
                    } else if ("27".equals(titlesUitype[i])) {
                        titleStr.append("(精度0.00").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append("必填)");
                    } else if ("15".equals(titlesUitype[i]) || "16".equals(titlesUitype[i])) {
                        titleStr.append("(多选英文逗号隔开").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append("必填)");
                    } else {
                        String titleD = CommonUtil.getTitleDesc(titlesTypeData[i]);
                        titleStr.append("".equals(titleD) ? "(必填)" : "(" + titleD + "必填)");
                    }
                    cell.setCellValue(titleStr.toString());
                } else {
                    if ("6".equals(titlesUitype[i])) {
                        titleStr.append("(yyyy-MM-dd").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append(")");
                    } else if ("7".equals(titlesUitype[i])) {
                        titleStr.append("(yyyy-MM-dd HH:mm:ss").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append(")");
                    } else if ("8".equals(titlesUitype[i])) {
                        titleStr.append("(HH:mm:ss").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append(")");
                    } else if ("27".equals(titlesUitype[i])) {
                        titleStr.append("(精度0.00").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append(")");
                    } else if ("15".equals(titlesUitype[i]) || "16".equals(titlesUitype[i])) {
                        titleStr.append("(多选英文逗号隔开").append(CommonUtil.getTitleDesc(titlesTypeData[i])).append(")");
                    } else {
                        String titleD = CommonUtil.getTitleDesc(titlesTypeData[i]);
                        titleStr.append("".equals(titleD) ? "" : "(" + titleD + ")");
                    }
                    cell.setCellValue(titleStr.toString());
                }
                //sheet.autoSizeColumn(i, true);
                sheet.setColumnWidth(i, 6000);
            }

            XSSFRow rowContent = sheet.createRow(1);
            XSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setLocked(false);
            for (int i = 0, size = titles.length; i < size; i++) {
                Cell cell = rowContent.createCell(i);
                if ("center".equals(titlesStyle[i])) {
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                } else if ("left".equals(titlesStyle[i])) {
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                } else if ("right".equals(titlesStyle[i])) {
                    cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                }
                XSSFDataFormat format = wb.createDataFormat();
                cellStyle.setDataFormat(format.getFormat("@"));
                XSSFFont font = wb.createFont();
                font.setFontHeightInPoints((short) 11);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                if ("curuser".equals(titlesMrz[i])) {
                    cell.setCellValue(ProfileUtils.getCurrentProfileName());
                } else if ("curdatetime".equals(titlesMrz[i])) {
                    SimpleDateFormat formatDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cell.setCellValue(formatDt.format(new Date()));
                } else if ("curdate".equals(titlesMrz[i])) {
                    SimpleDateFormat formatDt = new SimpleDateFormat("yyyy-MM-dd");
                    cell.setCellValue(formatDt.format(new Date()));
                } else if ("curtime".equals(titlesMrz[i])) {
                    SimpleDateFormat formatDt = new SimpleDateFormat("HH:mm:ss");
                    cell.setCellValue(formatDt.format(new Date()));
                } else if (null != titlesMrz[i] && !"".equals(titlesMrz[i])) {
                    cell.setCellValue(titlesMrz[i]);
                }
                if (null != titlesPicklist[i] && !"".equals(titlesPicklist[i])) {
                    if ("sfList".equals(titlesPicklist[i])) {
                        String[] textlist = {"是", "否"};
                        CommonUtil.setDataValidationList((short) 1, (short) 1, (short) i, (short) i, textlist, sheet);
                        if (null != titlesMrz[i] && !"".equals(titlesMrz[i])) {
                            String sfText = "";
                            if ("1".equals(titlesMrz[i])) {
                                sfText = "是";
                            } else if ("0".equals(titlesMrz[i])) {
                                sfText = "否";
                            }
                            cell.setCellValue(sfText);
                        }
                    } else {
                        String showMrz = titlesMrz[i];
                        Object picklists = PickListUtils.getPickList(titlesPicklist[i]);
                        List<Object> theList = (ArrayList) picklists;
                        if (null != theList) {
                            int jj = 0;
                            String[] textlist = null;
                            textlist = new String[theList.size()];
                            for (Object objPick : theList) {
                                List<Object> records = new ArrayList<>(Arrays.asList(objPick.toString().split(",")));
                                textlist[jj] = records.get(1).toString();
                                if (("14".equals(titlesUitype[i]) || "20".equals(titlesUitype[i])) && null != showMrz && showMrz.equals(records.get(0).toString().trim().replaceAll("\\[", ""))) {
                                    showMrz = records.get(1).toString();
                                }
                                jj++;
                            }
                            CommonUtil.setDataValidationList((short) 1, (short) 1, (short) i, (short) i, textlist, sheet);
                        }
                        if (null != titlesMrz[i] && !"".equals(titlesMrz[i])) {
                            cell.setCellValue(showMrz);
                        }
                    }
                } else if (null != titlesMrz[i] && !"".equals(titlesMrz[i]) && null != mapPicklist.get(titles[i]) && !"".equals(mapPicklist.get(titles[i]))) {
                    Object picklists = PickListUtils.getPickList(mapPicklist.get(titles[i]));
                    List<Object> theList = (ArrayList) picklists;
                    if (null != theList) {
                        for (Object objPick : theList) {
                            List<Object> records = new ArrayList<>(Arrays.asList(objPick.toString().split(",")));
                            if (titlesMrz[i].equals(records.get(0).toString().trim().replaceAll("\\[", ""))) {
                                cell.setCellValue(records.get(1).toString());
                                break;
                            }
                        }
                    }
                }
                if (null != titlesLink[i] && !"".equals(titlesLink[i])) {
                    List<Object> suppliersLnk = XN_Query.contentQuery().tag(titlesLink[i])
                            .filter("type", "eic", titlesLink[i])
                            .notDelete()
                            .begin(0)
                            .end(200).execute();
                    List<Object> query = XN_Query.contentQuery().tag("entitynames")
                            .filter("type", "eic", "entitynames")
                            .filter("my.modulename", "=", titlesLink[i])
                            .notDelete().end(-1).execute();
                    String showFieldName = null;
                    if (!query.isEmpty()) {
                        for (Object item : query) {
                            Map<String, Object> mapLnkQ = ((Content) item).my;
                            showFieldName = mapLnkQ.get("fieldname").toString();
                        }
                    }
                    if (!suppliersLnk.isEmpty()) {
                        String[] textlist = new String[suppliersLnk.size()];
                        int jj = 0;
                        for (Object item : suppliersLnk) {
                            Map<String, Object> mapLnk = ((Content) item).my;
                            StringBuilder val = new StringBuilder();
                            for (Map.Entry<String, Object> entryLnk : mapLnk.entrySet()) {
                                if (null != entryLnk.getValue() && !"".equals(entryLnk.getValue()) && !"0".equals(entryLnk.getValue()) && entryLnk.getKey().equals(showFieldName)) {
                                    val.append(entryLnk.getValue()).append(",");
                                    if (null != titlesMrz[i] && !"".equals(titlesMrz[i]) && titlesMrz[i].equals(((Content) item).id)) {
                                        cell.setCellValue(entryLnk.getValue().toString());
                                    }
                                }
                            }
                            String valStr = val.toString();
                            if (valStr.lastIndexOf(",") > 0) {
                                textlist[jj] = valStr.substring(0, valStr.lastIndexOf(","));
                                jj++;
                            }
                        }
                        CommonUtil.setDataValidationList((short) 1, (short) 1, (short) i, (short) i, textlist, sheet);
                    }
                }
                if (null != titlesUitype[i] && "10".equals(titlesUitype[i])) {
                    Object departObj = departmentsService.getRoleTree(null, true);
                    JSONArray jsonArray = JSONArray.parseArray((String) departObj);
                    String[] textlist = new String[jsonArray.size()];
                    int jjj = 0;
                    for (Object o : jsonArray) {
                        JSONObject objDep = (JSONObject) o;
                        if (null != objDep) {
                            String departmentid = (String) objDep.get("departmentid");
                            String departmentname = (String) objDep.get("departmentname");
                            StringBuffer val = new StringBuffer();
                            if (null != departmentid && !"".equals(departmentid) && null != departmentname && !"".equals(departmentname)) {
                                val.append(departmentname).append(",");
                                if (null != titlesMrz[i] && !"".equals(titlesMrz[i]) && titlesMrz[i].equals(departmentid)) {
                                    cell.setCellValue(departmentname);
                                }
                            }
                            String valStr = val.toString();
                            if (valStr.lastIndexOf(",") > 0) {
                                textlist[jjj] = valStr.substring(0, valStr.lastIndexOf(","));
                                jjj++;
                            }
                        }
                    }
                    CommonUtil.setDataValidationList((short) 1, (short) 1, (short) i, (short) i, textlist, sheet);
                }
            }
            rowContent.setRowStyle(cellStyle);
            sheet.createFreezePane(0, 1, 0, 1);
            //sheet.protectSheet("qnkj168");
            for (Map.Entry<String, String> entryPick : mapPicklist.entrySet()) {
                if (entryPick.getKey().indexOf("_uitype") > 0) {
                    continue;
                }
                XSSFSheet sheetPick = wb.createSheet(entryPick.getKey() + "(多选英文逗号隔开)");
                sheetPick.protectSheet("qnkj168");
                XSSFRow rowPick = sheetPick.createRow(0);
                Object picklists = PickListUtils.getPickList(entryPick.getValue().toString());
                List<Object> theList = (ArrayList) picklists;
                Cell cell = rowPick.createCell(0);
                XSSFCellStyle cellStyleC = wb.createCellStyle();
                cellStyleC.setAlignment(HorizontalAlignment.CENTER);
                cellStyleC.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
                XSSFFont font = wb.createFont();
                font.setFontName("仿宋_GB2312");
                font.setBold(true);
                font.setFontHeightInPoints((short) 12);
                cellStyleC.setFont(font);
                cell.setCellStyle(cellStyleC);
                cell.setCellValue("可填选项标签(多选英文逗号隔开)");
                sheetPick.autoSizeColumn(0);
                int jj = 0;
                if (null != theList) {
                    for (Object objPick : theList) {
                        List<Object> records = new ArrayList<>(Arrays.asList(objPick.toString().split(",")));
                        XSSFRow rowContentPick = sheetPick.createRow(1 + jj);
                        Cell cellCon = rowContentPick.createCell(0);
                        XSSFCellStyle cellConStyle = wb.createCellStyle();
                        XSSFDataFormat format = wb.createDataFormat();
                        cellConStyle.setDataFormat(format.getFormat("@"));
                        XSSFFont fontCon = wb.createFont();
                        fontCon.setBold(false);
                        fontCon.setFontHeightInPoints((short) 11);
                        cellConStyle.setFont(fontCon);
                        cellCon.setCellStyle(cellStyleC);
                        cellCon.setCellValue(records.get(1).toString());
                        jj++;
                    }
                }
            }
            OutputStream bos = response.getOutputStream();
            wb.write(bos);
            bos.close();
        }
    }

    @Override
    public List<?> getExcelset(String modulename)throws Exception {
        List<Object> suppliers = XN_Query.contentQuery().tag(CommonUtil.TABLE_NAME)
                .filter("type", "eic", CommonUtil.TABLE_NAME)
                .notDelete()
                .filter("my.tablename", "=", modulename)
                .end(-1).execute();
        List<SortObject> list = new ArrayList<>();
        if (!suppliers.isEmpty()) {
            suppliers.forEach(item -> {
                Map<String, Object> map = ((Content) item).my;
                list.add(new SortObject((String)map.get("fieldname"), (String)map.get("fieldlabel"), (String)map.get("align"), map.get("uitype").toString(), (String)map.get("picklist"), Integer.valueOf(map.get("sequence").toString()), (String)map.get("bitian"), (String)map.get("congfuchk"), (String)map.get("mrz"), (String)map.get("linkmodule"), (String)map.get("typeofdata")));
            });
        }
        return list;
    }
}
