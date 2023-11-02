package com.qnkj.core.base.modules.supplier.Supplierpicklists.service;

import java.util.HashMap;
import java.util.Map;

public interface ISupplierPickListsService {
    String getPickListLabel(String picklistname, Object value);

    Object getPickList(String picklistname);

    int getMaxIntValue(String picklistname);

    HashMap<String, Object> getAllPickLists();

    void save(Map<String, Object> httpRequest) throws Exception;

    void saveNewPicklist(Map<String, Object> httpRequest) throws Exception;

    void delete(Map<String, Object> httpRequest) throws Exception;

    void moveUp(Map<String, Object> httpRequest) throws Exception;

    void moveDown(Map<String, Object> httpRequest) throws Exception;
}
