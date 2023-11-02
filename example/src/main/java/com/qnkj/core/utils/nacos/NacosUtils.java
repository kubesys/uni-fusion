package com.qnkj.core.utils.nacos;


import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class NacosUtils {

    public static List<Map<String,Object>> getInstances(String serviceName) {
        List<Map<String,Object>> instances = new ArrayList<>();
        try {
            NamingService naming = NamingFactory.createNamingService(HttpProvider.getNacosServiceAddr());
            List<Instance> allInstances = naming.getAllInstances(serviceName,HttpProvider.getNacosServiceGroup());
            Integer index = 1;
            for(Instance instance : allInstances) {
                Map<String,Object> info = new HashMap<>();
                info.put("id",index);
                info.put("ip",instance.getIp());
                info.put("port",instance.getPort());
                info.put("ephemeral",instance.isEphemeral());
                info.put("weight",instance.getWeight());
                info.put("healthy",instance.isHealthy());
                info.put("metadata",instance.getMetadata().toString());
                info.put("serviceName",instance.getServiceName());
                info.put("clusterName",instance.getClusterName());
                instances.add(info);
                index++;
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return instances;
    }
    public static List<Map<String,Object>> getAllServices() throws Exception {
        List<Map<String,Object>> serviers = new ArrayList<>();
        try {
            serviers = listService("",1,100);
        } catch (NacosException e) {
            e.printStackTrace();
            log.error("getAllServices : {}",e.getMessage());
        }
        return serviers;
    }
    public static Map<String,Object> getServiceByName(String serviceName) throws Exception {
        try {
            List<Map<String,Object>> serviers = listService(serviceName,1,100);
            if (serviers.size() > 0) {
                return serviers.get(0);
            }
        } catch (NacosException e) {
            throw e;
        }
        return null;
    }

    private static List<Map<String,Object>> listService(String serviceName, Integer pageNo, Integer pageSize) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("hasIpCount", false);
        params.put("withInstances", false);
        params.put("serviceNameParam", serviceName);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        String ret = HttpProvider.getInstance().nacosRequest(HttpProvider.Method.GET, "/ns/catalog/services", params);
        JsonObject data = new JsonParser().parse(ret).getAsJsonObject();
        List<Map<String,Object>> results = new ArrayList<>();
        data.get("serviceList").getAsJsonArray().forEach(json -> {
            String name = json.getAsJsonObject().get("name").getAsString();
            String groupName = json.getAsJsonObject().get("groupName").getAsString();
            String clusterCount = json.getAsJsonObject().get("clusterCount").getAsString();
            String ipCount = json.getAsJsonObject().get("ipCount").getAsString();
            String healthyInstanceCount = json.getAsJsonObject().get("healthyInstanceCount").getAsString();
            String triggerFlag = json.getAsJsonObject().get("triggerFlag").getAsString();
            Map<String,Object> info = new HashMap<>();
            info.put("name",name);
            info.put("groupName",groupName);
            info.put("clusterCount",clusterCount);
            info.put("ipCount",ipCount);
            info.put("healthyInstanceCount",healthyInstanceCount);
            info.put("triggerFlag",triggerFlag);
            results.add(info);
        });
        return results;
    }



}
