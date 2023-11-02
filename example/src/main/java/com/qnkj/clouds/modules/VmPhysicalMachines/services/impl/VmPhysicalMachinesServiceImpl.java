package com.qnkj.clouds.modules.VmPhysicalMachines.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.clouds.modules.VmPhysicalMachines.entitys.VmPhysicalMachines;
import com.qnkj.clouds.modules.VmPhysicalMachines.services.IVmPhysicalMachinesService;
import com.qnkj.clouds.services.IKubeStackService;
import com.qnkj.common.entitys.CustomDataSearch;
import com.qnkj.common.entitys.SelectOption;
import com.qnkj.common.utils.MD5Util;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.utils.ProfileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * create by Auto Generator
 * create date 2023-07-09
 * @author Auto Generator
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class VmPhysicalMachinesServiceImpl implements IVmPhysicalMachinesService {
    private final IKubeStackService kubeStackService;

    @Override
    public Object addDataSearch(HashMap<String, Object> Request) {
        List<SelectOption> options = new ArrayList<>();
        try{
            String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
            Object zoneid = RedisUtils.get("zone_" + currentSessionId);
            List<Object> lists = XN_Query.create("Content").tag("vm_zones")
                    .filter("type","eic","vm_zones")
                    .filter("my.deleted","=","0")
                    .filter("my.status ", "=", "Active")
                    .filter("my.state ", "=", "Ready")
                    .end(-1).execute();
            for(Object item: lists){
                Content zone = (Content)item;
                if (Utils.isNotEmpty(zoneid) && zoneid.toString().equals(zone.id)) {
                    options.add(new SelectOption(zone.id,zone.my.get("name").toString(),true));
                } else {
                    options.add(new SelectOption(zone.id,zone.my.get("name").toString(),false));
                }
            }
            CustomDataSearch zoneDataSearch = new CustomDataSearch().order(0).searchtype("select").colspan(1).fieldname("zone").fieldlabel("区域").options(options);
            return Arrays.asList(zoneDataSearch);
        }catch (Exception ignored){ }
        return new CustomDataSearch();
    }

    @Override
    public void addQueryFilter(Map<String, Object> Request, BaseEntityUtils viewEntitys, XN_Query query) {
        log.info("addQueryFilter Request : {}",Request);
        String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
        Object zoneid = "";
        if (Request.containsKey("zone")) {
            zoneid = Request.get("zone").toString().trim();
            if (Utils.isEmpty(zoneid)) {
                RedisUtils.del("zone_" + currentSessionId);
            }
        }
        else {
            zoneid = RedisUtils.get("zone_" + currentSessionId);
        }
        if (Utils.isNotEmpty(zoneid)) {
            query.filter("my.zone","=",zoneid);
            RedisUtils.set("zone_"  + currentSessionId, zoneid);
        }
    }

    @Override
    public void initModuleData() {
        try{
            List<Object> lists = XN_Query.create("Content").tag("vm_physicalmachines")
                    .filter("type","eic","vm_physicalmachines")
                    .filter("my.deleted","=","0")
                    .end(-1).execute();
            Map<String, VmPhysicalMachines> keyMaps = new HashMap<>();
            for(Object item: lists) {
                Content vmPhysicalMachine = (Content) item;
                String name = vmPhysicalMachine.my.get("name").toString();
                String zone = vmPhysicalMachine.my.get("zone").toString();
                String md5 = MD5Util.get(name + zone);
                keyMaps.put(md5,new VmPhysicalMachines(vmPhysicalMachine));
            }
            List<Map<String, Object>> nodes = kubeStackService.getPhysicalMachiness();
            List<String> nodeNames = nodes.stream().map(v -> v.get("name").toString()).collect(Collectors.toList());
            List<String> fields = new ArrayList<>(Arrays.asList("zone","name","state","createtime","uid","internalip","hostname","cpu","ephemeral_storage","memory","state","architecture","kernelVersion","kubeletVersion","operatingSystem","osImage"));
            for(Map<String, Object> node: nodes) {
                String md5 = MD5Util.get(node.get("name").toString() + node.get("zone").toString());
                if (!keyMaps.containsKey(md5)) {
                    Content newcontent = XN_Content.create("vm_physicalmachines","", ProfileUtils.getCurrentProfileId());
                    newcontent.add("deleted","0");
                    for(String field: fields) {
                        if (Utils.isNotEmpty(node.get(field))) {
                            newcontent.add(field, node.get(field).toString());
                        } else {
                            newcontent.add(field, "");
                        }
                    }
                    newcontent.save("vm_physicalmachines");
                } else {
                        VmPhysicalMachines pool = keyMaps.get(md5);
                        Content obj = XN_Content.load(pool.id,"vm_physicalmachines");
                        for(String field: fields) {
                            if (Utils.isNotEmpty(node.get(field))) {
                                obj.add(field, node.get(field).toString());
                            } else {
                                obj.add(field, "");
                            }
                        }
                        obj.save("vm_physicalmachines");
                }
            }
            if (nodes.size() > 0) {
                for (Object item : lists) {
                    Content vmPhysicalMachine = (Content) item;
                    if (!nodeNames.contains(vmPhysicalMachine.my.get("name").toString())) {
                        vmPhysicalMachine.delete("vm_physicalmachines");
                    }
                }
            }
        }catch (Exception ignored){ }
    }

    @Override
    public List<String> getPopupEditViewFields() {
        return Arrays.asList("cluster");
    }
}
