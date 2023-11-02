package com.qnkj.core.base.modules.lcdp.pagedesign.Util;

import com.qnkj.common.entitys.TabField;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageDesignField;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageDesignUtils {

    /**
     * 数据类型ID<br>
     * * 1：字符串类型;<br>
     * * 2：新建时可编辑的文本框;<br>
     * * 3：密码类型输入框;<br>
     * * 4：编号型字段文本框;<br>
     * * 5: 是否选择框<br>
     * * 6：日期型文本框;<br>
     * * 7：日期带时分型文本框;<br>
     * * 8：时间型文本框;<br>
     * * 9：颜色选择框;<br>
     * * 10：部门选择类型;<br>
     * * 11：弹窗选择类型（单行）;<br>
     * * 12：弹窗选择类型（多行）;<br>
     * * 13：单项选择类型;(字典)<br>
     * * 14：单项选择取值类型;(字典)<br>
     * * 15：多选框类型;(字典)<br>
     * * 16：多选框取值类型;(字典)<br>
     * * 17：图片类型;<br>
     * * 18：附件类型;<br>
     * * 19：下拉选择类型;(字典)<br>
     * * 20：下拉选择取值类型;(字典)<br>
     * * 21：下拉树结构类型;<br>
     * * 22：上下分隔线类型;<br>
     * * 23：多行文本框;<br>
     * * 24：文本编辑器;<br>
     * * 25：用户选择类型;<br>
     * * 26：地址区域选择类型;<br>
     * * 27：货币型类型;<br>
     * * 28: 星级评分;<br>
     * * 29: 自定义组件;<br>
     * * 30: 普通文本行（只用于显示）<br>
     * * 31：HTML文本域（只用于显示）<br>
     */

    private static final List<Integer> stringUiTypes = Arrays.asList(1,2,3,4,5,9,10,11,13,14,19,20,23,25,26);
    private static final List<Integer> datetimeUiTypes = Arrays.asList(6,7);
    private static final List<Integer> numberUiTypes = Arrays.asList(27,28);
    private static final List<Integer> picklistUiTypes = Arrays.asList(13,14,19,20,5);
    private static final String regDigitEx="[^0-9.]";

    public static String getUiType(Integer UiType) {
        if (picklistUiTypes.contains(UiType)) {
            return "picklist";
        } else if (UiType == 25) {
            return "profile";
        } else if (UiType == 10) {
            return "department";
        }
        return "";
    }
    public static String getDigit(String text) {
        Pattern p = Pattern.compile(regDigitEx);
        Matcher m = p.matcher(text);
        return m.replaceAll("").trim();
    }
    public static TabField getModuleFieldInfo(String module,String fieldname) {
        BaseEntityUtils baseEntity = BaseEntityUtils.init(module);
        if (Utils.isNotEmpty(baseEntity)) {
            Map<String, TabField> fields = baseEntity.getModuleField();
            if (Utils.isNotEmpty(fields)) {
                if (fields.containsKey(fieldname)) {
                    return fields.get(fieldname);
                }
            }
        }
        return null;
    }

    public static Map<String, Object> getModuleInfo(String module) throws Exception {
        BaseEntityUtils baseEntity = BaseEntityUtils.init(module);
        if (Utils.isEmpty(baseEntity)) {
            throw new Exception("无法获得模块信息");
        }
        Map<String,Object> infoMap = new HashMap<>(1);
        infoMap.put("TabName", baseEntity.getTabName());
        infoMap.put("ModuleName", baseEntity.getModuleName());
        infoMap.put("ModuleLabel", baseEntity.getModuleLabel());
        infoMap.put("DataRole", baseEntity.getModuletab().datarole.toString());
        infoMap.put("DataType", baseEntity.getDataType());
        return infoMap;
    }
    public static HashMap<String,PageDesignField> getModuleFields(String module) throws Exception {
        BaseEntityUtils baseEntity = BaseEntityUtils.init(module);
        if (Utils.isEmpty(baseEntity)) {
            throw new Exception("无法获得模块信息");
        }
        Map<String, TabField> fields = baseEntity.getModuleField();
        HashMap<String, PageDesignField> maps = new HashMap<>();
        if (!Utils.isEmpty(fields)) {
            fields.forEach((k1, v1) -> {
                if (stringUiTypes.contains(v1.uitype)) {
                    if (v1.uitype == 1) {
                        if (v1.typeofdata.startsWith("MONEY~") || v1.typeofdata.startsWith("IN~") || v1.typeofdata.startsWith("NN~")) {
                            maps.put(v1.fieldname,new PageDesignField(v1.fieldlabel, v1.fieldname,"number",v1.uitype));
                        } else {
                            maps.put(v1.fieldname,new PageDesignField(v1.fieldlabel, v1.fieldname,"String",v1.uitype));
                        }
                    } else {
                        maps.put(v1.fieldname,new PageDesignField(v1.fieldlabel, v1.fieldname,"String",v1.uitype));
                    }
                } else if (datetimeUiTypes.contains(v1.uitype)) {
                    maps.put(v1.fieldname,new PageDesignField(v1.fieldlabel, v1.fieldname,"datetime",v1.uitype));
                } else if (numberUiTypes.contains(v1.uitype)) {
                    maps.put(v1.fieldname,new PageDesignField(v1.fieldlabel, v1.fieldname,"number",v1.uitype));
                }
            });
            maps.put("author",new PageDesignField("创建者", "author","string",25));
            maps.put("title",new PageDesignField("标题", "title","string"));
            maps.put("published",new PageDesignField("创建时间", "published","datetime"));
            return maps;
        }
        throw new Exception("无法获得模块字段信息");
    }
    public static HashMap<String, Object> getModulePageDesignFields(String module) throws Exception {
        BaseEntityUtils baseEntity = BaseEntityUtils.init(module);
        if (Utils.isEmpty(baseEntity)) {
            throw new Exception("无法获得模块信息");
        }
        Map<String, TabField> fields = baseEntity.getModuleField();
        HashMap<String, Object> info = new HashMap<>();
        List<PageDesignField> xAxis = new ArrayList<>();
        List<PageDesignField> yAxis = new ArrayList<>();
        yAxis.add(new PageDesignField("记录数", "id","string"));
        if (!Utils.isEmpty(fields)) {
            fields.forEach((k1, v1) -> {
                if (stringUiTypes.contains(v1.uitype)) {
                    if (v1.uitype == 1) {
                        if (v1.typeofdata.startsWith("MONEY~") || v1.typeofdata.startsWith("IN~") || v1.typeofdata.startsWith("NN~")) {
                            yAxis.add(new PageDesignField(v1.fieldlabel, v1.fieldname,"number"));
                        } else {
                            xAxis.add(new PageDesignField(v1.fieldlabel, v1.fieldname,"String"));
                        }
                    } else {
                        xAxis.add(new PageDesignField(v1.fieldlabel, v1.fieldname,"String"));
                    }
                } else if (datetimeUiTypes.contains(v1.uitype)) {
                    xAxis.add(new PageDesignField(v1.fieldlabel, v1.fieldname,"datetime"));
                } else if (numberUiTypes.contains(v1.uitype)) {
                    yAxis.add(new PageDesignField(v1.fieldlabel, v1.fieldname,"number"));
                }
            });
            xAxis.add(new PageDesignField("创建者", "author","string"));
            xAxis.add(new PageDesignField("创建时间", "published","datetime"));
            info.put("xAxis",xAxis);
            info.put("yAxis",yAxis);
            return info;
        }
        throw new Exception("无法获得模块设计字段信息");
    }



    private static final HashMap<String, String> cityMaps = new HashMap<>(1);

    static {
        cityMaps.put("北京","101010100");
        cityMaps.put("顺义","101010400");
        cityMaps.put("怀柔","101010500");
        cityMaps.put("通州","101010600");
        cityMaps.put("昌平","101010700");
        cityMaps.put("延庆","101010800");
        cityMaps.put("丰台","101010900");
        cityMaps.put("石景山","101011000");
        cityMaps.put("大兴","101011100");
        cityMaps.put("房山","101011200");
        cityMaps.put("密云","101011300");
        cityMaps.put("门头沟","101011400");
        cityMaps.put("平谷","101011500");
        cityMaps.put("八达岭","101011600");
        cityMaps.put("佛爷顶","101011700");
        cityMaps.put("汤河口","101011800");
        cityMaps.put("密云上甸子","101011900");
        cityMaps.put("斋堂","101012000");
        cityMaps.put("霞云岭","101012100");
        cityMaps.put("北京城区","101012200");
        cityMaps.put("海淀","101010200");
        cityMaps.put("天津","101030100");
        cityMaps.put("宝坻","101030300");
        cityMaps.put("东丽","101030400");
        cityMaps.put("西青","101030500");
        cityMaps.put("北辰","101030600");
        cityMaps.put("蓟县","101031400");
        cityMaps.put("汉沽","101030800");
        cityMaps.put("静海","101030900");
        cityMaps.put("津南","101031000");
        cityMaps.put("塘沽","101031100");
        cityMaps.put("大港","101031200");
        cityMaps.put("武清","101030200");
        cityMaps.put("宁河","101030700");
        cityMaps.put("上海","101020100");
        cityMaps.put("宝山","101020300");
        cityMaps.put("嘉定","101020500");
        cityMaps.put("南汇","101020600");
        cityMaps.put("浦东","101021300");
        cityMaps.put("青浦","101020800");
        cityMaps.put("松江","101020900");
        cityMaps.put("奉贤","101021000");
        cityMaps.put("崇明","101021100");
        cityMaps.put("徐家汇","101021200");
        cityMaps.put("闵行","101020200");
        cityMaps.put("金山","101020700");
        cityMaps.put("石家庄","101090101");
        cityMaps.put("张家口","101090301");
        cityMaps.put("承德","101090402");
        cityMaps.put("唐山","101090501");
        cityMaps.put("秦皇岛","101091101");
        cityMaps.put("沧州","101090701");
        cityMaps.put("衡水","101090801");
        cityMaps.put("邢台","101090901");
        cityMaps.put("邯郸","101091001");
        cityMaps.put("保定","101090201");
        cityMaps.put("廊坊","101090601");
        cityMaps.put("郑州","101180101");
        cityMaps.put("新乡","101180301");
        cityMaps.put("许昌","101180401");
        cityMaps.put("平顶山","101180501");
        cityMaps.put("信阳","101180601");
        cityMaps.put("南阳","101180701");
        cityMaps.put("开封","101180801");
        cityMaps.put("洛阳","101180901");
        cityMaps.put("商丘","101181001");
        cityMaps.put("焦作","101181101");
        cityMaps.put("鹤壁","101181201");
        cityMaps.put("濮阳","101181301");
        cityMaps.put("周口","101181401");
        cityMaps.put("漯河","101181501");
        cityMaps.put("驻马店","101181601");
        cityMaps.put("三门峡","101181701");
        cityMaps.put("济源","101181801");
        cityMaps.put("安阳","101180201");
        cityMaps.put("合肥","101220101");
        cityMaps.put("芜湖","101220301");
        cityMaps.put("淮南","101220401");
        cityMaps.put("马鞍山","101220501");
        cityMaps.put("安庆","101220601");
        cityMaps.put("宿州","101220701");
        cityMaps.put("阜阳","101220801");
        cityMaps.put("亳州","101220901");
        cityMaps.put("黄山","101221001");
        cityMaps.put("滁州","101221101");
        cityMaps.put("淮北","101221201");
        cityMaps.put("铜陵","101221301");
        cityMaps.put("宣城","101221401");
        cityMaps.put("六安","101221501");
        cityMaps.put("巢湖","101221601");
        cityMaps.put("池州","101221701");
        cityMaps.put("蚌埠","101220201");
        cityMaps.put("杭州","101210101");
        cityMaps.put("舟山","101211101");
        cityMaps.put("湖州","101210201");
        cityMaps.put("嘉兴","101210301");
        cityMaps.put("金华","101210901");
        cityMaps.put("绍兴","101210501");
        cityMaps.put("台州","101210601");
        cityMaps.put("温州","101210701");
        cityMaps.put("丽水","101210801");
        cityMaps.put("衢州","101211001");
        cityMaps.put("宁波","101210401");
        cityMaps.put("重庆","101040100");
        cityMaps.put("合川","101040300");
        cityMaps.put("南川","101040400");
        cityMaps.put("江津","101040500");
        cityMaps.put("万盛","101040600");
        cityMaps.put("渝北","101040700");
        cityMaps.put("北碚","101040800");
        cityMaps.put("巴南","101040900");
        cityMaps.put("长寿","101041000");
        cityMaps.put("黔江","101041100");
        cityMaps.put("万州天城","101041200");
        cityMaps.put("万州龙宝","101041300");
        cityMaps.put("涪陵","101041400");
        cityMaps.put("开县","101041500");
        cityMaps.put("城口","101041600");
        cityMaps.put("云阳","101041700");
        cityMaps.put("巫溪","101041800");
        cityMaps.put("奉节","101041900");
        cityMaps.put("巫山","101042000");
        cityMaps.put("潼南","101042100");
        cityMaps.put("垫江","101042200");
        cityMaps.put("梁平","101042300");
        cityMaps.put("忠县","101042400");
        cityMaps.put("石柱","101042500");
        cityMaps.put("大足","101042600");
        cityMaps.put("荣昌","101042700");
        cityMaps.put("铜梁","101042800");
        cityMaps.put("璧山","101042900");
        cityMaps.put("丰都","101043000");
        cityMaps.put("武隆","101043100");
        cityMaps.put("彭水","101043200");
        cityMaps.put("綦江","101043300");
        cityMaps.put("酉阳","101043400");
        cityMaps.put("秀山","101043600");
        cityMaps.put("沙坪坝","101043700");
        cityMaps.put("永川","101040200");
        cityMaps.put("福州","101230101");
        cityMaps.put("泉州","101230501");
        cityMaps.put("漳州","101230601");
        cityMaps.put("龙岩","101230701");
        cityMaps.put("晋江","101230509");
        cityMaps.put("南平","101230901");
        cityMaps.put("厦门","101230201");
        cityMaps.put("宁德","101230301");
        cityMaps.put("莆田","101230401");
        cityMaps.put("三明","101230801");
        cityMaps.put("兰州","101160101");
        cityMaps.put("平凉","101160301");
        cityMaps.put("庆阳","101160401");
        cityMaps.put("武威","101160501");
        cityMaps.put("金昌","101160601");
        cityMaps.put("嘉峪关","101161401");
        cityMaps.put("酒泉","101160801");
        cityMaps.put("天水","101160901");
        cityMaps.put("武都","101161001");
        cityMaps.put("临夏","101161101");
        cityMaps.put("合作","101161201");
        cityMaps.put("白银","101161301");
        cityMaps.put("定西","101160201");
        cityMaps.put("张掖","101160701");
        cityMaps.put("广州","101280101");
        cityMaps.put("惠州","101280301");
        cityMaps.put("梅州","101280401");
        cityMaps.put("汕头","101280501");
        cityMaps.put("深圳","101280601");
        cityMaps.put("珠海","101280701");
        cityMaps.put("佛山","101280800");
        cityMaps.put("肇庆","101280901");
        cityMaps.put("湛江","101281001");
        cityMaps.put("江门","101281101");
        cityMaps.put("河源","101281201");
        cityMaps.put("清远","101281301");
        cityMaps.put("云浮","101281401");
        cityMaps.put("潮州","101281501");
        cityMaps.put("东莞","101281601");
        cityMaps.put("中山","101281701");
        cityMaps.put("阳江","101281801");
        cityMaps.put("揭阳","101281901");
        cityMaps.put("茂名","101282001");
        cityMaps.put("汕尾","101282101");
        cityMaps.put("韶关","101280201");
        cityMaps.put("南宁","101300101");
        cityMaps.put("柳州","101300301");
        cityMaps.put("来宾","101300401");
        cityMaps.put("桂林","101300501");
        cityMaps.put("梧州","101300601");
        cityMaps.put("防城港","101301401");
        cityMaps.put("贵港","101300801");
        cityMaps.put("玉林","101300901");
        cityMaps.put("百色","101301001");
        cityMaps.put("钦州","101301101");
        cityMaps.put("河池","101301201");
        cityMaps.put("北海","101301301");
        cityMaps.put("崇左","101300201");
        cityMaps.put("贺州","101300701");
        cityMaps.put("贵阳","101260101");
        cityMaps.put("安顺","101260301");
        cityMaps.put("都匀","101260401");
        cityMaps.put("兴义","101260906");
        cityMaps.put("铜仁","101260601");
        cityMaps.put("毕节","101260701");
        cityMaps.put("六盘水","101260801");
        cityMaps.put("遵义","101260201");
        cityMaps.put("凯里","101260501");
        cityMaps.put("昆明","101290101");
        cityMaps.put("红河","101290301");
        cityMaps.put("文山","101290601");
        cityMaps.put("玉溪","101290701");
        cityMaps.put("楚雄","101290801");
        cityMaps.put("普洱","101290901");
        cityMaps.put("昭通","101291001");
        cityMaps.put("临沧","101291101");
        cityMaps.put("怒江","101291201");
        cityMaps.put("香格里拉","101291301");
        cityMaps.put("丽江","101291401");
        cityMaps.put("德宏","101291501");
        cityMaps.put("景洪","101291601");
        cityMaps.put("大理","101290201");
        cityMaps.put("曲靖","101290401");
        cityMaps.put("保山","101290501");
        cityMaps.put("呼和浩特","101080101");
        cityMaps.put("乌海","101080301");
        cityMaps.put("集宁","101080401");
        cityMaps.put("通辽","101080501");
        cityMaps.put("阿拉善左旗","101081201");
        cityMaps.put("鄂尔多斯","101080701");
        cityMaps.put("临河","101080801");
        cityMaps.put("锡林浩特","101080901");
        cityMaps.put("呼伦贝尔","101081000");
        cityMaps.put("乌兰浩特","101081101");
        cityMaps.put("包头","101080201");
        cityMaps.put("赤峰","101080601");
        cityMaps.put("南昌","101240101");
        cityMaps.put("上饶","101240301");
        cityMaps.put("抚州","101240401");
        cityMaps.put("宜春","101240501");
        cityMaps.put("鹰潭","101241101");
        cityMaps.put("赣州","101240701");
        cityMaps.put("景德镇","101240801");
        cityMaps.put("萍乡","101240901");
        cityMaps.put("新余","101241001");
        cityMaps.put("九江","101240201");
        cityMaps.put("吉安","101240601");
        cityMaps.put("武汉","101200101");
        cityMaps.put("黄冈","101200501");
        cityMaps.put("荆州","101200801");
        cityMaps.put("宜昌","101200901");
        cityMaps.put("恩施","101201001");
        cityMaps.put("十堰","101201101");
        cityMaps.put("神农架","101201201");
        cityMaps.put("随州","101201301");
        cityMaps.put("荆门","101201401");
        cityMaps.put("天门","101201501");
        cityMaps.put("仙桃","101201601");
        cityMaps.put("潜江","101201701");
        cityMaps.put("襄樊","101200201");
        cityMaps.put("鄂州","101200301");
        cityMaps.put("孝感","101200401");
        cityMaps.put("黄石","101200601");
        cityMaps.put("咸宁","101200701");
        cityMaps.put("成都","101270101");
        cityMaps.put("自贡","101270301");
        cityMaps.put("绵阳","101270401");
        cityMaps.put("南充","101270501");
        cityMaps.put("达州","101270601");
        cityMaps.put("遂宁","101270701");
        cityMaps.put("广安","101270801");
        cityMaps.put("巴中","101270901");
        cityMaps.put("泸州","101271001");
        cityMaps.put("宜宾","101271101");
        cityMaps.put("内江","101271201");
        cityMaps.put("资阳","101271301");
        cityMaps.put("乐山","101271401");
        cityMaps.put("眉山","101271501");
        cityMaps.put("凉山","101271601");
        cityMaps.put("雅安","101271701");
        cityMaps.put("甘孜","101271801");
        cityMaps.put("阿坝","101271901");
        cityMaps.put("德阳","101272001");
        cityMaps.put("广元","101272101");
        cityMaps.put("攀枝花","101270201");
        cityMaps.put("银川","101170101");
        cityMaps.put("中卫","101170501");
        cityMaps.put("固原","101170401");
        cityMaps.put("石嘴山","101170201");
        cityMaps.put("吴忠","101170301");
        cityMaps.put("西宁","101150101");
        cityMaps.put("黄南","101150301");
        cityMaps.put("海北","101150801");
        cityMaps.put("果洛","101150501");
        cityMaps.put("玉树","101150601");
        cityMaps.put("海西","101150701");
        cityMaps.put("海东","101150201");
        cityMaps.put("海南","101150401");
        cityMaps.put("济南","101120101");
        cityMaps.put("潍坊","101120601");
        cityMaps.put("临沂","101120901");
        cityMaps.put("菏泽","101121001");
        cityMaps.put("滨州","101121101");
        cityMaps.put("东营","101121201");
        cityMaps.put("威海","101121301");
        cityMaps.put("枣庄","101121401");
        cityMaps.put("日照","101121501");
        cityMaps.put("莱芜","101121601");
        cityMaps.put("聊城","101121701");
        cityMaps.put("青岛","101120201");
        cityMaps.put("淄博","101120301");
        cityMaps.put("德州","101120401");
        cityMaps.put("烟台","101120501");
        cityMaps.put("济宁","101120701");
        cityMaps.put("泰安","101120801");
        cityMaps.put("西安","101110101");
        cityMaps.put("延安","101110300");
        cityMaps.put("榆林","101110401");
        cityMaps.put("铜川","101111001");
        cityMaps.put("商洛","101110601");
        cityMaps.put("安康","101110701");
        cityMaps.put("汉中","101110801");
        cityMaps.put("宝鸡","101110901");
        cityMaps.put("咸阳","101110200");
        cityMaps.put("渭南","101110501");
        cityMaps.put("太原","101100101");
        cityMaps.put("临汾","101100701");
        cityMaps.put("运城","101100801");
        cityMaps.put("朔州","101100901");
        cityMaps.put("忻州","101101001");
        cityMaps.put("长治","101100501");
        cityMaps.put("大同","101100201");
        cityMaps.put("阳泉","101100301");
        cityMaps.put("晋中","101100401");
        cityMaps.put("晋城","101100601");
        cityMaps.put("吕梁","101101100");
        cityMaps.put("乌鲁木齐","101130101");
        cityMaps.put("石河子","101130301");
        cityMaps.put("昌吉","101130401");
        cityMaps.put("吐鲁番","101130501");
        cityMaps.put("库尔勒","101130601");
        cityMaps.put("阿拉尔","101130701");
        cityMaps.put("阿克苏","101130801");
        cityMaps.put("喀什","101130901");
        cityMaps.put("伊宁","101131001");
        cityMaps.put("塔城","101131101");
        cityMaps.put("哈密","101131201");
        cityMaps.put("和田","101131301");
        cityMaps.put("阿勒泰","101131401");
        cityMaps.put("阿图什","101131501");
        cityMaps.put("博乐","101131601");
        cityMaps.put("克拉玛依","101130201");
        cityMaps.put("拉萨","101140101");
        cityMaps.put("山南","101140301");
        cityMaps.put("阿里","101140701");
        cityMaps.put("昌都","101140501");
        cityMaps.put("那曲","101140601");
        cityMaps.put("日喀则","101140201");
        cityMaps.put("林芝","101140401");
        cityMaps.put("台北县","101340101");
        cityMaps.put("高雄","101340201");
        cityMaps.put("台中","101340401");
        cityMaps.put("海口","101310101");
        cityMaps.put("三亚","101310201");
        cityMaps.put("东方","101310202");
        cityMaps.put("临高","101310203");
        cityMaps.put("澄迈","101310204");
        cityMaps.put("儋州","101310205");
        cityMaps.put("昌江","101310206");
        cityMaps.put("白沙","101310207");
        cityMaps.put("琼中","101310208");
        cityMaps.put("定安","101310209");
        cityMaps.put("屯昌","101310210");
        cityMaps.put("琼海","101310211");
        cityMaps.put("文昌","101310212");
        cityMaps.put("保亭","101310214");
        cityMaps.put("万宁","101310215");
        cityMaps.put("陵水","101310216");
        cityMaps.put("西沙","101310217");
        cityMaps.put("南沙岛","101310220");
        cityMaps.put("乐东","101310221");
        cityMaps.put("五指山","101310222");
        cityMaps.put("琼山","101310102");
        cityMaps.put("长沙","101250101");
        cityMaps.put("株洲","101250301");
        cityMaps.put("衡阳","101250401");
        cityMaps.put("郴州","101250501");
        cityMaps.put("常德","101250601");
        cityMaps.put("益阳","101250700");
        cityMaps.put("娄底","101250801");
        cityMaps.put("邵阳","101250901");
        cityMaps.put("岳阳","101251001");
        cityMaps.put("张家界","101251101");
        cityMaps.put("怀化","101251201");
        cityMaps.put("黔阳","101251301");
        cityMaps.put("永州","101251401");
        cityMaps.put("吉首","101251501");
        cityMaps.put("湘潭","101250201");
        cityMaps.put("南京","101190101");
        cityMaps.put("镇江","101190301");
        cityMaps.put("苏州","101190401");
        cityMaps.put("南通","101190501");
        cityMaps.put("扬州","101190601");
        cityMaps.put("宿迁","101191301");
        cityMaps.put("徐州","101190801");
        cityMaps.put("淮安","101190901");
        cityMaps.put("连云港","101191001");
        cityMaps.put("常州","101191101");
        cityMaps.put("泰州","101191201");
        cityMaps.put("无锡","101190201");
        cityMaps.put("盐城","101190701");
        cityMaps.put("哈尔滨","101050101");
        cityMaps.put("牡丹江","101050301");
        cityMaps.put("佳木斯","101050401");
        cityMaps.put("绥化","101050501");
        cityMaps.put("黑河","101050601");
        cityMaps.put("双鸭山","101051301");
        cityMaps.put("伊春","101050801");
        cityMaps.put("大庆","101050901");
        cityMaps.put("七台河","101051002");
        cityMaps.put("鸡西","101051101");
        cityMaps.put("鹤岗","101051201");
        cityMaps.put("齐齐哈尔","101050201");
        cityMaps.put("大兴安岭","101050701");
        cityMaps.put("长春","101060101");
        cityMaps.put("延吉","101060301");
        cityMaps.put("四平","101060401");
        cityMaps.put("白山","101060901");
        cityMaps.put("白城","101060601");
        cityMaps.put("辽源","101060701");
        cityMaps.put("松原","101060801");
        cityMaps.put("吉林","101060201");
        cityMaps.put("通化","101060501");
        cityMaps.put("沈阳","101070101");
        cityMaps.put("鞍山","101070301");
        cityMaps.put("抚顺","101070401");
        cityMaps.put("本溪","101070501");
        cityMaps.put("丹东","101070601");
        cityMaps.put("葫芦岛","101071401");
        cityMaps.put("营口","101070801");
        cityMaps.put("阜新","101070901");
        cityMaps.put("辽阳","101071001");
        cityMaps.put("铁岭","101071101");
        cityMaps.put("朝阳","101071201");
        cityMaps.put("盘锦","101071301");
        cityMaps.put("大连","101070201");
        cityMaps.put("锦州","101070701");
    }
    public static String getCityId(String cityName) {
        if (cityMaps.containsKey(cityName)) {
            return cityMaps.get(cityName);
        }
        for (Map.Entry<String, String> entryCity : cityMaps.entrySet()) {
            if (entryCity.getKey().contains(cityName)) {
                return entryCity.getValue();
            }
        }
        //城市默认长沙id
        return "101250101";
    }
}
