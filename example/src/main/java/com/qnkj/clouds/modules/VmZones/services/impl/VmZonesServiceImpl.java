package com.qnkj.clouds.modules.VmZones.services.impl;

import com.github.restapi.XN_Query;
import com.github.restapi.XN_Rest;
import com.github.restapi.models.Content;
import com.qnkj.clouds.modules.VmZones.entitys.VmZones;
import com.qnkj.clouds.modules.VmZones.services.IVmZonesService;
import com.qnkj.clouds.services.IKubeStackService;
import com.qnkj.common.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class VmZonesServiceImpl implements IVmZonesService {
    private final IKubeStackService kubeStackService;

    @Override
    public void saveAfter(Content obj) throws Exception {
        String yaml = obj.my.get("yaml").toString();
        if (Utils.isNotEmpty(yaml)) {
            obj.my.put("state","Connecting");
            obj.save("vm_zones");
            String application = XN_Rest.getApplication();
            kubeStackService.cleanKubeStackClientPools(obj.id);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        XN_Rest.setApplication(application);
                        if (kubeStackService.connectKubeStackClient(obj.id,yaml)) {
                            obj.my.put("state","Ready");
                            obj.save("vm_zones");
                        } else {
                            obj.my.put("state","NotReady");
                            obj.save("vm_zones");
                        }
                    }catch (Exception ignored) {}
                }
            }).start();
        }
    }

    @Override
    public void deleteAfter(List<String> ids) throws Exception {
        for(String zone : ids) {
            kubeStackService.cleanKubeStackClientPools(zone);
        }
    }

    @Override
    public Boolean getEditViewIsReadOnly(Object entity) {
        VmZones vmZones = (VmZones)entity;
        try {
            List<Object> vm_clusters = XN_Query.create("Content").tag("vm_clusters")
                    .filter("type", "eic", "vm_clusters")
                    .filter("my.deleted", "=", '0')
                    .filter("my.zone", "=", vmZones.id)
                    .end(1).execute();
            if (vm_clusters.size() > 0){
                return true;
            }
            List<Object> vm_physicalmachines = XN_Query.create("Content").tag("vm_physicalmachines")
                    .filter("type", "eic", "vm_physicalmachines")
                    .filter("my.deleted", "=", '0')
                    .filter("my.zone", "=", vmZones.id)
                    .end(1).execute();
            if (vm_physicalmachines.size() > 0){
                return true;
            }
        }catch (Exception ignored) {}
        return false;
    }
}
