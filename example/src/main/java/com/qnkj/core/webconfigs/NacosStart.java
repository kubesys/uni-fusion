package com.qnkj.core.webconfigs;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.qnkj.common.utils.ColorUtils;
import com.qnkj.common.utils.NetWorkUtils;
import com.qnkj.common.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Order(Integer.MIN_VALUE + 99)
@Component
@Slf4j
@RequiredArgsConstructor
@EnableFeignClients
public class NacosStart implements ApplicationRunner {
    private final ConfigurableApplicationContext context;
    private String nacosServiceAddr;
    private String nacosServiceName;
    private String nacosServiceGroup;
    private String port;

    @Value("${spring.cloud.nacos.discovery.server-addr:localhost:8848}")
    private void setNacosServiceAddr(String value) {
        nacosServiceAddr = value;
    }

    @Value("${spring.application.name}")
    private void setNacosServiceName(String value) {
        nacosServiceName = value;
    }

    @Value("${spring.cloud.nacos.discovery.group:LCDP}")
    private void setNacosServiceGroup(String value) {
        nacosServiceGroup = value;
    }

    @Value("${server.port:8080}")
    private void setPort(String value) {
        port = value;
    }


    private static Boolean apiServiceStarted = false;

    public static Boolean isApiServiceStarted() {
        return apiServiceStarted;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (context.isActive()) {
            try {
                if (Utils.isNotEmpty(nacosServiceAddr)) {
                    log.info(ColorUtils.blue() + "正在搜索Nacos服务......" + ColorUtils.white());
                    if (Boolean.FALSE.equals(NetWorkUtils.isNacosStart(nacosServiceAddr))) {
                        log.error(ColorUtils.red() + " ____   __    _   _ " + ColorUtils.white());
                        log.error(ColorUtils.red() + "| |_   / /\\  | | | |" + ColorUtils.white());
                        log.error(ColorUtils.red() + "|_|   /_/--\\ |_| |_|__" + ColorUtils.white());
                        log.error(ColorUtils.red() + "Nacos服务器地址：{}" + ColorUtils.white(),nacosServiceAddr);
                        log.error(ColorUtils.red() + "Nacos服务连接异常，请检查Nacos服务连接配置并确保Nacos服务已启动" + ColorUtils.white());
                        return;
                    }
                    regestDiscovery(nacosServiceAddr, nacosServiceGroup, nacosServiceName, Integer.parseInt(port));
                    log.info(ColorUtils.green() + "注册Nacos微服务成功!!!" + ColorUtils.white());
                    if (Boolean.FALSE.equals(isApiServiceStart())) {
                        log.error(ColorUtils.red() + " ____   __    _   _ " + ColorUtils.white());
                        log.error(ColorUtils.red() + "| |_   / /\\  | | | |" + ColorUtils.white());
                        log.error(ColorUtils.red() + "|_|   /_/--\\ |_| |_|__" + ColorUtils.white());
                        log.error(ColorUtils.red() + "Api开放接口微服务连接异常，请检查配置并确保微服务已启动" + ColorUtils.white());
                    } else {
                        apiServiceStarted = true;
                        log.info(ColorUtils.green() + "连接Api开放接口微服务连接成功!!!" + ColorUtils.white());
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error(ColorUtils.red() + " ____   __    _   _ " + ColorUtils.white());
                log.error(ColorUtils.red() + "| |_   / /\\  | | | |" + ColorUtils.white());
                log.error(ColorUtils.red() + "|_|   /_/--\\ |_| |_|__" + ColorUtils.white());
                log.error(ColorUtils.red() + "注册Nacos微服务失败" + ColorUtils.white());
                return;
            }
        }
    }

    private void regestDiscovery(String nacosServiceAddr, String nacosServiceGroup, String applicationName, Integer applicationPort) throws NacosException {
        NamingService naming = NamingFactory.createNamingService(nacosServiceAddr);
        Instance instance = new Instance();
        instance.setServiceName(applicationName);
        instance.setIp(NetWorkUtils.getLocalIp());
        instance.setPort(applicationPort);
        Map<String, String> data = new HashMap<>(1);
        instance.setMetadata(data);

        naming.registerInstance(applicationName, nacosServiceGroup, instance);
        naming.subscribe("api", nacosServiceGroup, new EventListener() {
            @Override
            public void onEvent(Event event) {
                log.info("ServiceName: {} ",((NamingEvent)event).getServiceName().toString());
                log.info("Instances: {}",((NamingEvent)event).getInstances().toString());
                if (((NamingEvent)event).getInstances().size() > 0) {
                    apiServiceStarted = true;
                } else {
                    apiServiceStarted = false;
                }
            }
        });
    }

    /**
     * 检测认证微服务是否可连接
     */
    public Boolean isApiServiceStart() {
        try {
            NacosNamingService namingService = new NacosNamingService(nacosServiceAddr);
            List<Instance> instances = namingService.getAllInstances("api",nacosServiceGroup);
            for(Instance instance: instances){
                if(instance.isHealthy() && instance.isEnabled()){
                  return true;
                }
            }
        }catch (Exception e) {
            log.error("<isApiServiceStart> Error: {}",e.getMessage());
        }
        return false;
    }
}
