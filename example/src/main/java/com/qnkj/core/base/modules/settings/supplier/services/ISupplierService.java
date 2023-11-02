package com.qnkj.core.base.modules.settings.supplier.services;

import com.github.restapi.models.Profile;
import com.qnkj.core.base.modules.settings.supplier.entitys.Supplier;
import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;
import java.util.List;

/**
 * create by Auto Generator
 * create date 2020-11-23
 */
public interface ISupplierService extends IBaseService {

    /**
     * load客户
     *
     * @param supplierid ID
     * @return 客户
     */
    Supplier load(String supplierid) throws Exception;

    /**
     * 客户进行数据初始化
     *
     * @param info
     */
    Profile submitAgreeAndInit(Supplier info, String approver) throws Exception;

    /**
     * 客户审批不同意
     *
     * @param supplierid ID
     * @param approver 审批人
     */
    void disAgree(String supplierid,String approver) throws Exception;

    /**
     * 更新注册企业信息
     * wangwen
     * @param id,httpRequest
     * @return
     */
    Supplier update(String id, HashMap<String, Object> httpRequest) throws Exception;

    /**
     * 检查注册企业信息是否符合要求
     * wangwen
     * @param supplier
     * @return
     * @throws Exception
     */
    String checkSupplier(Supplier supplier) throws Exception;

    /**
     * @Description 根据suppliers的id取得客户全称
     * @param id
     * @return
     * @Author wangwen
     * @Date 9:56 2020/9/21
     **/
    String getSuppliersName(String id);

    /**
     * 根据客户全称查询客户信息(所有的，包括删除的)
     * @param givenname
     * @return
     * @throws Exception
     */
    List<Supplier> getSuppliersBySuppliersName(String givenname) ;


    /**
     * 获取根据用户ID获得Supplierid
     *
     * */
    public String getSupplierid(String profileid) throws Exception;

    /**
     * 根据id集合获得对象集合
     * @param ids
     * @return
     */
    List<Supplier> findSuppliers(List<String> ids);

}
