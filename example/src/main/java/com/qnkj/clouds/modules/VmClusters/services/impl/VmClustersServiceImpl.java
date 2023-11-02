package com.qnkj.clouds.modules.VmClusters.services.impl;

import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.clouds.modules.VmClusters.services.IVmClustersService;
import com.qnkj.common.entitys.CustomDataSearch;
import com.qnkj.common.entitys.SelectOption;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
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
public class VmClustersServiceImpl implements IVmClustersService {


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
            List<Content> lists = XN_Query.create("Content").tag("vm_clusters")
                    .filter("type","eic","vm_clusters")
                    .filter("my.deleted","=","0")
                    .end(-1).execute();
            List<String> vm_clusterids = lists.stream().map( v -> v.id).collect(Collectors.toList());

            List<Content> physicalmachines = XN_Query.create ( "Content_Count" ).tag("vm_physicalmachines")
                    .filter( "type", "eic", "vm_physicalmachines" )
                    .filter("my.deleted", "=", 0)
                    .filter("my.cluster", "in", vm_clusterids)
                    .end(-1)
                    .rollup()
                    .group("my.cluster")
                    .execute();

            if (physicalmachines.size() > 0 ) {
                Map<String, Integer> maps = physicalmachines.stream().collect(Collectors.toMap(v1 -> v1.get("cluster").toString(), v1 -> Integer.parseInt(v1.get("count").toString()), (k1, k2) -> k1));

                for(Content item : lists) {
                    if (maps.containsKey(item.id)) {
                        if (Integer.parseInt(item.get("physicalmachine").toString()) != maps.get(item.id)) {
                            item.my.put("physicalmachine",maps.get(item.id));
                            item.save("vm_clusters");
                        }
                    }
                }
            }

        }catch (Exception ignored){ }
    }
}
