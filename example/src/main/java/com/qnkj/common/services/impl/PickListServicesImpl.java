package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.PickList;
import com.qnkj.common.entitys.PickListEntity;
import com.qnkj.common.services.IPickListServices;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

@Service
public class PickListServicesImpl implements IPickListServices {
    private static Map<String,Map<String,PickList>> cachePickLists = new HashMap<>();

    @Override
    public Boolean existPickList(String name) {
        String application = BaseSaasConfig.getApplication();
        if (!cachePickLists.containsKey(application)) {
            this.list();
        }
        Map<String, PickList> pickLists = cachePickLists.get(application);
        if(!pickLists.containsKey(name)){
            this.list();
        }
        return pickLists.containsKey(name);
    }

    @Override
    public PickList get(String name) {
        String application = BaseSaasConfig.getApplication();
        if (!cachePickLists.containsKey(application)) {
            this.list();
        }
        Map<String, PickList> pickLists = cachePickLists.get(application);
        if(Utils.isNotEmpty(pickLists) && !pickLists.containsKey(name)){
            this.list();
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
    public List<PickList> list() {
        try {
            String application = BaseSaasConfig.getApplication();
            if (!cachePickLists.containsKey(application)) {
                Map<String, PickList> pickLists = new HashMap<>();
                int page = 0;
                List<Object> query;
                do {
                    query = XN_Query.contentQuery().tag("picklists")
                        .filter("type", "eic", "picklists")
                        .order("my.sequence", "A_N").notDelete()
                        .begin(page * 100).end((page + 1) * 100)
                        .execute();
                    if (!query.isEmpty()) {
                        for (Object item : query) {
                            String picklistname = ((Content) item).get("picklistname").toString();
                            String picklistlabel = ((Content) item).get("picklistlabel").toString();
                            String label = ((Content) item).get("label").toString();
                            String sequence = ((Content) item).get("sequence").toString();
                            String strval = ((Content) item).get("strval").toString();
                            String intval = ((Content) item).get("intval").toString();
                            String styclass = ((Content) item).get("styclass").toString();
                            Boolean builtin = "1".equals(((Content) item).get("builtin", "0"));
                            if(intval.contains(".")){
                                intval = intval.substring(0,intval.indexOf("."));
                            }
                            try {
                                intval = Integer.valueOf(intval).toString();
                            }catch (Exception e) {
                                intval = "0";
                            }
                            PickList pickList;
                            if (pickLists.containsKey(picklistname)) {
                                pickList = pickLists.get(picklistname);
                            } else {
                                pickList = new PickList();
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
                    page++;
                } while (query.size() == 100);
                cachePickLists.put(application,pickLists);
            }
            if (cachePickLists.containsKey(application)) {
                Map<String, PickList> pickLists = cachePickLists.get(application);
                if (!pickLists.isEmpty()) {
                    return new ArrayList<>(pickLists.values());
                }
            }
        } catch (Exception ignored) {
        }
        return new ArrayList<>(1);
    }

    @Override
    public void update(PickList pickList) throws Exception {
        this.clear();
        List<Object> query = XN_Query.contentQuery().tag("picklists")
                .filter("type", "eic", "picklists")
                .filter("my.picklistname", "=", pickList.name)
                .order("my.sequence", "A_N").notDelete()
                .end(-1).execute();
        if (!query.isEmpty()) {
            if (!Utils.isEmpty(pickList.entitys)) {
                int index = 0;
                List<Object> update = new ArrayList<>();
                List<Object> create = new ArrayList<>();
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
                                        .add("deleted",item.deleted);
                            }
                        }
                    } else {
                        Content ple = XN_Content.create("picklists", "","system");
                        create.add(ple);
                        ple.add("picklistname", pickList.name)
                                .add("builtin",pickList.builtin?"1":"0")
                                .add("strval", item.strval)
                                .add("label", item.label)
                                .add("intval", item.intval)
                                .add("styclass",item.styclass)
                                .add("sequence", index)
                                .add("picklistlabel", pickList.label);
                    }
                    index++;
                }
                if(!create.isEmpty()){
                    XN_Content.batchsave(create,"picklists");
                    create.clear();
                }
                if(!update.isEmpty()){
                    XN_Content.batchsave(update,"picklists");
                    query.removeAll(update);
                }
                if(!query.isEmpty()){
                    XN_Content.delete(query, "picklists");
                }
            } else {
                XN_Content.delete(query, "picklists");
            }
        } else {
            if (!Utils.isEmpty(pickList.entitys)) {
                int index = 0;
                List<Object> create = new ArrayList<>();
                for (PickListEntity item : pickList.entitys) {
                    Content ple = XN_Content.create("picklists", "","system");
                    create.add(ple);
                    ple.add("picklistname", pickList.name)
                            .add("builtin",pickList.builtin?"1":"0")
                            .add("strval", item.strval)
                            .add("label", item.label)
                            .add("intval", item.intval)
                            .add("styclass",item.styclass)
                            .add("sequence", index)
                            .add("deleted",item.deleted)
                            .add("picklistlabel", pickList.label);
                    index++;
                }
                if(!create.isEmpty()){
                    XN_Content.batchsave(create,"picklists");
                    create.clear();
                }
            }
        }
    }
}
