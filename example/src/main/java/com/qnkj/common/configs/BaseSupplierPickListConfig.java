package com.qnkj.common.configs;

import com.qnkj.common.entitys.SupplierPickList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * create by 徐雁
 * create date 2020/12/23
 */

public class BaseSupplierPickListConfig {
    private static Map<String, SupplierPickList> pickListsetting = new HashMap<>();

    private BaseSupplierPickListConfig() {}

    public static void addPicklists(List<SupplierPickList> pickLists) {
        for(SupplierPickList item: pickLists){
            if (!pickListsetting.containsKey(item.name)) {
                pickListsetting.put(item.name,item);
            }
        }
    }

    public static List<SupplierPickList> getPicklists() {
        return pickListsetting.values().stream().collect(Collectors.toList());
    }


}
