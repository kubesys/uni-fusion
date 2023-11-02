package com.qnkj.clouds.modules.VmInstances.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.utils.Md5Util;
import com.qnkj.clouds.modules.NoVNC.controller.WebsockifyServer;
import com.qnkj.clouds.modules.VmDisks.entitys.VmDisks;
import com.qnkj.clouds.modules.VmDisks.services.IVmDisksService;
import com.qnkj.clouds.modules.VmImages.entitys.VmImages;
import com.qnkj.clouds.modules.VmImages.services.IVmImagesService;
import com.qnkj.clouds.modules.VmInstanceOfferings.entitys.VmInstanceOfferings;
import com.qnkj.clouds.modules.VmInstanceOfferings.services.IVmInstanceOfferingsService;
import com.qnkj.clouds.modules.VmInstances.entitys.VmInstances;
import com.qnkj.clouds.modules.VmInstances.services.IVmInstancesService;
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
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class VmInstancesServiceImpl implements IVmInstancesService {
    private final IKubeStackService kubeStackService;
    private final IVmPoolsService vmpoolsService;
    private final IVmImagesService vmimagesService;
    private final IVmDisksService vmdisksService;
    private final IVmInstanceOfferingsService vminstanceofferingsService;

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
            List<Object> lists = XN_Query.create("Content").tag("vm_instances")
                    .filter("type","eic","vm_instances")
                    .filter("my.deleted","=","0")
                    .end(-1).execute();
            Map<String, VmInstances> keyMaps = new HashMap<>();
            for(Object item: lists) {
                Content vm_instance = (Content) item;
                keyMaps.put(vm_instance.my.get("name").toString(),new VmInstances(vm_instance));
            }
            List<Map<String, Object>> vms = kubeStackService.getVms();
            log.info("vms : {}",vms);
            List<String> fields = new ArrayList<>(Arrays.asList("zone","state","uid","eventid","node","createtime"));
            fields.addAll(Arrays.asList("port","cpu_vendor","vcpu","os_arch","os_machine","current_memory","memory"));

            for(Map<String, Object> item: vms) {
                if (!keyMaps.containsKey(item.get("name").toString())) {
                    Content newcontent = XN_Content.create("vm_instances","", ProfileUtils.getCurrentProfileId());
                    newcontent.add("deleted","0");
                    newcontent.add("name",item.get("name").toString());
                    newcontent.add("describe","");
                    for(String field: fields) {
                        if (Utils.isNotEmpty(item.get(field))) {
                            newcontent.add(field, item.get(field).toString());
                        } else {
                            newcontent.add(field, "");
                        }
                    }
                    newcontent.add("status","Active");
                    newcontent.save("vm_instances");
                } else {
                    VmInstances vmInstance = keyMaps.get(item.get("name").toString());
                    if (Utils.isNotEmpty(vmInstance.uid)) {
                        fields = new ArrayList<>(Arrays.asList("zone","port","state","cpu_vendor","vcpu","os_arch","os_machine","current_memory","memory"));
                        Content obj = XN_Content.load(vmInstance.id,"vm_instances");
                        for(String field: fields) {
                            if (Utils.isNotEmpty(item.get(field))) {
                                obj.add(field, item.get(field).toString());
                            } else {
                                obj.add(field, "");
                            }
                        }
                        obj.save("vm_instances");
                    }
                }
            }
        }catch (Exception ignored){ }
    }

    @Override
    public Boolean getEditViewIsReadOnly(Object entity) {
        VmInstances vmInstances = (VmInstances)entity;
        if (Utils.isEmpty(vmInstances.uid)) {
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
                    VmInstances obj = (VmInstances)this.load(record);
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
            createVm(obj);
        }catch (Exception ignored) {}
    }

    private void createVm(Content obj) throws Exception {
        try {
            VmInstances vmInstance = new VmInstances(obj);
            if (Utils.isEmpty(vmInstance.uid) &&
                Utils.isNotEmpty(vmInstance.disk) &&
                Utils.isNotEmpty(vmInstance.image) &&
                Utils.isNotEmpty(vmInstance.offering)) {
                VmDisks vmDisks = (VmDisks)vmdisksService.load(vmInstance.disk);
                VmImages vmImages = (VmImages)vmimagesService.load(vmInstance.image);
                VmInstanceOfferings vmInstanceOfferings = (VmInstanceOfferings)vminstanceofferingsService.load(vmInstance.offering);
                Map<String, Object> maps = new HashMap<String, Object>() {{
                    put("zone", vmInstance.zone);
                    put("name", vmInstance.name);
                    put("node", vmInstance.node);
                    put("eventid", vmInstance.eventid);
                    put("disk", vmDisks.name);
                    put("disk_current", vmDisks.current);
                    put("image", vmImages.name);
                    put("image_current", vmImages.current);
                    put("image_format", vmImages.format);
                    put("cpu", vmInstanceOfferings.cpu);
                    put("memory", vmInstanceOfferings.memory);

                    put("osvariant", vmInstance.osvariant);
                    put("uuid", vmInstance.uuid);
                    put("virt_type", vmInstance.virt_type);
                    put("livecd", vmInstance.livecd);
                    put("graphics", vmInstance.graphics);
                }};
                if (kubeStackService.createVmInstances(maps)) {
                    Map<String, Object> vm = kubeStackService.getVmInstanceByName(vmInstance.zone,vmInstance.name);
                    List<String> fields = new ArrayList<>(Arrays.asList("state","uid","createtime"));
                    fields.addAll(Arrays.asList("cpu_vendor","vcpu","os_arch","os_machine","current_memory","memory"));

                    for (String field : fields) {
                        if (Utils.isNotEmpty(vm.get(field))) {
                            obj.my.put(field, vm.get(field).toString());
                        } else {
                            obj.my.put(field, "");
                        }
                    }
//                    obj.my.put("capacity",vmInstanceOfferings.capacity);
                    obj.save("vm_instances");
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
        List<Object> objs = XN_Content.loadMany(ids,"vm_instances");
        try {
            for (Object obj : objs) {
                VmInstances vmInstance = new VmInstances(obj);
                if (Utils.isNotEmpty(vmInstance.uid)) {
                    Object vm = kubeStackService.getVmInstanceByName(vmInstance.zone,vmInstance.name);
                    if (Utils.isNotEmpty(vm)) {
                        kubeStackService.deleteVmInstances(vmInstance.zone,vmInstance.name,vmInstance.eventid);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("删除失败");
        }
    }


    private static Map<String,Integer> ports = new HashMap<>();
    private static Integer websockifyServerPort = 6088;
    @Override
    public Integer startWebsockifyServer() throws Exception {
        try {
            //VmInstances vmInstance = (VmInstances)this.load(record);
            if (Utils.isEmpty("5902")) {
                throw new Exception("端口为空");
            }
            String key = Md5Util.get("zone-1"+"test123");
            if (ports.containsKey(key)) {
                log.info("key: " + ports.get(key));
                return ports.get(key);
            }
            //String vncServcieIP = kubeStackService.getVmVncServiceIp("zone-1","test123");
            String vncServcieIP = "133.133.135.131";
            if (Utils.isEmpty(vncServcieIP)) {
                throw new Exception("VNC服务IP为空");
            }
            ports.put(key,websockifyServerPort);
            websockifyServerPort ++;
            log.info("vms : {}","mytest0916");
            log.info("vncServcieIP : {}",vncServcieIP);
            log.info("port : {}","5903");
            log.info("websockifyServerPort : {}",websockifyServerPort);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String wsHost = "0.0.0.0";
                    Integer port = ports.get(key);
                    WebSocketServer server = new WebsockifyServer(new InetSocketAddress(wsHost, port), vncServcieIP, Integer.parseInt("5902"));
                    server.run();
                }
            }).start();
            return ports.get(key);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void startVm(String record) throws Exception {
        try {
            VmInstances vmInstance = (VmInstances)this.load(record);
            if (Utils.isEmpty(vmInstance.uid)) {
                throw new Exception("虚拟机还没有创建成功");
            }
            kubeStackService.startVm(vmInstance.zone,vmInstance.name);
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void restartVm(String record) throws Exception {
        try {
            VmInstances vmInstance = (VmInstances)this.load(record);
            if (Utils.isEmpty(vmInstance.uid)) {
                throw new Exception("虚拟机还没有创建成功");
            }
            kubeStackService.restartVm(vmInstance.zone,vmInstance.name);
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void stopVm(String record) throws Exception {
        try {
            VmInstances vmInstance = (VmInstances)this.load(record);
            if (Utils.isEmpty(vmInstance.uid)) {
                throw new Exception("虚拟机还没有创建成功");
            }
            kubeStackService.stopVm(vmInstance.zone,vmInstance.name);
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void forceStopVm(String record) throws Exception {
        try {
            VmInstances vmInstance = (VmInstances)this.load(record);
            if (Utils.isEmpty(vmInstance.uid)) {
                throw new Exception("虚拟机还没有创建成功");
            }
            kubeStackService.forceStopVm(vmInstance.zone,vmInstance.name);
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void createDiskImageFromDisk(Map<String, Object> Request) throws Exception {
        try {
            String record = Request.get("record").toString();
            String name = Request.get("name").toString();
            String targetpool = Request.get("targetpool").toString();
            String describe = Request.get("describe").toString();
            VmInstances vmInstance = (VmInstances)this.load(record);
            if (Utils.isEmpty(vmInstance.uid)) {
                throw new Exception("虚拟机还没有创建成功");
            }
            VmPools vmPools = (VmPools)vmpoolsService.load(targetpool);
            if (Utils.isEmpty(vmPools.uid)) {
                throw new Exception("虚拟机还没有创建成功");
            }
            Map<String, Object> maps = new HashMap<String, Object>() {{
                put("name", name);
                put("node", vmInstance.node);
                put("eventid", "");
                put("targetpool", vmPools.name);
                put("sourcepool", "pooltest999");
                put("sourcevolume", "mytest123");
            }};
            kubeStackService.createDiskImageFromDisk(maps);
        }catch (Exception e) {
            throw e;
        }
    }
}
