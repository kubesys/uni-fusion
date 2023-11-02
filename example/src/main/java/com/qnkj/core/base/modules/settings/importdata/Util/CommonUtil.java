package com.qnkj.core.base.modules.settings.importdata.Util;

import com.google.common.base.Joiner;
import com.qnkj.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author ljx
 */
@Slf4j
public class CommonUtil {
    private CommonUtil() {}
    public static final String TABLE_NAME = "importdatadcsz";
    public static File mutipartFileToFile(MultipartFile file) {
        File toFile = null;
        if (null!=file && file.getOriginalFilename() != null && !"".equals(file.getOriginalFilename()) && file.getSize()>0 ) {
            try(InputStream ins = file.getInputStream() ) {
                toFile = new File(file.getOriginalFilename());
                inputStreamToFile(ins, toFile);
            }catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return toFile;
    }
    private static void inputStreamToFile(InputStream ins, File file) {
        try(OutputStream os = new FileOutputStream(file)) {
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead=ins.read(buffer,0,8192))!=-1) {
                os.write(buffer, 0 , bytesRead);
            }
        }catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static String unique(String arrStr){
        if (null==arrStr||"".equals(arrStr)) {
            return "";
        }
        String[] arr = arrStr.split(",");
        List<String> list = new ArrayList<>();
        for (String s : arr) {
            if (!list.contains(s)) {//检索arr1中是否含有arr中的值
                list.add(s);
            }
        }
        return Joiner.on(",").join(list);
    }

    public static ArrayList<HashMap<String,String>> uniqueListMap(List<HashMap<String,String>> list){
        if (null==list||list.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<HashMap<String,String>> newList = new ArrayList<>();
        List<String> listKey = new ArrayList<>();
        for (HashMap<String,String> map:list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!listKey.contains(entry.getKey())) {
                    listKey.add(entry.getKey());
                    newList.add(map);
                }
            }
        }
        return newList;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            if (obj.toString().isEmpty()) {
                return true;
            }
        } else if (obj instanceof List) {
            if (((List) obj).isEmpty()) {
                return true;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).isEmpty()) {
                return true;
            }
        } else if (obj instanceof String[] && ((String[])obj).length <= 0){
            return true;
        }
        return false;
    }

    public static void setDataValidationList(short firstRow, short endRow, short firstCol, short endCol, String[] textlist, XSSFSheet sheet ){
        if (null!=textlist) {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)dvHelper.createExplicitListConstraint(textlist);
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
            XSSFDataValidation dataValidationList = (XSSFDataValidation)dvHelper.createValidation(dvConstraint,addressList);
            dataValidationList.setSuppressDropDownArrow(true);
            sheet.addValidationData(dataValidationList);
        }
    }

    /**
     * 获取网络请求中参数
     *
     * @param request HttpServletRequest
     * @return HashMap
     */
    public static HashMap<String, Object> getRequestQuery(HttpServletRequest request) {
        HashMap<String, Object> query = new HashMap<>(1);
        Enumeration<String> parameteNames = request.getParameterNames();
        while(parameteNames.hasMoreElements()){
            String parameteName = parameteNames.nextElement();
            if((parameteName.startsWith("{") && parameteName.endsWith("}")) || (parameteName.startsWith("[") && parameteName.endsWith("]")) ||
                (parameteName.startsWith("bt[") && parameteName.endsWith("]")) || (parameteName.startsWith("cf[") && parameteName.endsWith("]"))){
                String josnStr = parameteName.replace("bt","").replace("cf","");
                Object obj = Utils.jsonToObject(josnStr);
                if(!isEmpty(obj)){
                    query.put("data",obj);
                }else {
                    if (parameteName.startsWith("bt[") && parameteName.endsWith("]")) {
                        List<HashMap<String,String>> listParamVal = (List)query.get("bt");
                        if (null==listParamVal) {
                            listParamVal = new ArrayList<>();
                        }
                        String[] parameteValues = request.getParameterValues(parameteName);
                        String parameteValue = "";
                        if (parameteValues.length == 1 && "on".equals(parameteValues[0])) {
                            parameteValue = "true";
                        }
                        HashMap<String,String> mapVal = new HashMap<>();
                        mapVal.put(josnStr.replaceAll("\\[","").replaceAll("\\]",""),parameteValue);
                        listParamVal.add(mapVal);
                        query.put("bt", listParamVal);
                    }else if (parameteName.startsWith("cf[") && parameteName.endsWith("]")) {
                        List<HashMap<String,String>> listParamVal = (List)query.get("cf");
                        if (null==listParamVal) {
                            listParamVal = new ArrayList<>();
                            HashMap<String,String> mapVal = new HashMap<>();
                            String str = josnStr.replaceAll("\\[","").replaceAll("\\]","");
                            String[] strAry = str.split(",");
                            if (strAry.length>1) {
                                for (int jj=1,size=strAry.length; jj<size; jj++) {
                                    mapVal.put(strAry[jj],strAry[0]);
                                }
                            }
                            listParamVal.add(mapVal);
                        }else {
                            HashMap<String,String> mapVal = new HashMap<>();
                            String str = josnStr.replaceAll("\\[","").replaceAll("\\]","");
                            String[] strAry = str.split(",");
                            if (strAry.length>1) {
                                for (int jj=1,size=strAry.length; jj<size; jj++) {
                                    for (int jjj=0,sizeLst=listParamVal.size(); jjj<sizeLst; jjj++) {
                                        HashMap<String,String> mapLst = listParamVal.get(jjj);
                                        for (Map.Entry<String, String> entryLst : mapLst.entrySet()) {
                                            if (entryLst.getKey().equals(strAry[jj])) {
                                                mapVal.put(strAry[jj],entryLst.getValue()+","+strAry[0]);
                                            }else {
                                                mapVal.put(strAry[jj],strAry[0]);
                                            }
                                        }
                                    }
                                }
                            }
                            listParamVal.add(mapVal);
                        }
                        query.put("cf", listParamVal);
                    }
                }
            }else {
                String[] parameteValues = request.getParameterValues(parameteName);
                Object parameteValue;
                if (parameteValues.length == 1&&"".equals(parameteValues[0])) {
                    continue;
                }else if (parameteValues.length == 1) {
                    if ("on".equals(parameteValues[0])) {
                        parameteValue = true;
                    } else {
                        parameteValue = parameteValues[0];
                    }
                } else if (parameteValues.length > 1) {
                    parameteValue = Arrays.asList(parameteValues);
                } else {
                    parameteValue = "";
                }
                if (parameteValue instanceof String) {
                    try {
                        parameteValue = URLDecoder.decode(parameteValue.toString(),"UTF-8");
                    } catch (Exception ignored) { }
                    if ((parameteValue.toString().startsWith("{") && parameteValue.toString().endsWith("}")) ||
                            (parameteValue.toString().startsWith("[") && parameteValue.toString().endsWith("]"))) {
                        Object obj = Utils.jsonToObject(parameteValue.toString());
                        if (!isEmpty(obj)) {
                            parameteValue = obj;
                        } else if (null!=parameteValue) {
                            parameteValue = Arrays.asList(parameteValue.toString().split(","));
                        }
                    }
                }
                if (parameteName.matches("(.*)\\[(\\d*)\\]$")) {
                    parameteName = parameteName.substring(0, parameteName.indexOf("["));
                } else if (parameteName.endsWith(".id")) {
                    parameteName = parameteName.substring(0, parameteName.indexOf("."));
                    if (parameteValue.toString().contains(",")) {
                        parameteValue = Arrays.asList(parameteValue.toString().split(","));
                    }
                }
                if(query.containsKey(parameteName)){
                    if(query.get(parameteName) instanceof List){
                        if(parameteValue instanceof List){
                            ((List)query.get(parameteName)).addAll((List)parameteValue);
                        }else {
                            ((List) query.get(parameteName)).add(parameteValue);
                        }
                    } else {
                        query.put(parameteName,new ArrayList<>(Arrays.asList(query.get(parameteName),parameteValue)));
                    }
                }else {
                    query.put(parameteName, parameteValue);
                }
            }
        }
        parameteNames = request.getAttributeNames();
        while(parameteNames.hasMoreElements()){
            String parameteName = parameteNames.nextElement();
            if(!parameteName.contains(".")){
                Object parameteValue = request.getAttribute(parameteName);
                query.put(parameteName,parameteValue);
            }
        }
        return query;
    }

    public static String getTitleDesc(String typedata) {
        if ("V".equals(typedata)) {
            return "";
        }else if ("NS".equals(typedata)) {
            return "无特殊字符的标准字符串";
        }else if ("NN".equals(typedata)) {
            return "数字";
        }else if ("L".equals(typedata)) {
            return "纯字母";
        }else if ("LN".equals(typedata)) {
            return "字母加数字";
        }else if ("IN".equals(typedata)) {
            return "整数";
        }else if ("MO".equals(typedata)) {
            return "手机号";
        }else if ("PH".equals(typedata)) {
            return "座机号";
        }else if ("PHMO".equals(typedata)) {
            return "手机或座机号";
        }else if ("QQ".equals(typedata)) {
            return "QQ号";
        }else if ("EM".equals(typedata)) {
            return "邮箱";
        }else if ("ID".equals(typedata)) {
            return "身份证号";
        }else if ("MONEY".equals(typedata)) {
            return "货币";
        }
        return "";
    }

    public static String getRegex(String typedata) {
        if ("V".equals(typedata)) {
            return "";
        }else if ("NS".equals(typedata)) {
            return "[`~!@#$%^&*()_\\-+=<>?:\"{}|,.\\/;'\\\\[\\]·~！@#￥%……&*（）——\\-+={}|《》？：“”【】、；‘'，。、]";
        }else if ("NN".equals(typedata)) {
            return "^[0-9]*$";
        }else if ("L".equals(typedata)) {
            return "^[A-Za-z]+$";
        }else if ("LN".equals(typedata)) {
            return "^[A-Za-z0-9]+$";
        }else if ("IN".equals(typedata)) {
            return "^+?[1-9][0-9]*$";
        }else if ("MO".equals(typedata)) {
            return "^1[0-9]{10}$";
        }else if ("PH".equals(typedata)) {
            return "^((\\d{3,4}-)";
        }else if ("PHMO".equals(typedata)) {
            return "(^(\\d{3,4}-)?\\d{7,8})$|(13[0-9]{9})";
        }else if ("QQ".equals(typedata)) {
            return "^[1-9]\\d{4,8}$";
        }else if ("EM".equals(typedata)) {
            return "^\\w+([-+.]\\w+)@\\w+([-.]\\w+).\\w+([-.]\\w+)*$";
        }else if ("ID".equals(typedata)) {
            return "^\\d{15}|\\d{18}$";
        }else if ("MONEY".equals(typedata)) {
            return "^[0-9]+(.[0-9]{2})?$";
        }
        return "";
    }

    public static String getColDesc(int rol) {
        switch (rol) {
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "C";
            case 4:
                return "D";
            case 5:
                return "E";
            case 6:
                return "F";
            case 7:
                return "G";
            case 8:
                return "H";
            case 9:
                return "I";
            case 10:
                return "J";
            case 11:
                return "K";
            case 12:
                return "L";
            case 13:
                return "M";
            case 14:
                return "N";
            case 15:
                return "O";
            case 16:
                return "P";
            case 17:
                return "Q";
            case 18:
                return "R";
            case 19:
                return "S";
            case 20:
                return "T";
            case 21:
                return "U";
            case 22:
                return "V";
            case 23:
                return "W";
            case 24:
                return "X";
            case 25:
                return "Y";
            case 26:
                return "Z";
            case 27:
                return "AA";
            case 28:
                return "AB";
            case 29:
                return "AC";
            case 30:
                return "AD";
            case 31:
                return "AE";
            case 32:
                return "AF";
            case 33:
                return "AG";
            case 34:
                return "AH";
            case 35:
                return "AI";
            case 36:
                return "AJ";
            case 37:
                return "AK";
            case 38:
                return "AL";
            case 39:
                return "AM";
            case 40:
                return "AN";
            case 41:
                return "AO";
            case 42:
                return "AP";
            case 43:
                return "AQ";
            case 44:
                return "AR";
            case 45:
                return "AS";
            case 46:
                return "AT";
            case 47:
                return "AU";
            case 48:
                return "AV";
            case 49:
                return "AW";
            case 50:
                return "AX";
            case 51:
                return "AY";
            case 52:
                return "AZ";
            case 53:
                return "BA";
            case 54:
                return "BB";
            case 55:
                return "BC";
            case 56:
                return "BC";
            case 57:
                return "BD";
            case 58:
                return "BE";
            case 59:
                return "BF";
            case 60:
                return "BG";
            case 61:
                return "BH";
            case 62:
                return "BI";
            case 63:
                return "BJ";
            case 64:
                return "BK";
            case 65:
                return "BL";
            case 66:
                return "BM";
            case 67:
                return "BN";
            case 68:
                return "BO";
            case 69:
                return "BP";
            case 70:
                return "BQ";
            case 71:
                return "BR";
            case 72:
                return "BS";
            case 73:
                return "BT";
            case 74:
                return "BU";
            case 75:
                return "BV";
            case 76:
                return "BW";
            case 77:
                return "BX";
            case 78:
                return "BY";
            case 79:
                return "BZ";
            case 80:
                return "CA";
            case 81:
                return "CB";
            case 82:
                return "CC";
            case 83:
                return "CD";
            case 84:
                return "CE";
            case 85:
                return "CF";
            case 86:
                return "CG";
            case 87:
                return "CH";
            case 88:
                return "CI";
            case 89:
                return "CJ";
            case 90:
                return "CK";
            case 91:
                return "CL";
            case 92:
                return "CM";
            case 93:
                return "CN";
            case 94:
                return "CO";
            case 95:
                return "CP";
            case 96:
                return "CQ";
            case 97:
                return "CR";
            case 98:
                return "CS";
            case 99:
                return "CT";
            case 100:
                return "CU";
            default:
                return "A";
        }
    }
}
