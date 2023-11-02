package com.qnkj.core.plugins.compiler;

import com.qnkj.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.OverridingClassLoader;

import java.io.File;

@Slf4j
public class LoadClassUtils {

    public static void flushClass(String classPath, String className) {
        try {
            File f = new File(classPath);
            if (f.exists()) {
                loadClass(classPath, className);
                log.info("flushClass classPath : {}",classPath);
                log.info("flushClass className : {}",className);
            } else {
                throw new Exception(classPath + " not found");
            }
        } catch (Exception e) {
            log.error("flushClass classPath : {}",classPath);
        }
    }

    /**
     * 加载class对象
     *
     * @param classPath
     * @param className
     * @return
     * @throws Exception
     */
    public synchronized static void loadClass(String classPath, String className) throws Exception {
        if (StringUtils.isEmpty(classPath)) { return; }
        if (StringUtils.isEmpty(className)) { return; }

        ClassLoader appClassLoader = Thread.currentThread().getContextClassLoader();

        // 添加到 excludedPackages 或 excludedClasses 的类就不会被代理的 ClassLoader 加载
        // 而会使用 JDK 默认的双亲委派机制
        // 因此 TestBean 不会被 OverridingClassLoader 重新加载，而 ITestBean 会重新加载
        OverridingClassLoader overridingClassLoader = new OverridingClassLoader(appClassLoader);
        overridingClassLoader.excludeClass(className);
        Class<?> excludedClazz1 = appClassLoader.loadClass(className);
        Class<?> excludedClazz2 = overridingClassLoader.loadClass(className);

        MyClassLoader myClassLoader = new MyClassLoader();
        myClassLoader.setClassPath(classPath);
        myClassLoader.loadClass(className);
    }

}
