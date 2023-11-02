package com.qnkj.clouds.modules.VmImageServers.services;

import com.qnkj.core.base.services.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */
public interface IVmImageServersService extends IBaseService {

    List<Map<String, Object>> getIsoFileLists(String record);
}
