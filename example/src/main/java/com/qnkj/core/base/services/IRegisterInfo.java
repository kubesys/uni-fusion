package com.qnkj.core.base.services;


import com.qnkj.core.base.entitys.RegisterInfo;

/**
 * @author Oldhand
 */
public interface IRegisterInfo {

    /**
     * 创建客户
     *
     * @param mobile 手机号码
     * @return 客户
     */
    RegisterInfo get(String mobile) throws Exception;

    /**
     * 更新用
     *
     * @param info 注册信息
     */
    void update(RegisterInfo info);

    /**
     * 提交审批
     *
     * @param mobile 手机号码
     */
    void submit(String mobile) throws Exception;

    /**
     * 检测客户是否存在
     *
     * @param name 客户名称
     * @return 是否存在
     */
    boolean isExistSupplier(String mobile,String name) throws Exception;

    /**
     * 检测用户是否存在
     *
     * @param mobile 手机号码
     * @param type 字段类型
     * @param value 值
     * @return 是否存在
     */
    boolean isExist(String mobile, String type, String value) throws Exception;


}
