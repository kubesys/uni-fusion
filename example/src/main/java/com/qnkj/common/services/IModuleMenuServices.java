package com.qnkj.common.services;

import com.qnkj.common.entitys.ModuleMenu;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/6
 */

public interface IModuleMenuServices {

    void clear();
    void clear(String modulename);
    void clear(String program, String parent, String modulename);

    ModuleMenu get(String modulename);

    ModuleMenu get(String program, String parent, String label);

    ModuleMenu load(String menuid);

    List<ModuleMenu> list(String program, String parent);

    Boolean isModulenameExist(String modulename);

    void update(ModuleMenu moduleMenu) throws Exception;
}
