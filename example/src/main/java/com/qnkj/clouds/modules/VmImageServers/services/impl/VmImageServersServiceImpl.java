package com.qnkj.clouds.modules.VmImageServers.services.impl;

import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.clouds.modules.VmImageServers.entitys.VmImageServers;
import com.qnkj.clouds.modules.VmImageServers.services.IVmImageServersService;
import com.qnkj.clouds.services.IKubeStackService;
import com.qnkj.common.entitys.CustomDataSearch;
import com.qnkj.common.entitys.SelectOption;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.ftpserver.ftp.MyFtpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class VmImageServersServiceImpl implements IVmImageServersService {
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
    public void saveAfter(Content obj) throws Exception {
        String serverip = obj.my.get("serverip").toString();
        String port = obj.my.get("port").toString();
        String username = obj.my.get("username").toString();
        String password = obj.my.get("password").toString();

        if (Utils.isNotEmpty(serverip) &&
                Utils.isNotEmpty(port) &&
                Utils.isNotEmpty(username) &&
                Utils.isNotEmpty(password)) {
            if (MyFtpClient.testConnectServer(serverip,Integer.parseInt(port),username,password)) {
                obj.my.put("state","connected");
                obj.save("vm_imageservers");
            } else {
                obj.my.put("state","notconnected");
                obj.save("vm_imageservers");
            }
        }

    }

    @Override
    public List<Map<String, Object>> getIsoFileLists(String record) {
        List<Map<String, Object>> lists = new ArrayList<>();
        try {
            VmImageServers vmImageServer = (VmImageServers)this.load(record);
            log.info("vmImageServer : {}",vmImageServer);
            if (Utils.isNotEmpty(vmImageServer.serverip) &&
                    Utils.isNotEmpty(vmImageServer.port) &&
                    Utils.isNotEmpty(vmImageServer.username) &&
                    Utils.isNotEmpty(vmImageServer.password)) {
                MyFtpClient client = new MyFtpClient();
                if (client.connectServer(vmImageServer.serverip,Integer.parseInt(vmImageServer.port),vmImageServer.username,vmImageServer.password)) {
                    List<String> files = client.listRemoteFiles("*.iso");
                    return files.stream().map( v -> new HashMap<String, Object>() {{
                        put("file", v);
                    }}).collect(Collectors.toList());
                }
            }
        }catch (Exception ex) {
        }
        return lists;
    }
}
