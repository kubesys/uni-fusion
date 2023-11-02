package com.qnkj.core.base.services;

import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.entitys.CustomDataSearch;
import com.qnkj.common.entitys.CustomFieldSetting;
import com.qnkj.common.entitys.PrintConfig;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.utils.ProfileUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author clubs
 */
public interface IPublicService {

    Pattern MODULE_PATTERN = Pattern.compile("^(.*)\\.(\\w+)\\.services\\.impl$");

    default Boolean isCreatorPrivateUse() { return false; }
    /**
     * 给查询对像添加企业数据条件过滤
     */
    default int getLeftListViewWidth() {
         return 2;
    }
    /**
     * 给查询对像添加企业数据条件过滤
     * @param query 查询对像
     */
    static void addSupplierFilter(XN_Query query) {
        if(ProfileUtils.isSupplier()) {
            query.filter("my.supplierid", "=", SupplierUtils.getSupplierid());
        } else {
            if(!(ProfileUtils.isAdmin() || ProfileUtils.isAssistant())) {
                query.filter( "my.supplierid", "=", "0");
            }
        }
    }

    default Boolean getEditViewIsReadOnly(Object entity) {
        return null;
    }

    /**
     * 检查查询字段
     */
    static String checkFilter(String fieldname){
        if (!fieldname.startsWith("my.") && !Arrays.asList("id", "title", "author", "published", "updated", "application").contains(fieldname)) {
            return "my." + fieldname;
        } else {
            return fieldname;
        }
    }

    /**
     * 添加自定义查询接口
     * @param httpRequest 客户端请求参数集
     * @param viewEntitys 数据模块实体
     * @param query XN_Query查询对像
     */
    default void addQueryFilter(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys,XN_Query query) { }

    /**
     * 添加弹窗自定义查询接口
     * @param httpRequest 客户端请求参数集
     * @param viewEntitys 数据模块实体
     * @param query XN_Query查询对像
     * @param modulename 远程调用模块名
     * @param record 远程调用记录ID
     * @param linkage 联动数据
     */
    default void addPopupQueryFilter(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys,XN_Query query, String modulename, String record, String linkage) {}

    /**
     * 自定义编辑界面接口
     */
    default Object customEditViewEntity(String record, String fieldname, String fieldvalue) {
        return null;
    }

    /**
     * 自定义列表界面接口
     * Entitys:为当前模块的Entity集合
     */
    default void customListViewEntity(ArrayList<Object> entitys) throws Exception {}

    /**
     * 列表界面获取自定义行Style
     * Entitys:为当前模块的Entity集合
     */
    default Map<String,Object> getCustomListEntityStyles(ArrayList<Object> entitys) throws Exception {
        return new HashMap<>();
    }

    /**
     * 生成自定义列表页面数据结构
     */
    default Map<String,Object> getCustomListEntity(HashMap<String, Object> httpRequest) {
        return null;
    }

    /**
     * 生成自定义编辑页面数据
     */
    default Object getCustomEditEntity(String record) {
        return null;
    }

    /**
     * 自定义列表界面接口
     * records:为当前模块的表格最终显示的内容，
     */
    default void customListViewRecord(List<Map<String, Object>> records) throws Exception {}

    /**
     * 添加自定义查询
     */
    default Object addDataSearch(HashMap<String, Object> httpRequest) {
        return new CustomDataSearch();
    }

    /**
     * 获取弹窗字段列表
     */
    default List<String> getPopupEditViewFields() {
        return Collections.emptyList();
    }

    /**
     * 获取弹窗栏设置
     */
    default Integer getPopupEditViewColumnSetting() {
        return 1;
    }
    /**
     * 获取自定义字段设置
     */
    default List<CustomFieldSetting> getCustomFields() {
        return new ArrayList<>();
    }

    /**
     * 获取批量弹窗编辑字段列表
     */
    default List<String> getBatchEditViewFields() {
        return Collections.emptyList();
    }

    /**
     * 初始化模板数据
     */
    default void initModuleData() {
    }
    /**
     * 审批完成后回调接口
     * record：记录ID
     * approvalstatus：审批状态，2、通过；3、不通过
     */
    default void approvalFinished(String record, String approvalstatus) {}

    /**
     * 获取自定义打印列表配置
     */
    default List<PrintConfig> getCustomPrintConfig(String printkeyname) {
        return new ArrayList<>();
    }
    /**
     * 获取自定义打印列表数据
     */
    default List<Map<String,Object>> getCustomPrintFieldData(String record, String table,String printkeyname) {
        return new ArrayList<>();
    }
    /**
     * action验证函数
     */
    default Object actionVerify(String param, String modulename, String record, BaseEntityUtils viewEntitys){
        return false;
    }
    /**
     * 保存前
     */
    default void saveBefore(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys, Class<?> entity) throws Exception {

    }
    default void saveBefore(Content obj) throws Exception {

    }
    /**
     * 保存后
     */
    default void saveAfter(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys, Class<?> entity) throws Exception {

    }
    default void saveAfter(Content obj) throws Exception {

    }
    /**
     * 删除前
     */
    default void deleteBefore(List<String> ids) throws Exception {

    }

    /**
     * 删除后
     */
    default void deleteAfter(List<String> ids) throws Exception {

    }
    /**
     * 添加自定义查询
     */
    default Integer getLabelWidth() {
        return 120;
    }

}
