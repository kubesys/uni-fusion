package com.qnkj.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by 徐雁
 * create date 2020/11/20
 */

@Slf4j
public class FindClassUtils implements DisposableBean {
    //region 跟椐父类获取所有子类
    private static String packRoot;
    private static List<Class<?>> packageClassList;
    private static final String CLASS_EXT = ".class";


    @Override
    public void destroy() throws Exception {
        packageClassList.clear();
    }

    private static final Pattern IS_BOOT_CLASS_PATTERN = Pattern.compile("\\.\\w+\\.(\\w+)\\.\\w+\\$\\$");
    public static String findProjectNameByBootClass() throws Exception {
        Map<String, Object> annotatedBeans = SpringServiceUtil.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class);
        if (annotatedBeans.isEmpty()) {
            throw new Exception("没有找到启动类！");
        }
        String apprun = annotatedBeans.values().toArray()[0].getClass().getName();
        String[] packlist = apprun.split("\\.");
        Matcher matcher = IS_BOOT_CLASS_PATTERN.matcher(apprun);
        if (matcher.find() && !Utils.isEmpty(packlist)) {
            return packlist[0];
        }
        throw new Exception("没有找到启动工程名称！");
    }

    /**
     * 根椐packageName名称获取parentClass的所有子类
     *
     * @param parentClass 父类Class或父类Class集合
     * @return 所有子类Class集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> List<Class<T>> getSonClass(String packageName,Object parentClass) {
        if (Utils.isEmpty(packageName)) {
            try {
                if(Utils.isEmpty(packRoot)){
                    packRoot = findProjectNameByBootClass();
                    packageClassList = getPackageClass(packRoot);
                }
                packageName = packRoot;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        List<Class<?>> classList = getPackageNameClassList(packageName);

        return sonClassAnalyzer(classList,parentClass);
    }

    /**
     * 在当前包名下获取fatherClass的所有子类
     *
     * @param fatherClass 父类Class
     * @return 所有子类Class集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> List<Class<T>> getSonClass(Class fatherClass) {
        List<Class<T>> sonClassList = new ArrayList<>();
        if(Utils.isEmpty(packageClassList)){
            try {
                String packRoot = findProjectNameByBootClass();
                packageClassList = getPackageClass(packRoot);
            }catch (Exception e){
                return sonClassList;
            }
        }
        for (Class clazz : packageClassList) {
            if (fatherClass.isAssignableFrom(clazz) && !fatherClass.equals(clazz)) {
                sonClassList.add(clazz);
            }
        }
        return sonClassList;
    }

    private static List<Class<?>> getPackageNameClassList(String packageName) {
        List<Class<?>> classList = packageClassList;
        if(!Utils.isEmpty(packageName) && (Utils.isEmpty(packRoot) || !packageName.equals(packRoot))){
            if(Utils.isEmpty(packageClassList)) {
                classList = getPackageClass(packageName);
            } else {
                classList = new ArrayList<>();
                for(Class<?> clazz: packageClassList){
                    if(clazz.getName().startsWith(packageName)){
                        classList.add(clazz);
                    }
                }
            }
        }
        return classList;
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> List<Class<T>> sonClassAnalyzer(List<Class<?>> classList,Object parentClass) {
        List<Class<T>> sonClassList = new ArrayList<>();
        for (Class<?> clazz : classList) {
            if (parentClass instanceof String) {
                String[] classlist = clazz.getName().split("\\.");
                if(!((String) parentClass).contains(".") && classlist.length > 0) {
                    String classname = classlist[classlist.length - 1];
                    if (classname.equals(parentClass)) {
                        sonClassList.add((Class<T>) clazz);
                    }
                }
            }else{
                isAssignableClass(sonClassList,parentClass,clazz);
            }
        }
        return sonClassList;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> void isAssignableClass(List<Class<T>> sonClassList,Object parentClass, Class<?> subClass) {
        if (parentClass instanceof Class) {
            if(((Class<?>)parentClass).isAssignableFrom(subClass) && !parentClass.equals(subClass) && !Utils.isEmpty(subClass.getAnnotatedSuperclass())){
                sonClassList.add((Class<T>) subClass);
            }
        } else if (parentClass instanceof List) {
            for (Object pClazz : (List<?>) parentClass) {
                isAssignableClass(sonClassList,pClazz,subClass);
            }
        }
    }

    /**
     * 跟据packageName获取所有Java类
     *
     * @param packageName 包的名称字符串
     * @return 所有类的Class
     */
    @SuppressWarnings({"rawtypes"})
    public static List<Class<?>> getPackageClass(String packageName) {
        ClassLoader loader = SpringServiceUtil.getApplicationContext().getClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = null;
        ArrayList<Class<?>> classList = new ArrayList<>();
        try {
            if(loader != null) {
                resources = loader.getResources(path);
            }
            if(loader == null || resources == null) {
                return classList;
            }
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if(!Utils.isEmpty(resource)) {
                    if("file".equals(resource.getProtocol())) {
                        classList.addAll(findClass(new File(resource.getFile()), packageName));
                    }else if("jar".equals(resource.getProtocol())){
                        jarAnalyzer((JarURLConnection) resource.openConnection(),packageName,classList,loader);
                    }
                }
            }
        } catch (IOException e) {
            return classList;
        }
        return classList;
    }

    private static void jarAnalyzer(JarURLConnection jarUrlConnection, String packageName, List<Class<?>>classList, ClassLoader loader) throws IOException {
        Enumeration<JarEntry> jarEntries = getJarEntries(jarUrlConnection);
        if(jarEntries != null) {
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.endsWith(CLASS_EXT) && !jarEntryName.contains("$")) {
                    String className = (jarEntryName.substring(0,jarEntryName.lastIndexOf(CLASS_EXT))).replace("/", ".");
                    if (className.startsWith(packageName) && Boolean.FALSE.equals(isFilterPackage(className))) {
                        addJarClass(classList, className, loader);
                    }
                }
            }
        }
    }

    private static Enumeration<JarEntry> getJarEntries(JarURLConnection jarUrlConnection) throws IOException {
        if (!Utils.isEmpty(jarUrlConnection)) {
            JarFile jarFile = jarUrlConnection.getJarFile();
            if (!Utils.isEmpty(jarFile)) {
                return jarFile.entries();
            }
        }
        return null;
    }

    private static void addJarClass(List<Class<?>>classList, String className, ClassLoader loader) {
        try {
            classList.add(Class.forName(className, false, loader));
        } catch (LinkageError | Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 根据包名获取所在目录中的类
     *
     * @param file        目录或文件对像
     * @param packageName 包的名称字符串
     * @return 所有类的Class
     */
    @SuppressWarnings({"rawtypes"})
    private static List<Class<?>> findClass(File file, String packageName) {
        List<Class<?>> classList = new ArrayList<>();
        if (!file.exists()) {
            return classList;
        }
        File[] fileArray = file.listFiles();
        if(fileArray != null) {
            for (File subFile : fileArray) {
                if (subFile.isDirectory() && !file.getName().contains(".")) {
                    classList.addAll(findClass(subFile, packageName + "." + subFile.getName()));
                } else if (!subFile.isDirectory() && subFile.getName().endsWith(CLASS_EXT) && !subFile.getName().contains("$")) {
                    String entryName = subFile.getName();
                    String classname = packageName + "." + entryName.substring(0,entryName.lastIndexOf(CLASS_EXT));
                    if (Boolean.FALSE.equals(isFilterPackage(classname))) {
                        addJarClass(classList,classname,SpringServiceUtil.getApplicationContext().getClassLoader());
                    }
                }
            }
        }
        return classList;
    }

    private static final List<String> EXCLUDE_PACKAGES = Arrays.asList(".stoyanr.",
            ".jagregory.",
            ".mortennobel.",
            ".wuwenze.poi.",
            ".microsoft.",
            ".alibaba.fastjson.",
            ".wf.captcha.",
            ".thoughtworks.",
            ".graphbuilder.",
            ".jhlabs.",
            ".mchange.",
            ".ulisesbocchio.",
            ".ctc.wstx.",
            ".netflix.",
            ".fasterxml",
            ".sun.",
            ".oracle.",
            ".google.",
            ".googlecode.",
            ".carrotsearch.",
            ".intellij.",
            ".common.utils.IdcardUtil",
            ".tools.rest.",
            "com.aliyuncs.",
            "com.hp.hpl.",
            "com.mxgraph.",
            ".common.utils.FindClassUtils");

    public static Boolean isFilterPackage(String packageName) {
        if(EXCLUDE_PACKAGES.contains(packageName)){
            return true;
        }else{
            for(String item: EXCLUDE_PACKAGES){
                if(packageName.contains(item)){
                    return true;
                }
            }
        }
        return false;
    }
    //endregion
}
