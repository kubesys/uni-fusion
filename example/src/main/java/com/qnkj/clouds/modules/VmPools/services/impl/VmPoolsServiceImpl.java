package com.qnkj.clouds.modules.VmPools.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.clouds.modules.VmPools.entitys.VmPools;
import com.qnkj.clouds.modules.VmPools.services.IVmPoolsService;
import com.qnkj.clouds.services.IKubeStackService;
import com.qnkj.common.entitys.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by Auto Generator
 * create date 2023-06-26
 * @author Auto Generator
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class VmPoolsServiceImpl implements IVmPoolsService {
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
    public void addPopupQueryFilter(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys, XN_Query query, String modulename, String record, String linkage) {
        log.info("addPopupQueryFilter : {} ",modulename);
        log.info("addPopupQueryFilter : {} ",record);
        log.info("addPopupQueryFilter : {} ",linkage);

        if (modulename.equals("VmImages")) {
            query.filter("my.content","=","vmdi");
        }else if (modulename.equals("VmDisks")) {
            query.filter("my.content","=","vmd");
        }
    }

    @Override
    public Boolean getEditViewIsReadOnly(Object entity) {
        VmPools vmPools = (VmPools)entity;
        if (Utils.isEmpty(vmPools.uid)) {
            return false;
        }
        return true;
    }



    @Override
    public void initModuleData() {
        try{
            List<Object> lists = XN_Query.create("Content").tag("vm_pools")
                    .filter("type","eic","vm_pools")
                    .filter("my.deleted","=","0")
                    .end(-1).execute();
            Map<String,VmPools> keyMaps = new HashMap<>();
            for(Object item: lists) {
                Content vm_pool = (Content) item;
                String name = vm_pool.my.get("name").toString();
                String zone = vm_pool.my.get("zone").toString();
                String md5 = MD5Util.get(name + zone);
                keyMaps.put(md5,new VmPools(vm_pool));
            }
            List<Map<String, Object>> vmPools = kubeStackService.getVmPools();
            List<String> fields = new ArrayList<>(Arrays.asList("zone","state","uid","eventid","node","createtime"));
            fields.addAll(Arrays.asList("vmpooltype","url","content"));
            for(Map<String, Object> vmPool: vmPools) {
                String md5 = MD5Util.get(vmPool.get("name").toString() + vmPool.get("zone").toString());
                if (!keyMaps.containsKey(md5)) {
                    Content newcontent = XN_Content.create("vm_pools","", ProfileUtils.getCurrentProfileId());
                    newcontent.add("deleted","0");
                    newcontent.add("name",vmPool.get("name").toString());
                    newcontent.add("describe","");
                    for(String field: fields) {
                        if (Utils.isNotEmpty(vmPool.get(field))) {
                            newcontent.add(field, vmPool.get(field).toString());
                        } else {
                            newcontent.add(field, "");
                        }
                    }
                    if (Utils.isNotEmpty(vmPool.get("autostart")) && "true".equals(vmPool.get("autostart"))) {
                        newcontent.add("autostart", "1");
                    } else {
                        newcontent.add("autostart", "0");
                    }
                    newcontent.add("status","Active");
                    newcontent.save("vm_pools");
                } else {
                    VmPools pool = keyMaps.get(md5);
                    if (Utils.isEmpty(pool.uid) || Utils.isEmpty(pool.state) || Utils.isEmpty(pool.createtime) ) {
                        Content obj = XN_Content.load(pool.id,"vm_pools");
                        for(String field: fields) {
                            if (Utils.isNotEmpty(vmPool.get(field))) {
                                obj.add(field, vmPool.get(field).toString());
                            } else {
                                obj.add(field, "");
                            }
                        }
                        obj.save("vm_pools");
                    }
                }
            }
        }catch (Exception ignored){ }
    }

    @Override
    public Object customEditViewEntity(String record, String fieldname, String fieldvalue) {
        try {
            if(fieldname.equals("node")) {
                List<Object> result = new ArrayList<>();
                String key = "";
                if (Utils.isNotEmpty(record)) {
                    VmPools obj = (VmPools)this.load(record);
                    key = obj.zone + ":" + obj.node;
                }
                Map<String,String> nodes = kubeStackService.getVmNodes();
                Integer sequence = 0;
                for(Map.Entry<String, String> entry: nodes.entrySet()) {
                    result.add(new PickListEntity()
                            .strval(entry.getKey())
                            .intval(sequence+1)
                            .sequence(sequence)
                            .label(entry.getValue()).toList());
                    sequence ++;
                }
                return new CustomEditEntity().uitype(19).three(result).one(key);
            }
        }
        catch(Exception e) {  }
        return null;
    }

    private final Pattern IS_ZONE_NODE = Pattern.compile("^(.*):(.*)$");
    @Override
    public void saveBefore(Content obj) throws Exception {
        if (obj.my.containsKey("node")) {
            String node = obj.my.get("node").toString();
            Matcher matcher = IS_ZONE_NODE.matcher(node);
            if(matcher.find()){
                String zone = matcher.group(1);
                String nodename = matcher.group(2);
                obj.my.put("zone",zone);
                obj.my.put("node",nodename);
            }
        }
    }

    @Override
    public void saveAfter(Content obj) throws Exception {
        try {
            createPool(obj);
        }catch (Exception ignored) {}
    }

    @Override
    public void startLinkVmPool(String record) throws Exception {
        try {
            Content obj = XN_Content.load(record,"vm_pools");
            createPool(obj);
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void deleteBefore(List<String> ids) throws Exception {
        List<Object> objs = XN_Content.loadMany(ids,"vm_pools");
        try {
            for (Object obj : objs) {
                VmPools vmPools = new VmPools(obj);
                if (Utils.isNotEmpty(vmPools.uid)) {
                    try {
                        Object pool = kubeStackService.getVmPoolByName(vmPools.zone,vmPools.name);
                        if (Utils.isNotEmpty(pool)) {
                            kubeStackService.deleteVmPools(vmPools.zone,vmPools.name, vmPools.vmpooltype, vmPools.eventid);
                        }
                    } catch (Exception e) {}
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("删除失败");
        }
    }

    private void createPool(Content obj) throws Exception {
        try {
            VmPools vmPools = new VmPools(obj);
            if (Utils.isEmpty(vmPools.uid)) {
                Map<String, Object> maps = new HashMap<String, Object>() {{
                    put("zone", vmPools.zone);
                    put("name", vmPools.name);
                    put("vmpooltype", vmPools.vmpooltype);
                    put("content", vmPools.content);
                    put("node", vmPools.node);
                    put("url", vmPools.url);
                    put("eventid", vmPools.eventid);
                    put("autostart", vmPools.autostart);
                }};
                if (kubeStackService.createVmPools(maps)) {
                    Map<String, Object> vmPool = kubeStackService.getVmPoolByName(vmPools.zone,vmPools.name);
                    List<String> fields = new ArrayList<>(Arrays.asList("state", "uid", "createtime"));
                    for (String field : fields) {
                        if (Utils.isNotEmpty(vmPool.get(field))) {
                            obj.add(field, vmPool.get(field).toString());
                        } else {
                            obj.add(field, "");
                        }
                    }
                    obj.save("vm_pools");
                } else {
                    throw new Exception("连接失败");
                }
            } else {
                throw new Exception("连接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
