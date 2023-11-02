package com.qnkj.common.utils;


import cn.hutool.core.io.FileUtil;
import com.github.restapi.models.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 工具类
 *
 * @author Oldhand
 *
 */

@Slf4j
@Configuration
@Component
public class ContextUtils {

    @Autowired
    private ApplicationContext context;

    private static ContextUtils utils;
    private static final Pattern IS_PROJECT_PATTERN = Pattern.compile("\\.\\w+\\.(\\w+)\\.\\w+\\$\\$");
    private static final Pattern IS_PACKAGE_PATTERN = Pattern.compile("^(.*)\\.\\w+\\$\\$");

    @PostConstruct
    public void init() {
        utils = this;
        utils.context = this.context;
    }
    // 获取当前环境
    public static String getActiveProfile() {
        return utils.context.getEnvironment().getActiveProfiles()[0];
    }

    public static String findProjectNameByBootClass() throws Exception {
        Map<String, Object> annotatedBeans = utils.context.getBeansWithAnnotation(SpringBootApplication.class);
        if (annotatedBeans.isEmpty()) {
            throw new WebException("没有找到启动类！");
        }
        String apprun =  annotatedBeans.values().toArray()[0].getClass().getName();
        Matcher matcher = IS_PROJECT_PATTERN.matcher(apprun);
        if(matcher.find()){
           return matcher.group(1);
        }
        throw new WebException("没有找到启动工程名称！");
    }

    public static String findPackageNameByBootClass() throws Exception {
        Map<String, Object> annotatedBeans = utils.context.getBeansWithAnnotation(SpringBootApplication.class);
        if (annotatedBeans.isEmpty()) {
            throw new WebException("没有找到启动类！");
        }
        String apprun =  annotatedBeans.values().toArray()[0].getClass().getName();
        Matcher matcher = IS_PACKAGE_PATTERN.matcher(apprun);
        if(matcher.find()){
            return matcher.group(1);
        }
        throw new WebException("没有找到包名称！");
    }

    public static Boolean isJar() {
        String protocol = Objects.requireNonNull(ContextUtils.class.getClassLoader().getResource("")).getProtocol();
        return "jar".equals(protocol);
    }

    /**
     * 获取资源文件的路径
     * @param filename 资源文件的相对名称
     * @return 路径
     */
    public static String getLocalFilepath(String filename){
        return getLocalFilepath(filename,false);
    }
    public static String getLocalFilepath(String filename,boolean isSrc){
        if(filename.startsWith(File.separator)){
            filename = filename.substring(1);
        }
        if(isSrc) {
            String projectPath = System.getProperty("user.dir");
            String projectName = "web";
            try{
                projectName = findProjectNameByBootClass();
            }catch (Exception e) {
                log.error(e.getMessage());
            }
            if (FileUtil.exist(projectPath + File.separator + projectName)) {
                projectPath += File.separator + projectName;
            }
            return projectPath + File.separator + "src" + File.separator + "main" + File.separator + filename;
        } else {
            String filePath = Objects.requireNonNull(ContextUtils.class.getClassLoader().getResource("")).getPath();
            try {
                filePath = URLDecoder.decode(filePath, "utf-8");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            filePath += filename;
            return filePath;
        }
    }
    public static String getLocalFilepath(String[] paths) {
        return getLocalFilepath(paths,false);
    }
    public static String getLocalFilepath(String[] paths, boolean isSrc) {
        return getLocalFilepath(String.join(File.separator,paths),isSrc);
    }
}
