package com.qnkj.clouds.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.clouds.services.IKubeStackService;
import com.qnkj.common.utils.Utils;
import com.qnkj.ftpserver.ftp.MyFtpClient;
import io.github.kubestack.client.KubeStackClient;
import io.github.kubestack.client.api.models.k8s.Node;
import io.github.kubestack.client.api.models.vms.*;
import io.github.kubestack.client.api.specs.vms.virtualmachine.Domain;
import io.github.kubestack.client.api.specs.vms.virtualmachine.Lifecycle.*;
import io.github.kubestack.client.api.specs.vms.virtualmachinedisk.Lifecycle.*;
import io.github.kubestack.client.api.specs.vms.virtualmachinediskimage.Lifecycle.*;
import io.github.kubestack.client.api.specs.vms.virtualmachinediskimage.Volume;
import io.github.kubestack.client.api.specs.vms.virtualmachinepool.Lifecycle.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */

@Slf4j
@Service
public class KubeStackServiceImpl implements IKubeStackService {

//    private static KubeStackClient kubeStackClient = null;

    private static Map<String,KubeStackClient> kubeStackClientPools = new HashMap<>();

    @Override
    public void cleanKubeStackClientPools(String zone) {
        if (kubeStackClientPools.containsKey(zone)) {
            kubeStackClientPools.remove(zone);
        }
    }
    private KubeStackClient getKubeStackClient(Object zone) throws Exception {
        if (kubeStackClientPools.containsKey(zone.toString())) {
            return kubeStackClientPools.get(zone.toString());
        }
        try{
            Content vm_zone = XN_Content.load(zone.toString(),"vm_zones");
            String yaml = vm_zone.get("yaml").toString();
            if (Utils.isNotEmpty(yaml)) {
                KubeStackClient client = new KubeStackClient(yaml);
                if (Utils.isNotEmpty(client)) {
                    kubeStackClientPools.put(vm_zone.id, client);
                    return client;
                }
            }
            throw new Exception("Failed to connect to KubeStackClient");
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Failed to connect to KubeStackClient");
        }
    }
    private void initKubeStackClientPools() {
            try{
                List<Object> lists = XN_Query.create("Content").tag("vm_zones")
                        .filter("type","eic","vm_zones")
                        .filter("my.deleted","=","0")
                        .filter("my.status ", "=", "Active")
                        .filter("my.state ", "=", "Ready")
                        .end(-1).execute();
                for(Object item: lists) {
                    Content vm_zone = (Content) item;
                    if (!kubeStackClientPools.containsKey(vm_zone.id)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String yaml = vm_zone.get("yaml").toString();
                                    if (Utils.isNotEmpty(yaml)) {
                                        KubeStackClient client = new KubeStackClient(yaml);
                                        if (Utils.isNotEmpty(client)) {
                                            kubeStackClientPools.put(vm_zone.id, client);
                                        }
                                    }
                                } catch (Exception ignored) {}
                            }
                        }).start();
                    }

                }
            }catch (Exception ignored){ }
    }

    @Override
    public Boolean connectKubeStackClient(String zone,String config) throws Exception {
        try {
            KubeStackClient client = new KubeStackClient(config);
            if (Utils.isEmpty(client)) {
                return false;
            }
            kubeStackClientPools.put(zone,client);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String,String> getVmNodes() throws Exception {
        Map<String,String> nodes = new HashMap<>();
        if (kubeStackClientPools.size() == 0 ) {
            return nodes;
        }
        initKubeStackClientPools();
        try {
            List<Content> lists = XN_Query.create("Content").tag("vm_zones")
                    .filter("type", "eic", "vm_zones")
                    .filter("my.deleted", "=", "0")
                    .filter("my.status ", "=", "Active")
                    .filter("my.state ", "=", "Ready")
                    .end(-1).execute();
            Map<String, String> maps = lists.stream().collect(Collectors.toMap(v -> v.id, v -> v.my.get("name").toString(), (k1, k2) -> k1));
            for (Map.Entry<String, KubeStackClient> entry : kubeStackClientPools.entrySet()) {
                String zone = entry.getKey();
                KubeStackClient client = entry.getValue();
                if (Utils.isNotEmpty(client)) {
                    for (Node node : client.nodes().list()) {
                        String name = node.getMetadata().getName();
                        if (name.startsWith("vm.")) {
                            String key = zone+":"+name;
                            nodes.put(key,name + "("+maps.get(zone)+")");
                        }
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return nodes;
    }

    @Override
    public List<Map<String, Object>> getVmPools() throws Exception {
        List<Map<String, Object>> vmPools = new ArrayList<>();
        initKubeStackClientPools();
        if (kubeStackClientPools.size() == 0 ) {
            return vmPools;
        }
        for (Map.Entry<String, KubeStackClient> entry : kubeStackClientPools.entrySet()) {
            String zone = entry.getKey();
            KubeStackClient client = entry.getValue();
            if (Utils.isNotEmpty(client)) {
                for (VirtualMachinePool item : client.virtualMachinePools().list()) {
                    Map<String, Object> vmPool = new HashMap<>();
                    vmPool.put("zone",zone);
                    vmPool.put("name",item.getMetadata().getName());
                    vmPool.put("namespace",item.getMetadata().getNamespace());
                    vmPool.put("node",item.getSpec().getNodeName());
                    vmPool.put("createtime",item.getMetadata().getCreationTimestamp());
                    vmPool.put("uid",item.getMetadata().getUid());
                    vmPool.put("eventid",item.getMetadata().getLabels().get("kubestack.label.event.id"));
                    if (Utils.isNotEmpty(item.getSpec().getPool())) {
                        vmPool.put("url", item.getSpec().getPool().getUrl());
                        vmPool.put("capacity", item.getSpec().getPool().getCapacity());
                        vmPool.put("autostart", item.getSpec().getPool().getAutostart());
                        vmPool.put("content", item.getSpec().getPool().getContent());
                        vmPool.put("free", item.getSpec().getPool().getFree());
                        vmPool.put("state", item.getSpec().getPool().getState());
                        vmPool.put("uuid", item.getSpec().getPool().getUuid());
                    }
                    vmPools.add(vmPool);
                }
            }
        }
        return vmPools;
    }

    @Override
    public List<Map<String, Object>> getVmDisks() throws Exception {
        List<Map<String, Object>> vmDisks = new ArrayList<>();
        initKubeStackClientPools();
        if (kubeStackClientPools.size() == 0 ) {
            return vmDisks;
        }
        for (Map.Entry<String, KubeStackClient> entry : kubeStackClientPools.entrySet()) {
            String zone = entry.getKey();
            KubeStackClient client = entry.getValue();
            if (Utils.isNotEmpty(client)) {
                for (VirtualMachineDisk item : client.virtualMachineDisks().list()) {
                    Map<String, Object> vmPool = new HashMap<>();
                    vmPool.put("zone",zone);
                    vmPool.put("name",item.getMetadata().getName());
                    vmPool.put("namespace",item.getMetadata().getNamespace());
                    vmPool.put("node",item.getSpec().getNodeName());
                    vmPool.put("createtime",item.getMetadata().getCreationTimestamp());
                    vmPool.put("uid",item.getMetadata().getUid());
                    vmPool.put("eventid",item.getMetadata().getLabels().get("kubestack.label.event.id"));
                    if (Utils.isNotEmpty(item.getSpec().getVolume())) {
                        vmPool.put("current", item.getSpec().getVolume().getCurrent());
                        vmPool.put("actual_size", item.getSpec().getVolume().getActual_size());
                        vmPool.put("cluster_size", item.getSpec().getVolume().getCluster_size());
                        vmPool.put("dirty_flag", item.getSpec().getVolume().getDirty_flag());
                        vmPool.put("disktype", item.getSpec().getVolume().getDisktype());
                        vmPool.put("format", item.getSpec().getVolume().getFormat());
                        vmPool.put("pool", item.getSpec().getVolume().getPool());
                        vmPool.put("current", item.getSpec().getVolume().getCurrent());
                        vmPool.put("poolname", item.getSpec().getVolume().getPoolname());
                        vmPool.put("uni", item.getSpec().getVolume().getUni());
                        vmPool.put("virtual_size", item.getSpec().getVolume().getVirtual_size());
                    }
                    if (Utils.isNotEmpty(item.getSpec().getStatus())) {
                        Object conditions = item.getSpec().getStatus().getAdditionalProperties().get("conditions");
                        if (Utils.isNotEmpty(conditions)) {
                            Object state = ((Map<String,Object>)conditions).get("state");
                            if (Utils.isNotEmpty(state)) {
                                Object waiting = ((Map<String,Object>)state).get("waiting");
                                if (Utils.isNotEmpty(waiting)) {
                                    Object reason = ((Map<String,Object>)waiting).get("reason");
                                    if (Utils.isNotEmpty(reason)) {
                                        if ("Ready".equals(reason)) {
                                            vmPool.put("state", "Ready");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    vmDisks.add(vmPool);
                }
            }
        }
        return vmDisks;
    }

    @Override
    public List<Map<String, Object>> getVmDiskImages() throws Exception {
        List<Map<String, Object>> vmDiskImages = new ArrayList<>();
        initKubeStackClientPools();
        if (kubeStackClientPools.size() == 0 ) {
            return vmDiskImages;
        }
        for (Map.Entry<String, KubeStackClient> entry : kubeStackClientPools.entrySet()) {
            String zone = entry.getKey();
            KubeStackClient client = entry.getValue();
            if (Utils.isNotEmpty(client)) {
                for (VirtualMachineDiskImage item : client.virtualMachineDiskImages().list()) {
                    Map<String, Object> vmPool = new HashMap<>();
                    vmPool.put("zone",zone);
                    vmPool.put("name",item.getMetadata().getName());
                    vmPool.put("namespace",item.getMetadata().getNamespace());
                    vmPool.put("node",item.getSpec().getNodeName());
                    vmPool.put("createtime",item.getMetadata().getCreationTimestamp());
                    vmPool.put("uid",item.getMetadata().getUid());
                    vmPool.put("eventid",item.getMetadata().getLabels().get("kubestack.label.event.id"));
                    Volume volume = item.getSpec().getVolume();
                    if (Utils.isNotEmpty(volume)) {
                        vmPool.put("current",volume.getCurrent());
                        vmPool.put("format",volume.getFormat());
                        vmPool.put("pool",volume.getPool());
                    }
                    if (Utils.isNotEmpty(item.getSpec().getStatus())) {
                        Object conditions = item.getSpec().getStatus().getAdditionalProperties().get("conditions");
                        if (Utils.isNotEmpty(conditions)) {
                            Object state = ((Map<String,Object>)conditions).get("state");
                            if (Utils.isNotEmpty(state)) {
                                Object waiting = ((Map<String,Object>)state).get("waiting");
                                if (Utils.isNotEmpty(waiting)) {
                                    Object reason = ((Map<String,Object>)waiting).get("reason");
                                    if (Utils.isNotEmpty(reason)) {
                                        if ("Ready".equals(reason)) {
                                            vmPool.put("state", "Ready");
                                        }
                                    }
                                }
                            }
                        }

                    }
                    vmDiskImages.add(vmPool);
                }
            }
        }
        return vmDiskImages;
    }

    @Override
    public List<Map<String, Object>> getVmDiskSnapShots() throws Exception {
        List<Map<String, Object>> vmDiskSnapShots = new ArrayList<>();
        initKubeStackClientPools();
        if (kubeStackClientPools.size() == 0 ) {
            return vmDiskSnapShots;
        }
        for (Map.Entry<String, KubeStackClient> entry : kubeStackClientPools.entrySet()) {
            String zone = entry.getKey();
            KubeStackClient client = entry.getValue();
            if (Utils.isNotEmpty(client)) {
                for (VirtualMachineDiskSnapshot item : client.virtualMachineDiskSnapshots().list()) {
                    Map<String, Object> vmPool = new HashMap<>();
                    vmPool.put("zone",zone);
                    vmPool.put("name",item.getMetadata().getName());
                    vmPool.put("namespace",item.getMetadata().getNamespace());
                    vmPool.put("node",item.getSpec().getNodeName());
                    vmPool.put("createtime",item.getMetadata().getCreationTimestamp());
                    vmDiskSnapShots.add(vmPool);
                }
            }
        }
        return vmDiskSnapShots;
    }

    @Override
    public List<Map<String, Object>> getVms() throws Exception {
        List<Map<String, Object>> vmDiskImages = new ArrayList<>();
        initKubeStackClientPools();
        if (kubeStackClientPools.size() == 0 ) {
            return vmDiskImages;
        }
        for (Map.Entry<String, KubeStackClient> entry : kubeStackClientPools.entrySet()) {
            String zone = entry.getKey();
            KubeStackClient client = entry.getValue();
            if (Utils.isNotEmpty(client)) {
                for (VirtualMachine item : client.virtualMachines().list()) {
                    Map<String, Object> vmPool = new HashMap<>();
                    vmPool.put("zone",zone);
                    vmPool.put("name",item.getMetadata().getName());
                    vmPool.put("namespace",item.getMetadata().getNamespace());
                    vmPool.put("node",item.getSpec().getNodeName());
                    vmPool.put("createtime",item.getMetadata().getCreationTimestamp());
                    vmPool.put("uid",item.getMetadata().getUid());
                    vmPool.put("eventid",item.getMetadata().getLabels().get("kubestack.label.event.id"));
                    vmPool.put("state",item.getSpec().getPowerstate());
                    if (Utils.isNotEmpty(item.getSpec().getDomain())) {
                        if (Utils.isNotEmpty(item.getSpec().getDomain().getCpu()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getCpu().getVendor()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getCpu().getVendor().getText())) {
                            vmPool.put("cpu_vendor", item.getSpec().getDomain().getCpu().getVendor().getText());
                        }
                        if (Utils.isNotEmpty(item.getSpec().getDomain().getVcpu()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getVcpu().getText())) {
                            vmPool.put("vcpu", item.getSpec().getDomain().getVcpu().getText());
                        }
                        if (Utils.isNotEmpty(item.getSpec().getDomain().getOs()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getOs().getType()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getOs().getType().get_arch())) {
                            vmPool.put("os_arch", item.getSpec().getDomain().getOs().getType().get_arch());
                        }
                        if (Utils.isNotEmpty(item.getSpec().getDomain().getOs()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getOs().getType()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getOs().getType().get_machine())) {
                            vmPool.put("os_machine", item.getSpec().getDomain().getOs().getType().get_machine());
                        }
                        if (Utils.isNotEmpty(item.getSpec().getDomain().getCurrentMemory()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getCurrentMemory().getText()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getCurrentMemory().get_unit())) {
                            vmPool.put("current_memory", item.getSpec().getDomain().getCurrentMemory().getText() + item.getSpec().getDomain().getCurrentMemory().get_unit());
                        }
                        if (Utils.isNotEmpty(item.getSpec().getDomain().get_type())) {
                            vmPool.put("type", item.getSpec().getDomain().get_type());
                        }
                        if (Utils.isNotEmpty(item.getSpec().getDomain().getMemory()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getMemory().getText()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getMemory().get_unit())) {
                            if ("KiB".equals(item.getSpec().getDomain().getMemory().get_unit())) {
                                Integer memory = Integer.parseInt(item.getSpec().getDomain().getMemory().getText());
                                memory = memory / 1024 / 1024;
                                vmPool.put("memory", memory + "GB");
                            } else {
                                vmPool.put("memory", item.getSpec().getDomain().getMemory().getText() + item.getSpec().getDomain().getMemory().get_unit());
                            }
                        }
                        if (Utils.isNotEmpty(item.getSpec().getDomain().getDevices()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getDevices().getGraphics()) &&
                                Utils.isNotEmpty(item.getSpec().getDomain().getDevices().getGraphics().get(0))) {
                            ArrayList<Domain.Devices.Graphics> graphicsArray = item.getSpec().getDomain().getDevices().getGraphics();
                            String port = "";
                            for (int i=0;i<graphicsArray.size();i++) {
                                Domain.Devices.Graphics g = graphicsArray.get(i);
                                if (g.get_type().equals("vnc")) {
                                    port = g.get_port();
                                }
                                else {
                                    continue;
                                }
                            }
                            vmPool.put("port", port);
                        }
                    }
                    vmDiskImages.add(vmPool);
                }
            }
        }
        return vmDiskImages;
    }

    @Override
    public List<Map<String, Object>> getVmNetWorks() throws Exception {
        List<Map<String, Object>> vmDiskImages = new ArrayList<>();
//        if (Utils.isNotEmpty(kubeStackClient)) {
//            for (VirtualMachineNetwork item : kubeStackClient.virtualMachineNetworks().list()) {
//                Map<String, Object> vmPool = new HashMap<>();
//                vmPool.put("name",item.getMetadata().getName());
//                vmPool.put("namespace",item.getMetadata().getNamespace());
//                vmPool.put("node",item.getSpec().getNodeName());
//                vmPool.put("createtime",item.getMetadata().getCreationTimestamp());
//                vmDiskImages.add(vmPool);
//            }
//        }
        return vmDiskImages;
    }

    @Override
    public Boolean createVmPools(Map<String, Object> maps) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(maps.get("zone"));
        if (Utils.isNotEmpty(kubeStackClient)) {
            String name = maps.get("name").toString();
            String vmpooltype = maps.get("vmpooltype").toString();
            String content = maps.get("content").toString();
            String node = maps.get("node").toString();
            String url = maps.get("url").toString();
            String eventid = maps.get("eventid").toString();
            String autostart = maps.get("autostart").toString();

            CreatePool createPool = new CreatePool();
            createPool.setType(vmpooltype);
            createPool.setContent(content);
            if (autostart.equals("1")) {
                createPool.setAutostart(true);
            } else {
                createPool.setAutostart(false);
            }
            createPool.setUrl(url);

            boolean successful = kubeStackClient.virtualMachinePools().createPool(name, node, createPool, eventid);
            return successful;
        }
        return false;
    }

    @Override
    public Map<String, Object> getVmPoolByName(String zone,String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            VirtualMachinePool item = kubeStackClient.virtualMachinePools().get(name);
            Map<String, Object> vmPool = new HashMap<>();
            vmPool.put("name",item.getMetadata().getName());
            vmPool.put("namespace",item.getMetadata().getNamespace());
            vmPool.put("node",item.getSpec().getNodeName());
            vmPool.put("createtime",item.getMetadata().getCreationTimestamp());
            vmPool.put("uid",item.getMetadata().getUid());
            vmPool.put("eventid",item.getMetadata().getLabels().get("kubestack.label.event.id"));
            if (Utils.isNotEmpty(item.getSpec().getPool())) {
                vmPool.put("url", item.getSpec().getPool().getUrl());
                vmPool.put("capacity", item.getSpec().getPool().getCapacity());
                vmPool.put("autostart", item.getSpec().getPool().getAutostart());
                vmPool.put("content", item.getSpec().getPool().getContent());
                vmPool.put("free", item.getSpec().getPool().getFree());
                vmPool.put("state", item.getSpec().getPool().getState());
                vmPool.put("uuid", item.getSpec().getPool().getUuid());
            }
            return vmPool;
        }
        return null;
    }

    @Override
    public Boolean deleteVmPools(String zone,String name, String vmPoolType, String eventid) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            DeletePool deletePool = new DeletePool();
            if (Utils.isNotEmpty(vmPoolType)) {
                deletePool.setType(vmPoolType);
            } else {
                deletePool.setType("localfs");
            }
            return kubeStackClient.virtualMachinePools().deletePool(name, deletePool, eventid);
        }
        return false;
    }


    @Override
    public Boolean createVmDisks(Map<String, Object> maps) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(maps.get("zone"));
        if (Utils.isNotEmpty(kubeStackClient)) {
            String name = maps.get("name").toString();
            String vmdisktype = maps.get("vmdisktype").toString();
            String node = maps.get("node").toString();
            String eventid = maps.get("eventid").toString();
            String pool = maps.get("pool").toString();
            String capacity = maps.get("capacity").toString();
            String format = maps.get("format").toString();

            CreateDisk createDisk = new CreateDisk();
            createDisk.setType(vmdisktype);
            createDisk.setPool(pool);
            // bytes 10G
            // Long size = 10L*1024*1024*1024;
            createDisk.setCapacity(capacity);
            createDisk.setFormat(format);
            boolean successful = kubeStackClient.virtualMachineDisks().createDisk(name, node, createDisk, eventid);
            return successful;
        }
        return false;
    }

    @Override
    public Map<String, Object> getVmDiskByName(String zone,String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            VirtualMachineDisk item = kubeStackClient.virtualMachineDisks().get(name);
            Map<String, Object> vmDisk = new HashMap<>();
            vmDisk.put("name",item.getMetadata().getName());
            vmDisk.put("namespace",item.getMetadata().getNamespace());
            vmDisk.put("node",item.getSpec().getNodeName());
            vmDisk.put("createtime",item.getMetadata().getCreationTimestamp());
            vmDisk.put("uid",item.getMetadata().getUid());
            vmDisk.put("eventid",item.getMetadata().getLabels().get("kubestack.label.event.id"));
            if (Utils.isNotEmpty(item.getSpec().getVolume())) {
                vmDisk.put("current", item.getSpec().getVolume().getCurrent());
                vmDisk.put("actual_size", item.getSpec().getVolume().getActual_size());
                vmDisk.put("cluster_size", item.getSpec().getVolume().getCluster_size());
                vmDisk.put("dirty_flag", item.getSpec().getVolume().getDirty_flag());
                vmDisk.put("disktype", item.getSpec().getVolume().getDisktype());
                vmDisk.put("format", item.getSpec().getVolume().getFormat());
                vmDisk.put("pool", item.getSpec().getVolume().getPool());
                vmDisk.put("poolname", item.getSpec().getVolume().getPoolname());
                vmDisk.put("uni", item.getSpec().getVolume().getUni());
                vmDisk.put("virtual_size", item.getSpec().getVolume().getVirtual_size());
            }
            return vmDisk;
        }
        return null;
    }

    @Override
    public Boolean deleteVmDisks(String zone, String name, String vmDiskType,String pool) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            DeleteDisk deleteDisk = new DeleteDisk();
            if (Utils.isNotEmpty(vmDiskType)) {
                deleteDisk.setType(vmDiskType);
            } else {
                deleteDisk.setType("localfs");
            }
            deleteDisk.setPool(pool);
            return kubeStackClient.virtualMachineDisks().deleteDisk(name, deleteDisk);
        }
        return false;
    }

    @Override
    public Boolean createVmDiskImages(Map<String, Object> maps) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(maps.get("zone"));
        if (Utils.isNotEmpty(kubeStackClient)) {
            String name = maps.get("name").toString();
            String node = maps.get("node").toString();
            String eventid = maps.get("eventid").toString();
            String targetpool = maps.get("pool").toString();
            String mediatype = maps.get("mediatype").toString();
            if ("qcow2".equals(mediatype)) {
                String source = maps.get("source").toString();
                CreateDiskImage createDiskImage = new CreateDiskImage();
                createDiskImage.setTargetPool(targetpool);
                createDiskImage.setImageType(mediatype);
                createDiskImage.setSource(source);
                boolean successful = kubeStackClient.virtualMachineDiskImages().createDiskImage(name, node, createDiskImage, eventid);
                return successful;
            } else {
                String imagefile = maps.get("imagefile").toString();
                String url = maps.get("url").toString();
                String cachefile = url + "/" + imagefile;
                if (new File(cachefile).exists()) {
                    CreateDiskImage createDiskImage = new CreateDiskImage();
                    createDiskImage.setTargetPool(targetpool);
                    createDiskImage.setImageType(mediatype);
                    createDiskImage.setSource(cachefile);
                    boolean successful = kubeStackClient.virtualMachineDiskImages().createDiskImage(name, node, createDiskImage, eventid);
                    return successful;
                } else {
                    String serverip = maps.get("serverip").toString();
                    String port = maps.get("port").toString();
                    String username = maps.get("username").toString();
                    String password = maps.get("password").toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyFtpClient client = new MyFtpClient();
                                if (client.connectServer(serverip, Integer.parseInt(port), username, password)) {
                                    client.downloadFile(imagefile, cachefile);
                                }
                                if (new File(cachefile).exists()) {
                                    CreateDiskImage createDiskImage = new CreateDiskImage();
                                    createDiskImage.setTargetPool(targetpool);
                                    createDiskImage.setImageType(mediatype);
                                    createDiskImage.setSource(cachefile);
                                    boolean successful = kubeStackClient.virtualMachineDiskImages().createDiskImage(name, node, createDiskImage, eventid);
                                }
                            }catch (Exception ignored) {}
                        }
                    }).start();
                   return true;
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> getVmDiskImageByName(String zone,String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            VirtualMachineDiskImage item = kubeStackClient.virtualMachineDiskImages().get(name);
            Map<String, Object> vmDiskImage = new HashMap<>();
            vmDiskImage.put("name",item.getMetadata().getName());
            vmDiskImage.put("namespace",item.getMetadata().getNamespace());
            vmDiskImage.put("node",item.getSpec().getNodeName());
            vmDiskImage.put("createtime",item.getMetadata().getCreationTimestamp());
            vmDiskImage.put("uid",item.getMetadata().getUid());
            vmDiskImage.put("eventid",item.getMetadata().getLabels().get("kubestack.label.event.id"));
            return vmDiskImage;
        }
        return null;
    }

    @Override
    public Boolean deleteVmDiskImages(String zone,String name, String sourcePool) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            DeleteDiskImage deleteDiskImage = new DeleteDiskImage();
            deleteDiskImage.setSourcePool(sourcePool);
            return kubeStackClient.virtualMachineDiskImages().deleteDiskImage(name, deleteDiskImage);
        }
        return false;
    }


    @Override
    public Boolean createVmInstances(Map<String, Object> maps) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(maps.get("zone"));
        if (Utils.isNotEmpty(kubeStackClient)) {
            String name = maps.get("name").toString();
            String node = maps.get("node").toString();
            String eventid = maps.get("eventid").toString();
            String disk = maps.get("disk").toString();
            String disk_current = maps.get("disk_current").toString();
            String image = maps.get("image").toString();
            String image_current = maps.get("image_current").toString();
            String image_format = maps.get("image_format").toString();
            String cpu = maps.get("cpu").toString();
            String memory = maps.get("memory").toString();

            String osvariant = maps.get("osvariant").toString();
            String uuid = maps.get("uuid").toString();
            String virt_type = maps.get("virt_type").toString();
            String livecd = maps.get("livecd").toString();
            String graphics = maps.get("graphics").toString();

            CreateAndStartVMFromISO createAndStartVMFromISO = new CreateAndStartVMFromISO();
            // default value
            createAndStartVMFromISO.setMetadata("uuid=" + uuid);
            createAndStartVMFromISO.setVirt_type(virt_type);
            createAndStartVMFromISO.setOs_variant(osvariant);
            createAndStartVMFromISO.setLivecd(livecd.equals("1")?"True":"False");
            createAndStartVMFromISO.setNoautoconsole(true);

            // calculationSpecification
            calculationSpecification(createAndStartVMFromISO,cpu,memory);

            // cdrom
//    		createAndStartVMFromISO.setCdrom("/var/lib/libvirt/iso/centos7-minimal-1511.iso");
            // Disk and QoS for 1 disk and many disks
//            createAndStartVMFromISO.setDisk("/var/lib/libvirt/cstor/170dd9accdd174caced76b0db2230/170dd9accdd174caced76b0db2230/centos7/centos7,target=vda,read_bytes_sec=1024000000,write_bytes_sec=1024000000 " + getOtherCDROMs());
//            createAndStartVMFromISO.setDisk("/var/lib/libvirt/pooltest999/mytest123/mytest123,target=vda,read_bytes_sec=1024000000,write_bytes_sec=1024000000 " + getOtherCDROMs());
            createAndStartVMFromISO.setDisk(getDiskParams(disk_current,image_current));
//		createAndStartVMFromISO.setNetwork("type=bridge,source=virbr0,inbound=102400,outbound=102400");
//		createAndStartVMFromISO.setNetwork("type=l2bridge,source=br-native,inbound=102400,outbound=102400");
            createAndStartVMFromISO.setNetwork("type=bridge,source=virbr0,inbound=102400,outbound=102400");
//		createAndStartVMFromISO.setNetwork("type=l2bridge,source=br-native,inbound=102400,outbound=102400");
//      if you want to use l3bridge, please first execute the command on your master node, 'kubeovn-adm create-switch --name switch8888 --subnet 192.168.5.0/24'
//		createAndStartVMFromISO.setNetwork("type=l3bridge,source=br-int,ip=2001:198:10::254,switch=switch2,inbound=102400,outbound=102400");

            // consoleMode amd passowrd
            if (Utils.isEmpty(graphics)) {
                createAndStartVMFromISO.setGraphics("vnc,listen=0.0.0.0");
            } else {
                createAndStartVMFromISO.setGraphics("vnc,listen=0.0.0.0" + getconsolePassword(graphics));
            }
//		createAndStartVMFromISO.setGraphics("rdp,listen=0.0.0.0" + getconsolePassword("123456"));
//		createAndStartVMFromISO.setGraphics("spice,listen=0.0.0.0" + getconsolePassword("567890"));
//		createAndStartVMFromISO.setRedirdev("lusb,type=tcp,server=192.168.1.1:4000");
            boolean successful = kubeStackClient.virtualMachines().createAndStartVMFromISO(name, node, createAndStartVMFromISO, eventid);
            return successful;

        }
        return false;
    }
    private static String getDiskParams(String disk_current,String image_current) {
        StringBuilder sb = new StringBuilder();
        sb.append(disk_current);
        sb.append(",target=vda,read_bytes_sec=1024000000,write_bytes_sec=1024000000");
        sb.append(" --disk ");
        sb.append(image_current);
        sb.append(",device=cdrom,perms=ro");
        return sb.toString();
    }

    private static void calculationSpecification(CreateAndStartVMFromISO createAndStartVMFromISO,String cpu,String memory) {
        createAndStartVMFromISO.setMemory(String.valueOf(Integer.parseInt(memory) * 1024));
        createAndStartVMFromISO.setVcpus("2" + getCPUSet("1-4,6,8") + ",maxvcpus="+cpu+",cores="+cpu+",sockets=1"+",threads=1");
    }

    private static String getCPUSet(String cpuset) {
        return (cpuset == null || cpuset.length() == 0)
                ? "" :",cpuset=" + cpuset;
    }

    private static String getconsolePassword(String pwd) {
        return (pwd == null || pwd.length() == 0) ? "" : ",password=abcdefg";
    }

    private static String getOtherCDROMs() {
        return "--disk /var/lib/libvirt/iso/centos7-minimal-1511.iso,device=cdrom,perms=ro";
    }

    @Override
    public Map<String, Object> getVmInstanceByName(String zone,String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            VirtualMachine item = kubeStackClient.virtualMachines().get(name);
            Map<String, Object> vm = new HashMap<>();
            vm.put("name",item.getMetadata().getName());
            vm.put("namespace",item.getMetadata().getNamespace());
            vm.put("node",item.getSpec().getNodeName());
            vm.put("createtime",item.getMetadata().getCreationTimestamp());
            vm.put("uid",item.getMetadata().getUid());
            vm.put("eventid",item.getMetadata().getLabels().get("kubestack.label.event.id"));
            vm.put("state",item.getSpec().getPowerstate());
            if (Utils.isNotEmpty(item.getSpec().getDomain())) {
                if (Utils.isNotEmpty(item.getSpec().getDomain().getCpu()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getCpu().getVendor()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getCpu().getVendor().getText())) {
                    vm.put("cpu_vendor", item.getSpec().getDomain().getCpu().getVendor().getText());
                }
                if (Utils.isNotEmpty(item.getSpec().getDomain().getVcpu()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getVcpu().getText())) {
                    vm.put("vcpu", item.getSpec().getDomain().getVcpu().getText());
                }
                if (Utils.isNotEmpty(item.getSpec().getDomain().getOs()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getOs().getType()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getOs().getType().get_arch())) {
                    vm.put("os_arch", item.getSpec().getDomain().getOs().getType().get_arch());
                }
                if (Utils.isNotEmpty(item.getSpec().getDomain().getOs()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getOs().getType()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getOs().getType().get_machine())) {
                    vm.put("os_machine", item.getSpec().getDomain().getOs().getType().get_machine());
                }
                if (Utils.isNotEmpty(item.getSpec().getDomain().getCurrentMemory()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getCurrentMemory().getText()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getCurrentMemory().get_unit())) {
                    vm.put("current_memory", item.getSpec().getDomain().getCurrentMemory().getText() + item.getSpec().getDomain().getCurrentMemory().get_unit());
                }
                if (Utils.isNotEmpty(item.getSpec().getDomain().get_type())) {
                    vm.put("type", item.getSpec().getDomain().get_type());
                }
                if (Utils.isNotEmpty(item.getSpec().getDomain().getMemory()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getMemory().getText()) &&
                        Utils.isNotEmpty(item.getSpec().getDomain().getMemory().get_unit())) {
                    vm.put("memory", item.getSpec().getDomain().getMemory().getText() + item.getSpec().getDomain().getMemory().get_unit());
                }
            }
            return vm;
        }
        return null;
    }

    @Override
    public Boolean deleteVmInstances(String zone, String name, String eventid) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            DeleteVM deleteVM = new DeleteVM();
            return kubeStackClient.virtualMachines().deleteVM(name,deleteVM, eventid);
        }
        return false;
    }

    @Override
    public Boolean startVm(String zone,String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            return kubeStackClient.virtualMachines().startVM(name,new StartVM());
        }
        return false;
    }

    @Override
    public Boolean restartVm(String zone,String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            return kubeStackClient.virtualMachines().rebootVM(name,new RebootVM());
        }
        return false;
    }

    @Override
    public Boolean stopVm(String zone,String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            return kubeStackClient.virtualMachines().stopVM(name,new StopVM());
        }
        return false;
    }

    @Override
    public Boolean forceStopVm(String zone,String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            return kubeStackClient.virtualMachines().stopVMForce(name,new StopVMForce());
        }
        return false;
    }

    @Override
    public String getVmVncServiceIp(String zone, String name) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(zone);
        if (Utils.isNotEmpty(kubeStackClient)) {
            String nodeName = kubeStackClient.virtualMachines().get(name).getMetadata().getLabels().get("host");
            return kubeStackClient.nodes().get(nodeName).getMetadata().getAnnotations().get("THISIP");
        }
        return "";
    }

    @Override
    public Boolean createDiskImageFromDisk(Map<String, Object> maps) throws Exception {
        KubeStackClient kubeStackClient = getKubeStackClient(maps.get("zone"));
        if (Utils.isNotEmpty(kubeStackClient)) {
            String name = maps.get("name").toString();
            String node = maps.get("node").toString();
            String eventid = maps.get("eventid").toString();

            String targetpool = maps.get("targetpool").toString();
            String sourcepool = maps.get("sourcepool").toString();
            String sourcevolume = maps.get("sourcevolume").toString();

            CreateDiskImageFromDisk createDiskImageFromDisk = new CreateDiskImageFromDisk();
            createDiskImageFromDisk.setTargetPool(targetpool);
            createDiskImageFromDisk.setSourcePool(sourcepool);
            createDiskImageFromDisk.setSourceVolume(sourcevolume);
            return kubeStackClient.virtualMachineDiskImages()
                    .createDiskImageFromDisk(name, node, createDiskImageFromDisk, eventid);
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getPhysicalMachiness() throws Exception {
        List<Map<String, Object>> nodes = new ArrayList<>();
        initKubeStackClientPools();
        if (kubeStackClientPools.size() == 0 ) {
            return nodes;
        }
        for (Map.Entry<String, KubeStackClient> entry : kubeStackClientPools.entrySet()) {
            String zone = entry.getKey();
            KubeStackClient client = entry.getValue();
            if (Utils.isNotEmpty(client)) {
                for (Node node : client.nodes().list()) {
                    log.info(node.toString());
                    Map<String, Object> item = new HashMap<>();
                    item.put("zone",zone);
                    item.put("name",node.getMetadata().getName());
                    item.put("createtime",node.getMetadata().getCreationTimestamp());
                    item.put("uid",node.getMetadata().getUid());
                    if (Utils.isNotEmpty(node.getStatus())) {
                        item.put("internalip", node.getStatus().getAddresses().get(0).getAddress());
                        item.put("hostname", node.getStatus().getAddresses().get(1).getAddress());
                        item.put("cpu", node.getStatus().getCapacity().get("cpu"));
                        item.put("ephemeral_storage", node.getStatus().getCapacity().get("ephemeral-storage"));
                        item.put("memory", node.getStatus().getCapacity().get("memory"));
                        item.put("state", node.getStatus().getConditions().get(3).getStatus().equals("True")?"Ready":"NotReady");
                        item.put("architecture", node.getStatus().getNodeInfo().getArchitecture());
                        item.put("kernelVersion", node.getStatus().getNodeInfo().getKernelVersion());
                        item.put("kubeletVersion", node.getStatus().getNodeInfo().getKubeletVersion());
                        item.put("operatingSystem", node.getStatus().getNodeInfo().getOperatingSystem());
                        item.put("osImage", node.getStatus().getNodeInfo().getOsImage());
                    }
                    nodes.add(item);
                }
            }
        }

        return nodes;
    }
}
