package com.qnkj.clouds.modules.VmDisks.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.clouds.modules.VmDiskOfferings.entitys.VmDiskOfferings;
import com.qnkj.clouds.modules.VmDiskOfferings.services.IVmDiskOfferingsService;
import com.qnkj.clouds.modules.VmDisks.entitys.VmDisks;
import com.qnkj.clouds.modules.VmDisks.services.IVmDisksService;
import com.qnkj.clouds.modules.VmPools.entitys.VmPools;
import com.qnkj.clouds.modules.VmPools.services.IVmPoolsService;
import com.qnkj.clouds.services.IKubeStackService;
import com.qnkj.common.entitys.CustomDataSearch;
import com.qnkj.common.entitys.CustomEditEntity;
import com.qnkj.common.entitys.PickListEntity;
import com.qnkj.common.entitys.SelectOption;
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
public class VmDisksServiceImpl implements IVmDisksService {
    private final IKubeStackService kubeStackService;
    private final IVmPoolsService vmpoolsService;
    private final IVmDiskOfferingsService vmdiskofferingsService;

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
            List<Object> lists = XN_Query.create("Content").tag("vm_disks")
                    .filter("type","eic","vm_disks")
                    .filter("my.deleted","=","0")
                    .end(-1).execute();
            Map<String, VmDisks> keyMaps = new HashMap<>();
            for(Object item: lists) {
                Content vm_disk = (Content) item;
                keyMaps.put(vm_disk.my.get("name").toString(),new VmDisks(vm_disk));
            }
            List<Map<String, Object>> vmDisks = kubeStackService.getVmDisks();
            log.info("vmDisks : {}",vmDisks);

            List<String> fields = new ArrayList<>(Arrays.asList("zone","state","uid","eventid","node","createtime"));
            fields.addAll(Arrays.asList("pool","current","actual_size","cluster_size","dirty_flag","disktype","format","uni","virtual_size"));
            for(Map<String, Object> vmPool: vmDisks) {
                if (!keyMaps.containsKey(vmPool.get("name").toString())) {
                    Content newcontent = XN_Content.create("vm_disks", "", ProfileUtils.getCurrentProfileId());
                    newcontent.add("deleted", "0");
                    newcontent.add("name", vmPool.get("name").toString());
                    newcontent.add("describe", "");
                    for (String field : fields) {
                        if (Utils.isNotEmpty(vmPool.get(field))) {
                            newcontent.add(field, vmPool.get(field).toString());
                        } else {
                            newcontent.add(field, "");
                        }
                    }
                    newcontent.add("status", "Active");
                    if (Utils.isNotEmpty(vmPool.get("state"))) {
                        newcontent.add("state", vmPool.get("state").toString());
                    } else {
                        newcontent.add("state", "NotReady");
                    }
                    newcontent.save("vm_disks");
                } else {
                    VmDisks vmDisk = keyMaps.get(vmPool.get("name").toString());
                    if (Utils.isNotEmpty(vmDisk.uid)) {
                        Content obj = XN_Content.load(vmDisk.id,"vm_disks");
                        for(String field: fields) {
                            if (Utils.isNotEmpty(vmPool.get(field))) {
                                obj.add(field, vmPool.get(field).toString());
                            } else {
                                obj.add(field, "");
                            }
                        }
                        if (Utils.isNotEmpty(vmPool.get("state"))) {
                            obj.add("state", vmPool.get("state").toString());
                        } else {
                            obj.add("state", "NotReady");
                        }
                        obj.save("vm_disks");
                    }
                }
            }

        }catch (Exception ignored){ }
    }

    @Override
    public Boolean getEditViewIsReadOnly(Object entity) {
        VmDisks vmDisks = (VmDisks)entity;
        if (Utils.isEmpty(vmDisks.uid)) {
            return false;
        }
        return true;
    }
    @Override
    public Object customEditViewEntity(String record, String fieldname, String fieldvalue) {
        try {
            if(fieldname.equals("node")) {
                List<Object> result = new ArrayList<>();
                String key = "";
                if (Utils.isNotEmpty(record)) {
                    VmDisks obj = (VmDisks)this.load(record);
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
            createDisk(obj);
        }catch (Exception ignored) {}
    }

    private void createDisk(Content obj) throws Exception {
        try {
            VmDisks vmdisks = new VmDisks(obj);
            if (Utils.isEmpty(vmdisks.uid) && Utils.isNotEmpty(vmdisks.poolname) && Utils.isNotEmpty(vmdisks.diskoffering)) {
                VmPools vmPools = (VmPools)vmpoolsService.load(vmdisks.poolname);
                VmDiskOfferings vmDiskOfferings = (VmDiskOfferings)vmdiskofferingsService.load(vmdisks.diskoffering);
                Map<String, Object> maps = new HashMap<String, Object>() {{
                    put("zone", vmdisks.zone);
                    put("name", vmdisks.name);
                    put("node", vmdisks.node);
                    put("eventid", vmdisks.eventid);
                    put("vmdisktype", vmdisks.vmdisktype);
                    put("pool", vmPools.name);
                    put("format", vmdisks.format);
                    put("capacity", vmDiskOfferings.capacity+"G");
                }};
                if (kubeStackService.createVmDisks(maps)) {
                    Map<String, Object> vmPool = kubeStackService.getVmDiskByName(vmdisks.zone,vmdisks.name);
                    List<String> fields = new ArrayList<>(Arrays.asList("uid", "createtime"));
                    fields.addAll(Arrays.asList("pool","current"));
                    for (String field : fields) {
                        if (Utils.isNotEmpty(vmPool.get(field))) {
                            obj.my.put(field, vmPool.get(field).toString());
                        } else {
                            obj.my.put(field, "");
                        }
                    }
                    obj.my.put("capacity",vmDiskOfferings.capacity);
                    obj.my.put("pool",vmPools.name);
                    obj.save("vm_disks");
                } else {
                    throw new Exception("创建失败");
                }
            } else {
                throw new Exception("创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public void deleteBefore(List<String> ids) throws Exception {
        List<Object> objs = XN_Content.loadMany(ids,"vm_disks");
        try {
            for (Object obj : objs) {
                VmDisks vmDisks = new VmDisks(obj);
                if (Utils.isNotEmpty(vmDisks.uid)) {
                    Object disk = kubeStackService.getVmDiskByName(vmDisks.zone,vmDisks.name);
                    if (Utils.isNotEmpty(disk)) {
                        kubeStackService.deleteVmDisks(vmDisks.zone,vmDisks.name, vmDisks.vmdisktype, vmDisks.pool);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("删除失败");
        }
    }
}
