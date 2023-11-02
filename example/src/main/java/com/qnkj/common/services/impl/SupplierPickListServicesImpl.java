package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.utils.Md5Util;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.configs.BaseSupplierPickListConfig;
import com.qnkj.common.entitys.PickListEntity;
import com.qnkj.common.entitys.SupplierPickList;
import com.qnkj.common.services.ISupplierPickListServices;
import com.qnkj.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

@Slf4j
@Service
public class SupplierPickListServicesImpl implements ISupplierPickListServices {
    private static Map<String,Map<String, SupplierPickList>> cachePickLists = new HashMap<>();

    private String getKey(String supplierid) {
        String application = BaseSaasConfig.getApplication();
        if(Utils.isEmpty(supplierid)){
            return application;
        } else {
            return application + "_" + supplierid;
        }
    }
    @Override
    public SupplierPickList get(String supplierid, String name) {
        String application = getKey(supplierid);
        if (!cachePickLists.containsKey(application)) {
            this.list(supplierid);
        }
        Map<String, SupplierPickList> pickLists = cachePickLists.get(application);
        if(!pickLists.containsKey(name)){
            this.list(supplierid);
        }
        if (pickLists.containsKey(name)) {
            return pickLists.get(name);
        }
        return null;
    }


    @Override
    public void clear() {
        cachePickLists.clear();
    }

    @Override
    public void clear(String supplierid) {
        cachePickLists.remove(getKey(supplierid));
    }

    @Override
    public List<SupplierPickList> list(String supplierid) {
        try {
            String application = getKey(supplierid);
            if (!cachePickLists.containsKey(application)) {
                Map<String, SupplierPickList> pickLists = new HashMap<>();
                List<Object> query = XN_Query.contentQuery().tag("supplier_picklists")
                        .filter("type", "eic", "supplier_picklists")
                        .filter("my.supplierid","=",supplierid)
                        .order("my.sequence", "A_N").notDelete()
                        .end(-1).execute();
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        String picklistname = ((Content) item).my.getOrDefault("picklistname","").toString();
                        String picklistlabel = ((Content) item).my.getOrDefault("picklistlabel","").toString();
                        String label = ((Content) item).my.getOrDefault("label","").toString();
                        String sequence = ((Content) item).my.getOrDefault("sequence","").toString();
                        String strval = ((Content) item).my.getOrDefault("strval","").toString();
                        String intval = ((Content) item).my.getOrDefault("intval","").toString();
                        String styclass = ((Content) item).my.getOrDefault("styclass","").toString();
                        Boolean builtin = "1".equals(((Content) item).my.getOrDefault("builtin", "0"));
                        if(intval.contains(".")){
                            intval = intval.substring(0,intval.indexOf("."));
                        }
                        try {
                            intval = Integer.valueOf(intval).toString();
                        }catch (Exception e) {
                            intval = "0";
                        }
                        SupplierPickList pickList;
                        if (pickLists.containsKey(picklistname)) {
                            pickList = pickLists.get(picklistname);
                        } else {
                            pickList = new SupplierPickList();
                            pickList.supplierid(supplierid);
                            pickList.picklistname(picklistname);
                            pickList.picklistlabel(picklistlabel);
                            pickList.builtin(builtin);
                        }
                        pickList.entitys.add(new PickListEntity().id(((Content) item).id)
                                .intval(Integer.valueOf(intval))
                                .label(label)
                                .strval(strval)
                                .styclass(styclass)
                                .sequence(Integer.parseInt(sequence, 10)));
                        pickLists.put(picklistname,pickList);
                    }
                }
                cachePickLists.put(application,pickLists);
            }
            if (cachePickLists.containsKey(application)) {
                Map<String, SupplierPickList> pickLists = cachePickLists.get(application);
                if (!pickLists.isEmpty()) {
                    return new ArrayList<>(pickLists.values());
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void update(SupplierPickList pickList) throws Exception {
        cachePickLists.clear();
        List<Object> query = XN_Query.contentQuery().tag("supplier_picklists")
                .filter("type", "eic", "supplier_picklists")
                .filter("my.supplierid", "=", pickList.supplierid)
                .filter("my.picklistname", "=", pickList.name)
                .order("my.sequence", "A_N").notDelete()
                .end(-1).execute();
        if (!query.isEmpty()) {
            if (!Utils.isEmpty(pickList.entitys)) {
                int index = 0;
                List<Object> update = new ArrayList<>();
                for (PickListEntity item : pickList.entitys) {
                    if (!Utils.isEmpty(item.id)) {
                        for (Object ditem : query) {
                            if (((Content) ditem).id.equals(item.id)) {
                                update.add(ditem);
                                ((Content) ditem).add("strval", item.strval)
                                        .add("builtin",pickList.builtin?"1":"0")
                                        .add("intval", item.intval)
                                        .add("label", item.label)
                                        .add("styclass",item.styclass)
                                        .add("sequence", index)
                                        .add("deleted",item.deleted)
                                        .save("supplier_picklists");
                            }
                        }
                    } else {
                        Content ple = XN_Content.create("supplier_picklists", "");
                        ple.add("picklistname", pickList.name)
                                .add("supplierid",pickList.supplierid)
                                .add("builtin",pickList.builtin?"1":"0")
                                .add("strval", item.strval)
                                .add("label", item.label)
                                .add("intval", item.intval)
                                .add("styclass",item.styclass)
                                .add("sequence", index)
                                .add("picklistlabel", pickList.label)
                                .save("supplier_picklists");
                    }
                    index++;
                }
                if(!update.isEmpty()){
                    query.removeAll(update);
                    if(!query.isEmpty()){
                        XN_Content.delete(query, "supplier_picklists");
                    }
                }
            } else {
                XN_Content.delete(query, "supplier_picklists");
            }
        } else {
            if (!Utils.isEmpty(pickList.entitys)) {
                int index = 0;
                for (PickListEntity item : pickList.entitys) {
                    Content ple = XN_Content.create("supplier_picklists", "");
                    ple.add("picklistname", pickList.name)
                            .add("supplierid",pickList.supplierid)
                            .add("builtin",pickList.builtin?"1":"0")
                            .add("strval", item.strval)
                            .add("label", item.label)
                            .add("intval", item.intval)
                            .add("styclass",item.styclass)
                            .add("sequence", index)
                            .add("deleted",item.deleted)
                            .add("picklistlabel", pickList.label)
                            .save("supplier_picklists");
                    index++;
                }
            }
        }
    }
    @Override
    public void init(String supplierid) {
        try {
            List<SupplierPickList> picklists = BaseSupplierPickListConfig.getPicklists();
            if (!Utils.isEmpty(picklists)) {
                List<String> haspicklists = new ArrayList<>();
                Map<String,Content> pickMaps = new HashMap<>();
                List<Object> query = XN_Query.contentQuery().tag("supplier_picklists")
                        .filter("type", "eic", "supplier_picklists")
                        .filter("my.supplierid", "=", supplierid).notDelete()
                        .end(-1).execute();
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        Content picklist = (Content) item;
                        if (picklist.my.containsKey("md5") && Utils.isNotEmpty(picklist.my.get("md5"))) {
                            String md5 = picklist.my.get("md5").toString();
                            haspicklists.add(md5);
                            pickMaps.put(md5,picklist);
                        }
                    }
                }
                List<String> existpicklists = new ArrayList<>();
                for (SupplierPickList item : picklists) {
                    if (!item.entitys.isEmpty()) {
                        int index = 0;
                        List<Object> saveds = new ArrayList<>();
                        for (PickListEntity picklist : item.entitys) {
                            String md5 = Md5Util.get(item.name+picklist.label+picklist.strval).toLowerCase();
                            existpicklists.add(md5);
                            if (!haspicklists.contains(md5)) {
                                Content supplierPicklist = XN_Content.create("supplier_picklists", "", "");
                                supplierPicklist.add("supplierid", supplierid)
                                        .add("picklistname", item.name)
                                        .add("md5", md5)
                                        .add("builtin", item.builtin ? "1" : "0")
                                        .add("strval", picklist.strval)
                                        .add("label", picklist.label)
                                        .add("intval", picklist.intval)
                                        .add("styclass", picklist.styclass)
                                        .add("sequence", index)
                                        .add("picklistlabel", item.label);
                                saveds.add(supplierPicklist);
                            }
                            index++;
                        }
                        if (!saveds.isEmpty()) {
                            try {
                                XN_Content.batchsave(saveds, "supplier_picklists");
                                saveds.clear();
                                this.clear();
                            } catch (Exception ignored) {}
                        }
                    }
                }
                for (Map.Entry<String, Content> entry : pickMaps.entrySet()) {
                    if (!existpicklists.contains(entry.getKey())) {
                         entry.getValue().delete("supplier_picklists");
                         this.clear();
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
