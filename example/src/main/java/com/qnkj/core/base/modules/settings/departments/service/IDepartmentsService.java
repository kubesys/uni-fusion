package com.qnkj.core.base.modules.settings.departments.service;

import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;
import java.util.Map;

public interface IDepartmentsService extends IBaseService {
    HashMap<String,Object> list();
    /**
     * 获取部门树结构数据，返回List数据集
     * departmentids：参数可为字符串或List数据集，指定需要排除的记录维一ID
     * */
    Object getRoleTree(Object departmentids);
    Object getRoleTree(Object departmentids,boolean isEdit);
    /**
     * 获取部门树结构数据，返回List数据集
     * */
    Object getRoleTreeByUsers(Object departmentids);
    /**
     * 根据部门ID生成部门树的一个节点数据集
     * roleid：部门ID
     * */
    Object getTreeNodes(String roleid);
    /**
     * 保存一个部门数据
     * */
    Object save(Map<String, Object> httpRequest) throws Exception;
    /**
     * 删除一个部门数据
     * */
    void delete(Map<String, Object> httpRequest) throws Exception;

    /**
     * 部门树移动
     */
    void dropMove(Map<String, Object> httpRequest) throws Exception;

    HashMap<String,Object> getAllDepartmentLeader();

    HashMap<String,Object> getNameByDepartmentIds(Object departmentids);
}
