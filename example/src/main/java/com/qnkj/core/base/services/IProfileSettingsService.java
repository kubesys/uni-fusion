package com.qnkj.core.base.services;

import com.qnkj.core.base.entitys.ProfileSettings;

/**
 * @author Oldhand
 */
public interface IProfileSettingsService {

    /**
     * 修改用户系统配置（个性化配置）
     *
     * @param profileid 用户ID
     * @param info 配置数据
     */
    void updateSettings(String profileid, ProfileSettings info) throws Exception;

    /**
     * 获取用户系统配置（个性化配置）
     *
     * @param profileid 用户ID
     */
    ProfileSettings get(String profileid) throws Exception;

}
