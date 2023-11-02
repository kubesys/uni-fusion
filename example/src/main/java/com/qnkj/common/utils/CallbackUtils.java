package com.qnkj.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.util.Collections;
import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/22
 */
@Slf4j
public class CallbackUtils {

    private CallbackUtils() {}

    private static Object getObjectClass(String funName, List<Class<T>> initClass, Object... parameter) throws Exception {
        try {
            for (Class<T> clazz : initClass) {
                if (ClassUtils.getDeclaredMethod(clazz,funName,parameter) != null) {
                    Object module = SpringServiceUtil.getBean(clazz);
                    return ClassUtils.exeMethod(module,funName,parameter);
                }
            }
            if (!initClass.isEmpty()) {
                Object module = SpringServiceUtil.getBean(initClass.get(0));
                return ClassUtils.exeMethod(module,funName,parameter);
            }
        } catch (Exception e) {
            log.error("getObjectClass : {}", e.getMessage());
            throw e;
        }
        return null;
    }

    /**
     * 回调函数处理
     *
     * @param funName   回调的函数名
     * @return 返回Object对像
     * @throws Exception 异常
     */

    public static Object invoke(String funName) throws Exception {
        List<Class<T>> initClass = FindClassUtils.getSonClass(null, "IPublicCallbackService");
        if(Utils.isEmpty(initClass)) {
            return null;
        }
        return getObjectClass(funName, initClass);
    }
    /**
     * 回调函数处理
     *
     * @param funName   回调的函数名
     * @param parameter 回调函数的参数
     * @return 返回Object对像
     * @throws Exception 异常
     */
    public static Object invoke(String funName, Object... parameter) throws Exception {
        List<Class<T>> initClass = FindClassUtils.getSonClass(null, "IPublicCallbackService");
        if(Utils.isEmpty(initClass)) {
            return null;
        }
        return getObjectClass(funName, initClass, parameter);
    }

    public static Object invoke(String funName, String serviceName, Object... parameter) throws Exception {
        List<Class<T>> initClass = FindClassUtils.getSonClass(null, serviceName);
        if(Utils.isEmpty(initClass)) {
            return null;
        }
        return getObjectClass(funName, initClass, parameter);
    }

    public static Object invoke(String funName, String packageName, String serviceName, Object... parameter) throws Exception {
        List<Class<T>> initClass = FindClassUtils.getSonClass(packageName, serviceName);
        if(Utils.isEmpty(initClass)) {
            return null;
        }
        return getObjectClass(funName, initClass, parameter);
    }

    public static Object invoke(String funName, Class<?> serviceClass, Object... parameter) throws Exception {
        List<Class<T>> initClass = FindClassUtils.getSonClass(null, serviceClass);
        if(Utils.isEmpty(initClass)) {
            return null;
        }
        return getObjectClass(funName, initClass, parameter);
    }

    public static Object invoke(String funName, String packageName, Class<?> serviceClass, Object... parameter) throws Exception {
        List<Class<T>> initClass = FindClassUtils.getSonClass(packageName, serviceClass);
        if(Utils.isEmpty(initClass)) {
            return null;
        }
        return getObjectClass(funName, initClass, parameter);
    }

    public static Object invokeByModule(String modulename, String funName, Class<?> serviceClass, Object... parameter) throws Exception {
        Class<T> serviceClazz = getService(modulename, serviceClass);
        if(Utils.isEmpty(serviceClazz)) {
            return null;
        }
        return getObjectClass(funName, Collections.singletonList(serviceClazz), parameter);
    }

    public static Class<T> getService(String modulename, Object baseServiceClass) {
        List<Class<T>> initClass = FindClassUtils.getSonClass(null, baseServiceClass);
        if(Utils.isEmpty(initClass)) {
            return null;
        }
        for(Class<T> clazz: initClass){
            String clazzname = clazz.getName();
            if(clazzname.contains("."+modulename+".")){
                return clazz;
            }
        }
        return null;
    }
}
