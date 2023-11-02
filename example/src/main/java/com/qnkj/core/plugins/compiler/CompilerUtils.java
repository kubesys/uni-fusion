package com.qnkj.core.plugins.compiler;


import com.qnkj.common.utils.ContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author oldhand
 */
@Slf4j
public class CompilerUtils {
    private CompilerUtils() {}
    private static Map<String, Long> compilerCache = new HashMap<>();
    private static final Pattern IS_COMPILER_PATTERN = Pattern.compile("/modules/(\\w+)/(\\w+)/(services|configs|controller|entitys)/.*\\.java$");
    public static void init() throws Exception {
        try {
            if (Boolean.TRUE.equals(ContextUtils.isJar())) {
                return;
            }
            String compilerPath = getCompilerPath();
            List<String> javaFiles = getCompilerJavaFileLists(compilerPath);
            for (String file : javaFiles) {
                File f = new File(file);
                compilerCache.put(file, f.lastModified());
            }
        }catch (Exception e) {
            log.error("CompilerUtils.ini : {}",e.getMessage());
        }
    }
    private static List<String> compilerModules(String compilerPath,String basePath,String targetPath,List<HashMap<String,String>> modules) throws Exception {
        List<String> compiledFiles = new ArrayList<>();
        for (HashMap<String,String> item : modules) {
            String group = item.get("group");
            String module = item.get("module");
            log.info("编译模块 : {}",module);
            String filepath = compilerPath + "modules/" + group + "/" + module + "/entitys/" + module + ".java";
            if (Boolean.TRUE.equals(compile(compilerPath,basePath,targetPath,filepath))) {
                compiledFiles.add(filepath);
            }
        }
        for (HashMap<String,String> item : modules) {
            String group = item.get("group");
            String module = item.get("module");
            String filepath = compilerPath + "modules/" + group + "/" + module + "/services/I" + module + "Service.java";
            if (Boolean.TRUE.equals(compile(compilerPath,basePath,targetPath,filepath))) {
                compiledFiles.add(filepath);
            }
        }
        for (HashMap<String,String> item : modules) {
            String group = item.get("group");
            String module = item.get("module");
            String filepath = compilerPath + "modules/" + group + "/" + module + "/services/impl/" + module + "ServiceImpl.java";
            if (Boolean.TRUE.equals(compile(compilerPath,basePath,targetPath,filepath))) {
                compiledFiles.add(filepath);
            }
            filepath = compilerPath + "modules/" + group + "/" + module + "/configs/ConfigActionsConfig.java";
            if (Boolean.TRUE.equals(compile(compilerPath,basePath,targetPath,filepath))) {
                compiledFiles.add(filepath);
            }
            filepath = compilerPath + "modules/" + group + "/" + module + "/configs/ConfigDataConfig.java";
            if (Boolean.TRUE.equals(compile(compilerPath,basePath,targetPath,filepath))) {
                compiledFiles.add(filepath);
            }
        }
        for (HashMap<String,String> item : modules) {
            String group = item.get("group");
            String module = item.get("module");
            String filepath = compilerPath + "modules/" + group + "/" + module + "/controller/BaseModuleController.java";
            if (Boolean.TRUE.equals(compile(compilerPath,basePath,targetPath,filepath))) {
                compiledFiles.add(filepath);
            }
            filepath = compilerPath + "modules/" + group + "/" + module + "/controller/" + module + "Controller.java";
            if (Boolean.TRUE.equals(compile(compilerPath,basePath,targetPath,filepath))) {
                compiledFiles.add(filepath);
            }
        }
        return compiledFiles;
    }

    private static Boolean compile(String compilerPath,String basePath,String targetPath,String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            compilerCache.put(filePath, file.lastModified());
            String classname = filePath.replace(basePath, "").
                    replace(".java", "").
                    replace(File.separator, ".");
            log.info("类名: {} 文件 : {} ", classname, filePath);
            String classfile = filePath.replace(File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator, File.separator + "target" + File.separator + "classes" + File.separator)
                    .replace(".java", ".class");
            if (Boolean.TRUE.equals(DynamicCompile.compile(filePath, targetPath))) {
                LoadClassUtils.flushClass(classfile, classname);
                return true;
            }
        }
        return false;
    }


    private static List<HashMap<String,String>> getCompilerModules(List<String> javaFiles) throws Exception {
        HashMap<String,HashMap<String,String>> modules = new HashMap<>(1);
        for (String file : javaFiles) {
            File f = new File(file);
            if (!compilerCache.containsKey(file) || compilerCache.get(file) != f.lastModified()) {
                Matcher matcher = IS_COMPILER_PATTERN.matcher(file);
                if(matcher.find()){
                    String group = matcher.group(1);
                    String module = matcher.group(2);
                    String key = group+module;
                    modules.computeIfAbsent(key,v -> {
                        HashMap<String,String> info = new HashMap<>(1);
                        info.put("group",group);
                        info.put("module",module);
                        return info;
                    });
                }
            }
        }
        return new ArrayList<>(modules.values());
    }
    public static List<String> autoCompiler() throws Exception {
        try {
            if (Boolean.TRUE.equals(ContextUtils.isJar())) {
                throw new Exception("仅开发模式才需要动态编译！");
            }
            String compilerPath = getCompilerPath();
            String basePath = getBasePath();
            String targetPath = getTargetPath();
            List<String> javaFiles = getCompilerJavaFileLists(compilerPath);
            List<HashMap<String,String>> modules = getCompilerModules(javaFiles);
            List<String> compiledFiles = compilerModules(compilerPath,basePath,targetPath,modules);
            boolean hasCompile = false;
            List<String> compileds = new ArrayList<>(compiledFiles);
            List<String> devJavaFiles = new ArrayList<>();
            List<String> againDevJavaFiles = new ArrayList<>();
            List<String> lastDevJavaFiles = new ArrayList<>();
            for (String file : javaFiles) {
                if (!compiledFiles.contains(file)) {
                    File f = new File(file);
                    if (!compilerCache.containsKey(file) || compilerCache.get(file) != f.lastModified()) {
                        log.info("发现Java文件[{}]已经修改，动态编译...", file);
                        hasCompile = true;
                        compilerCache.put(file, f.lastModified());
                        String classname = file.replace(basePath, "").
                                replace(".java", "").
                                replace(File.separator, ".");
                        log.info("类名: {} 文件 : {} ", classname, file);
                        String classfile = file.replace(File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator, File.separator + "target" + File.separator + "classes" + File.separator)
                                .replace(".java", ".class");
                        if (Boolean.TRUE.equals(DynamicCompile.compile(file, targetPath))) {
                            compileds.add(file);
                            LoadClassUtils.flushClass(classfile, classname);
                        } else {
                            devJavaFiles.add(file);
                        }
                    }
                }
            }
            log.info("devJavaFiles: {} ", devJavaFiles);
            if (!devJavaFiles.isEmpty()) {
                Thread.sleep(500);
                for (String file : devJavaFiles) {
                    log.info("因为依赖关系,第二次动态编译Java文件[{}]...", file);
                    String classname = file.replace(basePath, "").
                            replace(".java", "").
                            replace(File.separator, ".");
                    log.info("类名: {} 文件 : {} ", classname, file);
                    String classfile = file.replace(File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator, File.separator + "target" + File.separator + "classes" + File.separator)
                            .replace(".java", ".class");
                    if (Boolean.TRUE.equals(DynamicCompile.compile(file, targetPath))) {
                        compileds.add(file);
                        LoadClassUtils.flushClass(classfile, classname);
                    } else {
                        againDevJavaFiles.add(file);
                    }
                }
            }
            if (!againDevJavaFiles.isEmpty()) {
                Thread.sleep(500);
                for (String file : againDevJavaFiles) {
                    log.info("因为依赖关系,第三次动态编译Java文件[{}]...", file);
                    String classname = file.replace(basePath, "").
                            replace(".java", "").
                            replace(File.separator, ".");
                    log.info("类名: {} 文件 : {} ", classname, file);
                    String classfile = file.replace(File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator, File.separator + "target" + File.separator + "classes" + File.separator)
                            .replace(".java", ".class");

                    if (Boolean.TRUE.equals(DynamicCompile.compile(file, targetPath))) {
                        compileds.add(file);
                        LoadClassUtils.flushClass(classfile, classname);
                    } else {
                        lastDevJavaFiles.add(file);
                    }
                }
            }
            if (!lastDevJavaFiles.isEmpty()) {
                Thread.sleep(500);
                for (String file : lastDevJavaFiles) {
                    log.info("因为依赖关系,最后一次动态编译Java文件[{}]...", file);
                    String classname = file.replace(basePath, "").
                            replace(".java", "").
                            replace(File.separator, ".");
                    log.info("类名: {} 文件 : {} ", classname, file);
                    String classfile = file.replace(File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator, File.separator + "target" + File.separator + "classes" + File.separator)
                            .replace(".java", ".class");

                    if (Boolean.TRUE.equals(DynamicCompile.compile(file, targetPath))) {
                        compileds.add(file);
                        LoadClassUtils.flushClass(classfile, classname);
                    } else {
                        log.error("编译失败 : {} ", classname);
                    }
                }
            }
            if (!hasCompile) {
                throw new Exception("没有发现需要编译的文件");
            }
            return compileds;
        } catch (Exception e) {
            log.error("autoCompiler : {}",e.getMessage());
            throw e;
        }
    }



    private static String getBasePath() throws Exception {
        return ContextUtils.getLocalFilepath("java" + File.separator,true);
    }
    private static String getTargetPath() throws Exception {
        return ContextUtils.getLocalFilepath("");
    }

    private static String getCompilerPath() throws Exception {
        String packagename = ContextUtils.findPackageNameByBootClass();
        String packagePath = ContextUtils.getLocalFilepath("java" + File.separator,true);
        packagePath += packagename.replace(".", File.separator)  + File.separator;
        return packagePath;
    }

    private static void searchFolder(String path,List<String> lists) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        searchFolder(file2.getAbsolutePath(),lists);
                    } else {
                        String fileName = file2.getName();
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        if ("java".equals(suffix)) {
                            lists.add(file2.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private static List<String> getCompilerJavaFileLists(String path) throws Exception {
        List<String> result = new ArrayList<>();
        searchFolder(path,result);
        return result;
    }
}
