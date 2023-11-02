package com.qnkj.core.base.modules.supplier.Supplierpicklists.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.entitys.PickListEntity;
import com.qnkj.common.entitys.SupplierPickList;
import com.qnkj.common.services.ISupplierPickListServices;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.base.modules.supplier.Supplierpicklists.service.ISupplierPickListsService;
import com.qnkj.core.utils.ProfileUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierPickListsServiceImpl implements ISupplierPickListsService {

    private final ISupplierPickListServices supplierpickListServices;

    public SupplierPickListsServiceImpl(ISupplierPickListServices supplierpickListServices) {
        this.supplierpickListServices = supplierpickListServices;
    }

    @Override
    public String getPickListLabel(String picklistname, Object value) {
        SupplierPickList picklist = supplierpickListServices.get(SupplierUtils.getSupplierid(),picklistname);
        if(Utils.isEmpty(picklist)) {
            return null;
        }
        return picklist.label;
    }

    @Override
    public Object getPickList(String picklistname) {
        SupplierPickList picklist = supplierpickListServices.get(SupplierUtils.getSupplierid(),picklistname);
        if(Utils.isEmpty(picklist)) {
            return null;
        }
        List<Object> result = new ArrayList<>();
        for (PickListEntity item : picklist.entitys) {
            if (!Utils.isEmpty(item.strval)) {
                result.add(item.toList());
            }
        }
        return result;
    }

    @Override
    public int getMaxIntValue(String picklistname) {
        try{
            List<Object> query = XN_Query.contentQuery().tag("supplier_picklists")
                    .filter("type", "eic", "supplier_picklists")
                    .filter("my.picklistname", "=", picklistname)
                    .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                    .order("my.intval", "D_N")
                    .notDelete().end(1).execute();
            if(!query.isEmpty()){
                return Integer.parseInt(((Content)query.get(0)).my.get("intval").toString(),10);
            }
        }catch (Exception ignored){}
        return 0;
    }

    @Override
    public HashMap<String, Object> getAllPickLists() {
        List<SupplierPickList> pickLists = supplierpickListServices.list(SupplierUtils.getSupplierid());
        HashMap<String, Object> result = new HashMap<>();
        if(!Utils.isEmpty(pickLists)) {
            for (SupplierPickList item : pickLists) {
                Map<String,Object> infoMap = new HashMap<>(1);
                List<Object> infoList = new ArrayList<>(1);
                for (PickListEntity entity : item.entitys) {
                    infoList.add(entity.toList());
                }
                infoMap.put("label", item.label);
                infoMap.put("picklist", infoList);
                result.put(item.name, infoMap);
            }
        }
        return result;

    }

    @Override
    public void save(Map<String, Object> httpRequest) throws Exception {
        try{
            supplierpickListServices.clear(SupplierUtils.getSupplierid());
            String label = httpRequest.getOrDefault("label", "").toString().trim();
            String strval = httpRequest.getOrDefault("strval", "").toString().trim();
            String intval = httpRequest.getOrDefault("intval", "0").toString().trim();
            String styclass = httpRequest.getOrDefault("styclass", "").toString().trim();
            if(!Utils.isEmpty(httpRequest.get("submittype")) && "edit".equals(httpRequest.get("submittype"))) {
                if (!Utils.isEmpty(httpRequest.get("record"))) {
                    String record = httpRequest.get("record").toString();
                    Content picklist = XN_Content.load(record, "supplier_picklists");
                    picklist.my.put("label", label);
                    picklist.my.put("strval", strval);
                    picklist.my.put("intval", intval);
                    picklist.my.put("styclass", styclass);
                    picklist.save("supplier_picklists");
                } else {
                    throw new Exception("保存参数错误");
                }
            }else if(!Utils.isEmpty(httpRequest.get("submittype")) && "add".equals(httpRequest.get("submittype"))){
                if(!Utils.isEmpty(httpRequest.get("picklist"))){
                    int sequence = 0;
                    List<Object> query = XN_Query.contentQuery().tag("supplier_picklists")
                            .filter("type", "eic", "supplier_picklists")
                            .filter("my.supplierid", "=", SupplierUtils.getSupplierid())
                            .filter("my.picklistname", "=", httpRequest.get("picklist"))
                            .order("my.sequence", "D_N")
                            .notDelete().end(1).execute();
                    if(!query.isEmpty()){
                        sequence = Integer.parseInt(((Content)query.get(0)).my.get("sequence").toString(),10) + 1;
                    }
                    String picklistname = httpRequest.getOrDefault("picklist", "").toString().trim();
                    String picklistlabel = httpRequest.getOrDefault("picklistlabel", "").toString().trim();
                    Content picklist = XN_Content.create("supplier_picklists","", ProfileUtils.getCurrentProfileId());
                    picklist.add("picklistname",picklistname)
                            .add("picklistlabel",picklistlabel)
                            .add("supplierid",SupplierUtils.getSupplierid())
                            .add("strval",strval)
                            .add("label",label)
                            .add("intval",intval)
                            .add("styclass",styclass)
                            .add("sequence",sequence);
                    picklist.save("supplier_picklists");
                } else {
                    throw new Exception("保存参数错误");
                }
            } else {
                throw new Exception("保存参数错误");
            }
        }catch (Exception e){
            throw new Exception("保存失败");
        }
    }


    @Override
    public void saveNewPicklist(Map<String, Object> httpRequest) throws Exception {
        supplierpickListServices.clear();
        String picklistname = httpRequest.getOrDefault("picklistname", "").toString().trim();
        String picklistlabel = httpRequest.getOrDefault("picklistlabel", "").toString().trim();
        String label = httpRequest.getOrDefault("label", "").toString().trim();
        String strval = httpRequest.getOrDefault("strval", "").toString().trim();
        String intval = httpRequest.getOrDefault("intval", "").toString().trim();
        String styclass = httpRequest.getOrDefault("styclass", "").toString().trim();

        List<Object> supplierPicklists = XN_Query.contentQuery().tag("supplier_picklists")
                .filter("type", "eic", "supplier_picklists")
                .filter("my.supplierid", "=", SupplierUtils.getSupplierid())
                .filter("my.picklistname", "=", picklistname)
                .notDelete().end(1).execute();
        if (!supplierPicklists.isEmpty()) {
            throw new Exception("字典已经存在");
        }
        Content picklist = XN_Content.create("supplier_picklists","", ProfileUtils.getCurrentProfileId());
        picklist.add("picklistname",picklistname)
                .add("picklistlabel",picklistlabel)
                .add("supplierid",SupplierUtils.getSupplierid())
                .add("strval",strval)
                .add("builtin", "0")
                .add("label",label)
                .add("intval",intval)
                .add("styclass",styclass)
                .add("sequence",0);
        picklist.save("supplier_picklists");

    }

    @Override
    public void delete(Map<String, Object> httpRequest) throws Exception {
        try{
            supplierpickListServices.clear(SupplierUtils.getSupplierid());
            if(!Utils.isEmpty(httpRequest.get("picklist")) && !Utils.isEmpty(httpRequest.get("options"))){
                if(!Utils.isEmpty(((List<?>)httpRequest.get("options")).get(5))){
                    String record = ((List<?>)httpRequest.get("options")).get(5).toString();
                    XN_Content.delete(new ArrayList<>(ImmutableSet.of(record)),"supplier_picklists");
                } else {
                    throw new Exception("删除失败，参数错误");
                }
            } else {
                throw new Exception("删除失败，参数错误");
            }
        }catch (Exception e){
            throw new Exception("删除失败");
        }
    }

    @Override
    public void moveDown(Map<String, Object> httpRequest) throws Exception {
        try{
            supplierpickListServices.clear(SupplierUtils.getSupplierid());
            if(!Utils.isEmpty(httpRequest.get("picklist")) && !Utils.isEmpty(httpRequest.get("options"))){
                if(!Utils.isEmpty(((List<?>)httpRequest.get("options")).get(5))){
                    String record = ((List<?>)httpRequest.get("options")).get(5).toString();
                    String sequence = ((List<?>)httpRequest.get("options")).get(4).toString();
                    List<Object> query = XN_Query.contentQuery().tag("supplier_picklists")
                            .filter("type","eic","supplier_picklists")
                            .notDelete()
                            .filter("my.supplierid", "=", SupplierUtils.getSupplierid())
                            .filter("my.picklistname","=",httpRequest.get("picklist"))
                            .filter("my.sequence",">",Integer.parseInt(sequence))
                            .order("my.sequence","A_N")
                            .end(1).execute();
                    if(!query.isEmpty()) {
                        Content next = (Content) query.get(0);
                        String nextsequence = next.my.get("sequence").toString();
                        next.my.put("sequence",sequence);
                        Content curr = XN_Content.load(record,"supplier_picklists");
                        curr.my.put("sequence",nextsequence);
                        XN_Content.batchsave(new ArrayList<Object>(ImmutableSet.of(next,curr)),"supplier_picklists");
                    }
                } else {
                    throw new Exception("移动失败，参数错误");
                }
            } else {
                throw new Exception("移动失败，参数错误");
            }
        }catch (Exception e){
            throw new Exception("移动失败");
        }
    }

    @Override
    public void moveUp(Map<String, Object> httpRequest) throws Exception {
        try{
            supplierpickListServices.clear(SupplierUtils.getSupplierid());
            if(!Utils.isEmpty(httpRequest.get("picklist")) && !Utils.isEmpty(httpRequest.get("options"))){
                if(!Utils.isEmpty(((List<?>)httpRequest.get("options")).get(5))){
                    String record = ((List<?>)httpRequest.get("options")).get(5).toString();
                    String sequence = ((List<?>)httpRequest.get("options")).get(4).toString();
                    List<Object> query = XN_Query.contentQuery().tag("supplier_picklists")
                            .filter("type","eic","supplier_picklists")
                            .notDelete()
                            .filter("my.supplierid", "=", SupplierUtils.getSupplierid())
                            .filter("my.picklistname","=",httpRequest.get("picklist"))
                            .filter("my.sequence","<",Integer.parseInt(sequence))
                            .order("my.sequence","D_N")
                            .end(1).execute();
                    if(!query.isEmpty()) {
                        Content next = (Content) query.get(0);
                        String nextsequence = next.my.get("sequence").toString();
                        next.my.put("sequence",sequence);
                        Content curr = XN_Content.load(record,"supplier_picklists");
                        curr.my.put("sequence",nextsequence);
                        XN_Content.batchsave(new ArrayList<>(ImmutableSet.of(next,curr)),"supplier_picklists");
                    }
                } else {
                    throw new Exception("移动失败，参数错误");
                }
            } else {
                throw new Exception("移动失败，参数错误");
            }
        }catch (Exception e){
            throw new Exception("移动失败");
        }
    }
}
