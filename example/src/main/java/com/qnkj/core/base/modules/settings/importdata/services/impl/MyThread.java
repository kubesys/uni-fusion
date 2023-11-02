package com.qnkj.core.base.modules.settings.importdata.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.restapi.XN_Content;
import com.github.restapi.XN_Filter;
import com.github.restapi.XN_Query;
import com.github.restapi.XN_Rest;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.core.base.modules.settings.importdata.Util.CommonUtil;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.utils.PickListUtils;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.UserUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyThread implements Runnable {
    private String name;
    private File file;
    private String tableName;
    private String[] titleNames;
    private String[] titleCodes;
    private String[] titleUitype;
    private String[] titlePicklist;
    private String[] titlesBt;
    private HashMap<String,String> titlesCf;
    private String[] titlesMrz;
    private String[] titlesLink;
    private String[] titlesTypeData;
    private String drfs;
    JSONArray jsonArray;
    public List<Object> objs = new ArrayList<>();

    public MyThread(File file,String tableName, String[] titleNames, String[] titleCodes, String[] titleUitype, String[] titlePicklist, String[] titlesBt, HashMap<String,String> titlesCf, String[] titlesMrz, String[] titlesLink, String[] titlesTypeData, String drfs, JSONArray jsonArray){
        this.file = file;
        this.tableName = tableName;
        this.titleNames = titleNames;
        this.titleCodes = titleCodes;
        this.titleUitype = titleUitype;
        this.titlePicklist = titlePicklist;
        this.titlesBt = titlesBt;
        this.titlesCf = titlesCf;
        this.titlesMrz = titlesMrz;
        this.titlesLink = titlesLink;
        this.titlesTypeData = titlesTypeData;
        this.drfs = drfs;
        this.jsonArray = jsonArray;
    }

    @Override
    public void run(){
        StringBuilder errorMsg = new StringBuilder();
        try {
            SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
            XN_Rest.setApplication(saasUtils.getApplication());
            this.name = file.getAbsolutePath();
            //创建工作簿
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(this.file));
            //读取第一个工作表(这里的下标与list一样的，从0开始取，之后的也是如此)
            XSSFSheet sheet = workbook.getSheetAt(0);
            int maxRow = sheet.getLastRowNum();
            List<HashMap<String,String>> chkList = new ArrayList<>(1);
            List<String> chkAllList = new ArrayList<>(1);
            HashMap<String,String> mapPicklist = new HashMap<>();
            HashMap<String,String> mapLinklist = new HashMap<>();
            HashMap<String,String> mapDeptlist = new HashMap<>();
            HashMap<String,HashMap<String,String>> mapConPicklist = new HashMap<>();
            HashMap<String,HashMap<String,String>> mapConLinklist = new HashMap<>();
            HashMap<String,HashMap<String,String>> mapConDeptlist = new HashMap<>();
            int i = 0;
            for (String picklist:titlePicklist) {
                if (null!=picklist&&!"".equals(picklist)) {
                    if ("sfList".equals(picklist)) {
                        HashMap<String,String> mapContent = new HashMap<>();
                        mapContent.put("是","1");
                        mapContent.put("否","0");
                        mapPicklist.put(picklist,"是,否");
                        mapConPicklist.put(picklist,mapContent);
                    }else {
                        Object picklists = PickListUtils.getPickList(picklist);
                        List<Object> theList = (List<Object>)picklists;
                        StringBuilder contentStr = new StringBuilder();
                        HashMap<String,String> mapContent = new HashMap<>();
                        if (null!=theList) {
                            for (Object objPick:theList) {
                                String uitype = titleUitype[i];
                                List<Object> records = new ArrayList<>(Arrays.asList(objPick.toString().split(",")));
                                contentStr.append(records.get(1).toString()).append(",");
                                if ("14".equals(uitype)||"16".equals(uitype)||"20".equals(uitype)) {
                                    mapContent.put(records.get(1).toString(),records.get(2).toString());
                                }else {
                                    mapContent.put(records.get(1).toString(),records.get(0).toString().replaceAll("\\[",""));
                                }
                            }
                        }
                        mapPicklist.put(picklist,contentStr.toString());
                        mapConPicklist.put(picklist,mapContent);
                    }
                }
                i++;
            }
            for (String linklist:titlesLink) {
                if (null!=linklist&&!"".equals(linklist)) {
                    List<Object> suppliersLnk = XN_Query.contentQuery().tag(linklist)
                            .filter("type", "eic", linklist).notDelete()
                            .end(-1).execute();
                    List<Object> query = XN_Query.contentQuery().tag("entitynames")
                            .filter("type", "eic", "entitynames")
                            .filter("my.modulename", "=", linklist)
                            .notDelete().end(-1).execute();
                    String showFieldName = null;
                    if (!query.isEmpty()) {
                        for (Object item:query) {
                            Map<String, Object> mapLnkQ = ((Content)item).my;
                            showFieldName = mapLnkQ.get("fieldname").toString();
                        }
                    }
                    if (!suppliersLnk.isEmpty()) {
                        StringBuilder contentStr = new StringBuilder();
                        HashMap<String,String> mapContent = new HashMap<>();
                        for (Object item:suppliersLnk) {
                            Map<String, Object> mapLnk = ((Content)item).my;
                            StringBuilder val = new StringBuilder();
                            for (Map.Entry<String, Object> entryLnk : mapLnk.entrySet()) {
                                if (null!=entryLnk.getValue()&&!"".equals(entryLnk.getValue())&&!"0".equals(entryLnk.getValue())&&entryLnk.getKey().equals(showFieldName)) {
                                    val.append(entryLnk.getValue()).append(",");
                                }
                            }
                            String valStr = val.toString();
                            if (valStr.lastIndexOf(",")>0) {
                                contentStr.append(valStr.substring(0, valStr.lastIndexOf(","))).append(",");
                                mapContent.put(valStr.substring(0, valStr.lastIndexOf(",")), ((Content)item).id);
                            }
                        }
                        mapLinklist.put(linklist,contentStr.toString());
                        mapConLinklist.put(linklist,mapContent);
                    }
                }
            }
            for (String uitype:titleUitype) {
                if ("10".equals(uitype)&&null!=jsonArray) {
                    StringBuilder contentStr = new StringBuilder();
                    HashMap<String,String> mapContent = new HashMap<>();
                    for (Object o : jsonArray) {
                        JSONObject objDep = (JSONObject) o;
                        if (null != objDep) {
                            String departmentid = (String) objDep.get("departmentid");
                            String departmentname = (String) objDep.get("departmentname");
                            StringBuilder val = new StringBuilder();
                            if (null != departmentid && !"".equals(departmentid) && null != departmentname && !"".equals(departmentname)) {
                                val.append(departmentname).append(",");
                            }
                            String valStr = val.toString();
                            if (valStr.lastIndexOf(",") > 0) {
                                contentStr.append(valStr.substring(0, valStr.lastIndexOf(","))).append(",");
                                mapContent.put(valStr.substring(0, valStr.lastIndexOf(",")), departmentid);
                            }
                        }
                    }
                    mapDeptlist.put("10",contentStr.toString());
                    mapConDeptlist.put("10",mapContent);
                    break;
                }
            }
            SimpleDateFormat formatDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatT = new SimpleDateFormat("HH:mm:ss");
            List<Object> delrecords = new ArrayList<>();
            List<Content> cfrecords = new ArrayList<>();
            for (int row = 1; row <= maxRow; row++) {
                //获取最后单元格num，即总单元格数 ***注意：此处从1开始计数***
                int maxRol = sheet.getRow(row).getLastCellNum();
                StringBuilder contentChk = new StringBuilder();
                for (int rol = 0; rol < maxRol; rol++){
                    contentChk.append(null==sheet.getRow(row).getCell(rol)?"":sheet.getRow(row).getCell(rol).toString());
                }
                if ("".equals(contentChk.toString())) {
                    continue;
                }
                Content content = XN_Content.create(this.tableName,"",ProfileUtils.getCurrentProfileId(),0);
                content.add("deleted","0").add("supplierid", SupplierUtils.getSupplierid());
                HashMap<String,String> colMap = new HashMap<>(1);
                HashMap<String,String> colMapT = new HashMap<>(1);
                HashMap<String,String> colMapV = new HashMap<>(1);
                StringBuilder colAllStr = new StringBuilder();
                List<Object> filters = new ArrayList<>(1);
                List<Object> filtersAll = new ArrayList<>(1);
                String showCol = null;
                for (int rol = 0; rol < maxRol; rol++){
                    String rolDesc = CommonUtil.getColDesc(rol+1);
                    String cellVal = null==sheet.getRow(row).getCell(rol)?"":sheet.getRow(row).getCell(rol).toString().trim();
                    if ("updated".equals(titleCodes[rol])||"published".equals(titleCodes[rol])) {
                        continue;
                    }else if ("curuser".equals(titlesMrz[rol])) {
                        if (null!=cellVal&&!"".equals(cellVal)) {
                            String userId = UserUtils.getProfileIdByName(cellVal);
                            if (null==userId||"".equals(userId)) {
                                content.add(titleCodes[rol], cellVal);
                            }else {
                                content.add(titleCodes[rol], userId);
                            }
                        }else {
                            content.add(titleCodes[rol], ProfileUtils.getCurrentProfileId());
                        }
                    }else if ("curdatetime".equals(titlesMrz[rol])) {
                        if (null!=cellVal&&!"".equals(cellVal)) {
                            try {
                                formatDt.parse(cellVal);
                            }catch (ParseException e){
                                errorMsg.append(rolDesc+(row+1)+"导入内容格式不是要求的：yyyy-MM-dd HH:mm:ss</br>");
                            }
                            content.add(titleCodes[rol],cellVal);
                        }else {
                            content.add(titleCodes[rol], formatDt.format(new Date()));
                        }
                    }else if ("curdate".equals(titlesMrz[rol])) {
                        if (null!=cellVal&&!"".equals(cellVal)) {
                            try {
                                formatD.parse(cellVal);
                            }catch (ParseException e){
                                errorMsg.append(rolDesc+(row+1)+"导入内容格式不是要求的：yyyy-MM-dd</br>");
                            }
                            content.add(titleCodes[rol],cellVal);
                        }else {
                            content.add(titleCodes[rol], formatD.format(new Date()));
                        }
                    }else if ("curtime".equals(titlesMrz[rol])) {
                        if (null != cellVal && !"".equals(cellVal)) {
                            try {
                                formatT.parse(cellVal);
                            } catch (ParseException e) {
                                errorMsg.append(rolDesc + (row + 1) + "导入内容格式不是要求的：HH:mm:ss</br>");
                            }
                            content.add(titleCodes[rol], cellVal);
                        } else {
                            content.add(titleCodes[rol], formatT.format(new Date()));
                        }
                    }else {
                        if ((null==cellVal||"".equals(cellVal))&&null!=titlesMrz[rol]&&!"".equals(titlesMrz[rol])) {
                            content.add(titleCodes[rol], titlesMrz[rol]);
                        }
                        if ("true".equals(titlesBt[rol])&&(null==cellVal||"".equals(cellVal))&&(null==titlesMrz[rol]||"".equals(titlesMrz[rol]))) {
                            errorMsg.append(rolDesc).append(row + 1).append(" 内容不能为空</br>");
                        }
                        if ("6".equals(titleUitype[rol])&&!"".equals(cellVal)) {
                            try {
                                formatD.parse(cellVal);
                            }catch (ParseException e){
                                errorMsg.append(rolDesc+(row+1)+"导入内容格式不是要求的：yyyy-MM-dd</br>");
                            }
                        }else if ("7".equals(titleUitype[rol])&&!"".equals(cellVal)) {
                            try {
                                formatDt.parse(cellVal);
                            }catch (ParseException e){
                                errorMsg.append(rolDesc+(row+1)+"导入内容格式不是要求的：yyyy-MM-dd HH:mm:ss</br>");
                            }
                        }else if ("8".equals(titleUitype[rol])&&!"".equals(cellVal)) {
                            try {
                                formatT.parse(cellVal);
                            }catch (ParseException e) {
                                errorMsg.append(rolDesc+(row+1)+"导入内容格式不是要求的：HH:mm:ss</br>");
                            }
                        }else if ("27".equals(titleUitype[rol])&&!"".equals(cellVal)) {
                            try {
                                Double.parseDouble(cellVal);
                                int scaleNum = cellVal.substring(cellVal.indexOf(".")+1).length();
                                if (scaleNum>2) {
                                    errorMsg.append(rolDesc+(row+1)+"导入内容格式不是要求的精度：0.00</br>");
                                }
                            }catch (NumberFormatException e) {
                                errorMsg.append(rolDesc+(row+1)+"导入内容格式不是要求的精度：0.00</br>");
                            }
                        }
                        String regex = CommonUtil.getRegex(titlesTypeData[rol]);
                        if (!"".equals(cellVal)&&!"".equals(regex)) {
                            boolean flag = cellVal.matches(regex);
                            if (!flag) {
                                errorMsg.append(rolDesc).append(row + 1).append("导入内容格式不是要求的：").append(CommonUtil.getTitleDesc(titlesTypeData[rol])).append("</br>");
                            }
                        }
                        if (null!=titlePicklist[rol]&&!"".equals(titlePicklist[rol])) {
                            if ("15".equals(titleUitype[rol])||"16".equals(titleUitype[rol])) {
                                String[] chkAry = cellVal.trim().split(",");
                                StringBuilder newVal = new StringBuilder();
                                HashMap<String,String> inputMap = mapConPicklist.get(titlePicklist[rol]);
                                for (Map.Entry<String, String> entryInput : inputMap.entrySet()) {
                                    for (String chkVal:chkAry) {
                                        if (entryInput.getKey().trim().equals(chkVal)) {
                                            newVal.append(entryInput.getValue().trim()).append(",");
                                        }else {
                                            continue;
                                        }
                                    }
                                }
                                int len = newVal.toString().split(",").length;
                                if (len<chkAry.length) {
                                    errorMsg.append(rolDesc+(row+1)+"导入内容格式不是要求的字典内容</br>");
                                }
                                cellVal = newVal.toString();
                            }else {
                                String contentStr = mapPicklist.get(titlePicklist[rol]);
                                if (null!=contentStr&&!"".equals(contentStr)&& !contentStr.contains(cellVal)) {
                                    errorMsg.append(rolDesc).append(row + 1).append("导入内容格式不是要求的字典内容</br>");
                                }
                                HashMap<String,String> inputMap = mapConPicklist.get(titlePicklist[rol]);
                                for (Map.Entry<String, String> entryInput : inputMap.entrySet()) {
                                    if (entryInput.getKey().trim().equals(cellVal.trim())) {
                                        cellVal = entryInput.getValue().trim();
                                        break;
                                    }
                                }
                            }
                        }
                        if (null!=titlesLink[rol]&&!"".equals(titlesLink[rol])) {
                            String contentStr = mapLinklist.get(titlesLink[rol]);
                            if (!contentStr.contains(cellVal)) {
                                errorMsg.append(rolDesc).append(row + 1).append("导入内容格式不是对应的关联内容</br>");
                            }
                            HashMap<String,String> inputLinkMap = mapConLinklist.get(titlesLink[rol]);
                            for (Map.Entry<String, String> entryLink : inputLinkMap.entrySet()) {
                                if (entryLink.getKey().trim().equals(cellVal.trim())) {
                                    cellVal = entryLink.getValue().trim();
                                    break;
                                }
                            }
                        }
                        if ("10".equals(titleUitype[rol])) {
                            String contentStr = mapDeptlist.get("10");
                            if (!contentStr.contains(cellVal)) {
                                errorMsg.append(rolDesc).append(row + 1).append("导入内容格式不是对应的部门内容</br>");
                            }
                            HashMap<String,String> inputDeptMap = mapConDeptlist.get(titlesLink[rol]);
                            if (null!=inputDeptMap) {
                                for (Map.Entry<String, String> entryDept : inputDeptMap.entrySet()) {
                                    if (entryDept.getKey().trim().equals(cellVal.trim())) {
                                        cellVal = entryDept.getValue().trim();
                                        break;
                                    }
                                }
                            }
                        }
                        if (null!=cellVal&&!"".equals(cellVal)) {
                            content.add(titleCodes[rol], cellVal);
                        }
                        for (Map.Entry<String, String> entryLst : titlesCf.entrySet()) {
                            if(entryLst.getValue().contains(titleCodes[rol])) {
                                if (null==colMap.get(entryLst.getKey())) {
                                    colMap.put(entryLst.getKey(),CommonUtil.unique(titleCodes[rol]));
                                    colMapT.put(entryLst.getKey(),CommonUtil.unique(titleNames[rol]));
                                    colMapV.put(entryLst.getKey(),CommonUtil.unique(cellVal));
                                }else {
                                    colMap.put(entryLst.getKey(),CommonUtil.unique(colMap.get(entryLst.getKey())+","+titleCodes[rol]));
                                    colMapT.put(entryLst.getKey(),CommonUtil.unique(colMapT.get(entryLst.getKey())+","+titleNames[rol]));
                                    colMapV.put(entryLst.getKey(),CommonUtil.unique(colMapV.get(entryLst.getKey())+","+cellVal));
                                }
                                if (!filters.contains(XN_Filter.filter("my." + titleCodes[rol], "=", cellVal))) {
                                    filters.add(XN_Filter.filter("my." + titleCodes[rol], "=", cellVal));
                                }
                            }
                        }
                        colAllStr.append(cellVal).append(",");
                        if (!filtersAll.contains(XN_Filter.filter("my." + titleCodes[rol], "=", cellVal))) {
                            filtersAll.add(XN_Filter.filter("my." + titleCodes[rol], "=", cellVal));
                        }
                    }
                }
                boolean noCf = true;
                for (HashMap<String,String> mapChk:chkList) {
                    for (Map.Entry<String, String> entryChk : colMapV.entrySet()) {
                        if (mapChk.containsKey(entryChk.getKey())&&entryChk.getValue().equals(mapChk.get(entryChk.getKey()))) {
                            noCf = false;
                            showCol = (null==showCol?"":showCol+",")+colMapT.get(entryChk.getKey());
                        }
                    }
                }
                chkList.add(colMapV);
                if (noCf) {
                    String filter = "";
                    if (filters.size()>0) {
                        filter = XN_Filter.all(filters);
                    }
                    List<Object> suppliers = XN_Query.contentQuery().tag(tableName)
                            .filter("type", "eic", tableName)
                            .notDelete()
                            .filter(filter)
                            .end(-1).execute();
                    if (!suppliers.isEmpty()) {
                        for (Object item:suppliers) {
                            if ("2".equals(drfs)) {
                                if (!cfrecords.contains(content)) {
                                    cfrecords.add(content);
                                }
                                content = null;
                            }else if ("3".equals(drfs)) {
                                if (!delrecords.contains(((Content)item).id)) {
                                    delrecords.add(((Content) item).id);
                                }
                            }
                        }
                    }
                }else {
                    if ("2".equals(drfs)) {
                        continue;
                    }else if ("3".equals(drfs)) {
                        String filter = "";
                        if (!filters.isEmpty()) {
                            filter = XN_Filter.all(filters);
                        }
                        List<Object> suppliers = XN_Query.contentQuery().tag(tableName)
                                .filter("type", "eic", tableName)
                                .notDelete()
                                .filter(filter)
                                .end(-1).execute();
                        if (!suppliers.isEmpty()) {
                            suppliers.forEach(item -> {
                                if (!delrecords.contains(((Content)item).id)) {
                                    delrecords.add(((Content) item).id);
                                }
                            });
                        }
                        if (colMapT.size()>0) {
                            StringBuilder colstrShow = new StringBuilder();
                            for (Map.Entry<String, String> entryCf : colMapT.entrySet()) {
                                colstrShow.append(entryCf.getValue()).append(",");
                            }
                            String showStr = colstrShow.toString();
                            if (null!=showCol) {
                                showStr = CommonUtil.unique(showCol);
                            }else {
                                showStr = showStr.substring(0,showStr.lastIndexOf(","));
                            }
                            errorMsg.append("【").append(showStr).append("】第").append(row + 1).append("行，导入内容没有通过重复检查</br>");
                        }
                    }else {
                        if (colMapT.size()>0) {
                            StringBuilder colstrShow = new StringBuilder();
                            for (Map.Entry<String, String> entryCf : colMapT.entrySet()) {
                                colstrShow.append(entryCf.getValue()).append(",");
                            }
                            String showStr = colstrShow.toString();
                            if (null!=showCol) {
                                showStr = CommonUtil.unique(showCol);
                            }else {
                                showStr = showStr.lastIndexOf(",")>=0?showStr.substring(0,showStr.lastIndexOf(",")):showStr;
                            }
                            errorMsg.append("【").append(showStr).append("】第").append(row + 1).append("行，导入内容没有通过重复检查</br>");
                        }
                    }
                }
                if (!chkAllList.contains(colAllStr.toString())&&colAllStr.length()>0) {
                    chkAllList.add(colAllStr.toString());
                    String filter = "";
                    if (filtersAll.size()>0) {
                        filter = XN_Filter.all(filtersAll);
                    }
                    List<Object> suppliers = XN_Query.contentQuery().tag(tableName)
                            .filter("type", "eic", tableName)
                            .notDelete()
                            .filter(filter)
                            .end(-1).execute();
                    if (!suppliers.isEmpty()) {
                        for (Object item:suppliers) {
                            if ("2".equals(drfs)) {
                                if (!cfrecords.contains(content)) {
                                    cfrecords.add(content);
                                }
                                content = null;
                            }else if ("3".equals(drfs)) {
                                if (!delrecords.contains(((Content)item).id)) {
                                    delrecords.add(((Content) item).id);
                                }
                            }
                        }
                    }
                    if (null!=content) {
                        objs.add(content);
                    }
                }else {
                    if ("2".equals(drfs)) {
                        continue;
                    }else if ("3".equals(drfs)) {
                        String filter = "";
                        if (filtersAll.size()>0) {
                            filter = XN_Filter.all(filtersAll);
                        }
                        List<Object> suppliers = XN_Query.contentQuery().tag(tableName)
                                .filter("type", "eic", tableName)
                                .notDelete()
                                .filter(filter)
                                .end(-1).execute();
                        if (!suppliers.isEmpty()) {
                            suppliers.forEach(item -> {
                                if (!delrecords.contains(((Content)item).id)) {
                                    delrecords.add(((Content)item).id);
                                }
                            });
                        }
                        objs.add(content);
                    }
                }
            }
            if ("".equals(errorMsg.toString())) {
                int numGx = 0;
                if ("3".equals(drfs)) {
                    XN_Content.delete(delrecords, tableName);
                    numGx = delrecords.size();
                }
                int numIn = objs.size();
                if (numIn>0) {
                    XN_Content.batchsave(objs, this.tableName);
                    File theFile = new File(this.file.getAbsolutePath());
                    if(theFile.delete()){}
                    if (numGx>0) {
                        throw new RuntimeException("已成功更新："+numGx+" 条记录</br>已成功导入：" + (Math.max((numIn - numGx), 0)) + " 条记录");
                    }else {
                        if ("2".equals(drfs)) {
                            int numCf = cfrecords.size();
                            throw new RuntimeException("已成功导入：" + numIn + " 条记录</br>重复未导入："+numCf+" 条记录");
                        }else {
                            throw new RuntimeException("已成功导入：" + numIn + " 条记录");
                        }
                    }
                }else {
                    if ("2".equals(drfs)) {
                        throw new RuntimeException("重复记录已跳过");
                    }else {
                        throw new RuntimeException("没有可以导入的记录");
                    }
                }
            }else {
                File theFile = new File(this.file.getAbsolutePath());
                if(theFile.delete()) {}
                throw new RuntimeException("导入失败，未导入任何数据，原因如下：</br>"+errorMsg.toString());
            }
        } catch(Exception e) {
            if (e.getMessage().contains("导入失败，")) {
                throw new RuntimeException(e.getMessage());
            }else {
                throw new RuntimeException("导入失败，未导入任何数据，原因如下：</br>"+e.getMessage());
            }

        }
    }


}
