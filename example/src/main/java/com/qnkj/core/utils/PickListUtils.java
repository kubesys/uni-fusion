package com.qnkj.core.utils;

import com.qnkj.core.base.modules.settings.picklistsmanage.service.IMyPickListsService;
import com.qnkj.core.base.modules.supplier.Supplierpicklists.service.ISupplierPickListsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class PickListUtils {

    @Autowired
    public IMyPickListsService mypickListsService;
    @Autowired
    public ISupplierPickListsService supplierPickListsService;

    private static PickListUtils pickListUtils;

    @PostConstruct
    public void init() {
        pickListUtils = this;
        pickListUtils.mypickListsService = this.mypickListsService;
        pickListUtils.supplierPickListsService = this.supplierPickListsService;
    }

    public static Object getPickList(String fieldname) {
        if(fieldname.startsWith("Supplie::")){
            String picklist = fieldname.split("::")[1];
            return pickListUtils.supplierPickListsService.getPickList(picklist);
        }else {
            if (pickListUtils.mypickListsService.existPickList(fieldname)) {
                return pickListUtils.mypickListsService.getPickList(fieldname);
            } else {
                return pickListUtils.supplierPickListsService.getPickList(fieldname);
            }
        }
    }

    public static String getPickListLabel(String fieldname, Object fieldvalue) {
        if(fieldname.startsWith("Supplie::")){
            String picklist = fieldname.split("::")[1];
            return pickListUtils.supplierPickListsService.getPickListLabel(picklist,fieldvalue);
        }else {
            return pickListUtils.mypickListsService.getPickListLabel(fieldname,fieldvalue);
        }
    }

    public static List<String> getPickList() {
        Set<String> sets = new HashSet<>();
        sets.addAll(pickListUtils.mypickListsService.getAllPickLists().keySet());
        sets.addAll(pickListUtils.supplierPickListsService.getAllPickLists().keySet());
       return new ArrayList<>(sets);
    }

    public static Map<String,Object> getAllPicklists() {
        Map<String,Object> result = new HashMap<>();
        HashMap<String, Object> picklist = pickListUtils.mypickListsService.getAllPickLists();
        for(String key : picklist.keySet()){
            result.put(key,picklist.get(key));
        }
        picklist = pickListUtils.supplierPickListsService.getAllPickLists();
        for(String key : picklist.keySet()){
            result.put(key,picklist.get(key));
        }
        return result;
    }
}
