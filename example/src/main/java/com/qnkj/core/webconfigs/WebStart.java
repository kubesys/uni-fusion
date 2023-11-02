package com.qnkj.core.webconfigs;

import com.qnkj.common.utils.*;
import com.qnkj.core.base.services.IBaseService;
import com.qnkj.core.plugins.compiler.CompilerUtils;
import com.qnkj.core.webconfigs.configure.FreemarkerConfig;
import com.qnkj.core.webconfigs.configure.SpringContextUtil;
import com.qnkj.core.webconfigs.configure.WebConstant;
import com.qnkj.core.webconfigs.properties.WebProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

/**
 * @author clubs
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(value = Integer.MIN_VALUE + 100)
public class WebStart implements ApplicationRunner {
    private final ConfigurableApplicationContext context;
    private final WebProperties webProperties;
    private String restUrl;
    private int restPort;

    private String applicationName;

    @Value("${spring.application.name}")
    private void setApplicationName(String value) {
        applicationName = value;
    }

    @Value("${server.port:8080}")
    private String port;
    @Value("${server.servlet.context-path:}")
    private String contextPath;
    @Value("${spring.profiles.active}")
    private String active;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${rest.baseUrl}")
    public void setBaseUrl(String value) {
        String base = value;
        if (base.toLowerCase().startsWith("http://")) {
            base = value.substring(7);
        }

        if (base.contains(":")) {
            restPort = Integer.parseInt(base.substring(base.indexOf(":") + 1), 10);
            restUrl = base.substring(0, base.indexOf(":"));
        } else {
            restUrl = base;
            restPort = 8000;
        }
    }

    @Value("${spring.minio.url}")
    public String minioUrl;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Utils.startComplete();


        if (Boolean.FALSE.equals(isRedisStart(redisHost, redisPort, redisPassword))) {
            log.error(ColorUtils.red() + " ____   __    _   _ " + ColorUtils.white());
            log.error(ColorUtils.red() + "| |_   / /\\  | | | |" + ColorUtils.white());
            log.error(ColorUtils.red() + "|_|   /_/--\\ |_| |_|__" + ColorUtils.white());
            log.error(ColorUtils.red() + applicationName + "  启动失败" + ColorUtils.white());
            log.error(ColorUtils.red() + "Redis连接异常，请检查Redis连接配置并确保Redis服务已启动" + ColorUtils.white());
            try {
                context.close();
                context.stop();
            }catch (Exception ignored) {

            }
            return;
        } else {
            log.info(ColorUtils.green() + "检测Redis服务成功!!!" + ColorUtils.white());
        }

        if (Boolean.FALSE.equals(isHostConnectable(restUrl, restPort))) {
            log.error(ColorUtils.red() + " ____   __    _   _ " + ColorUtils.white());
            log.error(ColorUtils.red() + "| |_   / /\\  | | | |" + ColorUtils.white());
            log.error(ColorUtils.red() + "|_|   /_/--\\ |_| |_|__" + ColorUtils.white());
            log.error(ColorUtils.red() + applicationName + "  启动失败" + ColorUtils.white());
            log.error(ColorUtils.red() + "MyBase底座服务连接异常，请检查连接配置并确保MyBase底座服务已启动" + ColorUtils.white());
            try {
                context.close();
                context.stop();
            }catch (Exception ignored) {

            }
            return;
        } else {
            log.info(ColorUtils.green() + "检测MyBase底座服务成功!!!" + ColorUtils.white());
        }

        log.info(ColorUtils.blue() + "正在搜索MinIO服务......" + ColorUtils.white());
        if (Boolean.FALSE.equals(Utils.isUrlValid(minioUrl))) {
            log.error(ColorUtils.red() + " ____   __    _   _ " + ColorUtils.white());
            log.error(ColorUtils.red() + "| |_   / /\\  | | | |" + ColorUtils.white());
            log.error(ColorUtils.red() + "|_|   /_/--\\ |_| |_|__" + ColorUtils.white());
            log.error(ColorUtils.red() + "MinIO服务器地址：{}" + ColorUtils.white(),minioUrl);
            log.error(ColorUtils.red() + "MinIO服务连接异常，请检查MinIO服务连接配置并确保MinIO服务已启动" + ColorUtils.white());
        } else {
            log.info(ColorUtils.green() + "检测MinIO服务成功!!!" + ColorUtils.white());
        }

        if (context.isActive()) {
            InetAddress address = InetAddress.getLocalHost();
            String url = String.format("http://%s:%s", address.getHostAddress(), port);
            if (StringUtils.isNotBlank(contextPath)) {
                url += contextPath;
            }


            log.info(ColorUtils.blue() + "搜索配置信息......" + ColorUtils.white());
            long start = DateTimeUtils.gettimeStamp();
            CompilerUtils.init();
            List<Class<T>> initClass = FindClassUtils.getSonClass(null, IBaseService.class);
            log.info(ColorUtils.green() + "["+initClass.size() + "个模块] [耗时："+(DateTimeUtils.gettimeStamp()-start)+"秒] [OK]!!!" + ColorUtils.white());
            log.info(ColorUtils.blue() + "=================================Start Complete=========================================" + ColorUtils.white());
            log.info(ColorUtils.green() + applicationName + "管理端启动完毕，地址 ===> " + ColorUtils.white() + url );
            log.info(ColorUtils.blue() + " __    ___   _      ___   _     ____ _____  ____ " + ColorUtils.white());
            log.info(ColorUtils.blue() + "/ /`  / / \\ | |\\/| | |_) | |   | |_   | |  | |_  " + ColorUtils.white());
            log.info(ColorUtils.blue() + "\\_\\_, \\_\\_/ |_|  | |_|   |_|__ |_|__  |_|  |_|__ " + ColorUtils.white());
            log.info(ColorUtils.blue() + "========================================================================================" + ColorUtils.white());
            RedisUtils.set("JAVA_VM_START_COMPLETE",true, 2L);

            boolean auto = webProperties.isAutoOpenBrowser();
            if (auto && SpringContextUtil.getActiveProfile().compareTo("dev") == 0) {
                Object isAutoOpenBrowser = RedisUtils.get(WebConstant.AUTOOPENBROWSER_PREFIX);
                if (isAutoOpenBrowser == null) {
                    String os = System.getProperty("os.name");
                    // 默认为 windows时才自动打开页面
                    if (StringUtils.containsIgnoreCase(os, WebConstant.SYSTEM_WINDOWS)) {
                        //使用默认浏览器打开系统登录页
                        Runtime.getRuntime().exec("cmd  /c  start " + url);
                    } else if (StringUtils.containsIgnoreCase(os, WebConstant.SYSTEM_MACOS)) {
                        Runtime.getRuntime().exec("open " + url);
                    }
                    RedisUtils.set(WebConstant.AUTOOPENBROWSER_PREFIX, "ok", 14400L);
                }

            }
        }
    }

    /**
     * 采用远程访问方式，检测Redis是否启动
     */
    private Boolean isRedisStart(String address, int port, String password) {
        boolean result = false;
        try(Jedis jedis = new Jedis(address, port)) {
            if (password != null && !password.isEmpty()) {
                //密码
                jedis.auth(password);
            }
            String ping = jedis.ping();
            if ("PONG".equalsIgnoreCase(ping)) {
                result = true;
            }
        }catch (Exception e) {
            log.error("{}",e.getMessage());
        }
        return result;
    }

    private Boolean isHostConnectable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
