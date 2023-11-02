package com.qnkj.clouds.modules.VmImages.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.clouds.modules.VmImageServers.entitys.VmImageServers;
import com.qnkj.clouds.modules.VmImageServers.services.IVmImageServersService;
import com.qnkj.clouds.modules.VmImages.entitys.VmImages;
import com.qnkj.clouds.modules.VmImages.services.IVmImagesService;
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
 * create date 2023-06-27
 * @author Auto Generator
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class VmImagesServiceImpl implements IVmImagesService {
    private final IKubeStackService kubeStackService;
    private final IVmPoolsService vmpoolsService;
    private final IVmImageServersService vmimageserversService;

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
        try {
            List<Object> lists = XN_Query.create("Content").tag("vm_images")
                    .filter("type", "eic", "vm_images")
                    .filter("my.deleted", "=", "0")
                    .end(-1).execute();
            Map<String, VmImages> keyMaps = new HashMap<>();
            for (Object item : lists) {
                Content vm_image = (Content) item;
                keyMaps.put(vm_image.my.get("name").toString(), new VmImages(vm_image));
            }
            List<Map<String, Object>> vmDiskImages = kubeStackService.getVmDiskImages();
            log.info("VmImages : {}", vmDiskImages);
            List<String> fields = new ArrayList<>(Arrays.asList("zone","uid","eventid","node","createtime"));
            fields.addAll(Arrays.asList("current","format","pool"));
             for(Map<String, Object> vmPool: vmDiskImages) {
                if (!keyMaps.containsKey(vmPool.get("name").toString())) {
                    Content newcontent = XN_Content.create("vm_images", "", ProfileUtils.getCurrentProfileId());
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
                    if (Utils.isNotEmpty(vmPool.get("state"))) {
                        newcontent.add("state", vmPool.get("state").toString());
                    } else {
                        newcontent.add("state", "NotReady");
                    }

                    newcontent.add("status", "Active");
                    newcontent.save("vm_images");
                } else {
                    VmImages vmImage = keyMaps.get(vmPool.get("name").toString());
                    Content obj = XN_Content.load(vmImage.id,"vm_images");
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
                    obj.save("vm_images");
                }
            }

        } catch (Exception ignored) {
        }
    }

    @Override
    public Boolean getEditViewIsReadOnly(Object entity) {
        VmImages vmImages = (VmImages)entity;
        if (Utils.isEmpty(vmImages.uid)) {
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
                    VmImages obj = (VmImages)this.load(record);
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
            createDiskImage(obj);
        }catch (Exception ignored) {}
    }

    private void createDiskImage(Content obj) throws Exception {
        try {
            VmImages vmimage = new VmImages(obj);
            if (Utils.isEmpty(vmimage.uid) && Utils.isNotEmpty(vmimage.poolname)) {
                VmPools vmPools = (VmPools)vmpoolsService.load(vmimage.poolname);
                Map<String, Object> maps = new HashMap<String, Object>() {{
                    put("zone", vmimage.zone);
                    put("name", vmimage.name);
                    put("node", vmimage.node);
                    put("eventid", vmimage.eventid);
                    put("pool", vmPools.name);
                    put("mediatype", vmimage.mediatype);
                    put("imagefile", vmimage.imagefile);
                    put("source", vmimage.source);
                }};
                if (Utils.isNotEmpty(vmimage.imageserver)) {
                    VmImageServers vmImageServer = (VmImageServers) vmimageserversService.load(vmimage.imageserver);
                    maps.put("url", vmImageServer.url);
                    maps.put("serverip", vmImageServer.serverip);
                    maps.put("port", vmImageServer.port);
                    maps.put("username", vmImageServer.username);
                    maps.put("password", vmImageServer.password);
                }
                if (kubeStackService.createVmDiskImages(maps)) {
                    Map<String, Object> vmDiskImage = kubeStackService.getVmDiskImageByName(vmimage.zone,vmimage.name);
                    List<String> fields = new ArrayList<>(Arrays.asList("uid", "createtime"));
                    for (String field : fields) {
                        if (Utils.isNotEmpty(vmDiskImage.get(field))) {
                            obj.my.put(field, vmDiskImage.get(field).toString());
                        } else {
                            obj.my.put(field, "");
                        }
                    }
                    obj.my.put("pool",vmPools.name);
                    obj.save("vm_images");
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
        List<Object> objs = XN_Content.loadMany(ids,"vm_images");
        try {
            for (Object obj : objs) {
                VmImages vmImage = new VmImages(obj);
                if (Utils.isNotEmpty(vmImage.uid)) {
                    try {
                        Object disk = kubeStackService.getVmDiskImageByName(vmImage.zone,vmImage.name);
                        if (Utils.isNotEmpty(disk)) {
                            if (Utils.isNotEmpty(vmImage.pool)) {
                                kubeStackService.deleteVmDiskImages(vmImage.zone,vmImage.name, vmImage.pool);
                            } else if (Utils.isNotEmpty(vmImage.poolname)) {
                                VmPools vmpool = (VmPools) vmpoolsService.load(vmImage.poolname);
                                kubeStackService.deleteVmDiskImages(vmImage.zone,vmImage.name, vmpool.name);
                            }
                        }
                    }catch (Exception ignored) {}
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("删除失败");
        }
    }
}
