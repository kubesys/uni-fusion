package com.qnkj.core.base.services;


import com.github.restapi.models.Profile;
import com.qnkj.core.base.entitys.RegisterInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 */
public interface IProfileService {

    /**
     * 创建用户
     *
     * @param profile 用户信息
     * @return 用户
     */
    Profile create(RegisterInfo profile) throws Exception;

    /**
     * 检测用户是否存在
     *
     * @param type 字段类型
     * @param value 值
     * @return 是否存在
     */
    boolean isExist(String type, String value) throws Exception;

    /**
     * 检测用户是否可以更换新手机号码
     *
     * @param profileid 用户ID
     * @param mobile 新手机号码
     * @return 是否存在
     */
    boolean allowChangeMobile(String profileid, String mobile) throws Exception;

    /**
     * 通过手机号码查找用户
     *
     * @param mobile 用户ID
     * @return 用户
     */
    Profile findByMobile(String mobile) throws Exception;

    /**
     * 通过用户名查找用户
     *
     * @param username 用户ID
     * @return 用户
     */
    Profile findByUsername(String username) throws Exception;

    /**
     * 通过用户ID查找用户
     *
     * @param profileid 用户ID
     * @return 用户
     */
    Profile load(String profileid) throws Exception;

    /**
     * 登录
     *
     * @param profileid 用户ID
     * @return 用户
     */
    boolean sign(String profileid, String password) throws Exception;

    /**
     * 更新用户头像
     *
     * @param profileid 用户ID
     * @param avatar   用户头像
     */
    void updateAvatar(String profileid, String avatar) throws Exception;

    /**
     * 更新用户头像
     *
     * @param profileid 用户ID
     * @param info   用户信息
     */
    void updateProfile(String profileid, Map<?,?> info) throws Exception;


    /**
     * 更新用户密码
     *
     * @param profileid 用户ID
     * @param password   密码
     */
    void updatePassword(String profileid, String password) throws Exception;


    /**
     * 更新用户手机号码
     *
     * @param profileid 用户ID
     * @param mobile   手机号码
     */
    void updateMobile(String profileid, String mobile) throws Exception;

    Object getProfileNameById(Object profileids);

    /**
     * 检查是否满足新增用户信息，满足返回"",不满足返回原因
     * wangwen
     */
    String checkProfile(Profile profile) throws Exception;

    /**
     * 新增或修改用户信息
     * @param profile
     * wangwen
     * @return
     * @throws Exception
     */
    Profile saveOrUpdate(Profile profile) throws Exception;

    /**
     * 新增
     * @param profile
     * wangwen
     * @return
     * @throws Exception
     */
    Profile insert(Profile profile) throws Exception;

    /**
     * 修改
     * @param profile
     * wangwen
     * @return
     * @throws Exception
     */
    Profile update(Profile profile) throws Exception;


    /**
     * 根据id取得用户名
     * wangwen
     * @param id
     * @return
     */
    String getGivenName(String id);

    Map<String, Object> getGivenNames(List<String> ids);
}
