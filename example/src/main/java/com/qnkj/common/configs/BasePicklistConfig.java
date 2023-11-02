package com.qnkj.common.configs;

import com.qnkj.common.entitys.PickList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * create by 徐雁
 * create date 2020/12/23
 */
public class BasePicklistConfig {
    private static Map<String,PickList> pickListsetting = new HashMap<>();

    private BasePicklistConfig() {}

    public static void addBuiltinPicklists(List<PickList> pickLists) {
        for(PickList item: pickLists){
            if (!pickListsetting.containsKey(item.name)) {
                item.builtin = true;
                pickListsetting.put(item.name,item);
            }
        }
    }

    public static void addPicklists(List<PickList> pickLists) {
        for(PickList item: pickLists){
            if (!pickListsetting.containsKey(item.name)) {
                item.builtin = false;
                pickListsetting.put(item.name,item);
            }
        }
    }

    public static List<PickList> getPicklists() {
        return pickListsetting.values().stream().collect(Collectors.toList());
    }

}
