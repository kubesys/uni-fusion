package com.qnkj.core.base.services;

import com.qnkj.core.base.entitys.MenuTree;

import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 */
public interface IMenuService {
    /**
     * 查找用户菜单集合
     *
     * @param profileid 用户ID
     * @return 用户菜单集合
     */
    MenuTree<Object> findUserMenus(String profileid, String usertype, String menuType) throws Exception;

    /**
     * 查找报表菜单集合
     *
     * @param profileid 用户ID
     * @return 用户菜单集合
     */
    MenuTree<Object> findReportMenus(String profileid, String usertype, String menuType) throws Exception;

    /**
     * 查找菜单组
     *
     * @param usertype 用户类型
     * @return 用户菜单组
     */
    List<Map<String, Object>> findMenuGroups(String profileid,String usertype, Boolean isdev) throws Exception;

    List<Map<String, Object>> findMenuGroups(Boolean isdev) throws Exception;
}
