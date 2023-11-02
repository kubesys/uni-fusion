package com.qnkj.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * create by 徐雁
 * create date 2021/10/29
 * create time 4:58 下午
 */

@Slf4j
public class ClassUtils {
    private ClassUtils() {}

    public static Object create(Class<?> clazz) {
        try {
            Constructor<?> constructor = getConstructor(clazz);
            if(constructor != null){
                return constructor.newInstance();
            } else {
                log.error("couldn't find constructor for {}",clazz.getCanonicalName());
            }
        }catch (Exception e) {
            log.error("couldn't find constructor for {}, Error: {}",clazz.getCanonicalName(),e.getMessage());
        }
        return null;
    }

    public static Object create(Class<?> clazz, Object... parameter) {
        try {
            Constructor<?> constructor = getConstructor(clazz,parameter);
            if(constructor != null){
                return constructor.newInstance(parameter);
            } else {
                log.error("couldn't find constructor for {}",clazz.getCanonicalName());
            }
        }catch (Exception e) {
            log.error("couldn't find constructor for {}, Error: {}",clazz.getCanonicalName(),e.getMessage());
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T>Constructor<T> getConstructor(Class<T> clazz, Object... parameter) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if(parameter.length <= 0){
                if(constructor.getParameterTypes().length <= 0){
                    return constructor;
                }
            } else {
                if (matches(constructor.getParameterTypes(), parameter)) {
                    return constructor;
                }
            }
        }
        return null;
    }

    public static Field getField(Object classObject, String fieldName) {
        Field field = null;
        try{
            field = classObject.getClass().getDeclaredField(fieldName);
        }catch (NoSuchFieldException e) {
            Class<?> superClass = classObject.getClass().getSuperclass();
            if (superClass != null) {
                return getField(superClass,fieldName);
            }
        }
        return field;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = null;
        try{
            field = clazz.getDeclaredField(fieldName);
        }catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getField(superClass,fieldName);
            }
        }
        return field;
    }

    public static void setValue(Object classObject, String fieldName, Object fieldValue) {
        Field field = getField(classObject,fieldName);
        if(field != null) {
            try{
                field.setAccessible(true);
                field.set(classObject, fieldValue);
            }catch (Exception e) {
                log.error("{} field {} setValue error: {}",classObject.getClass().getCanonicalName(),fieldName,e.getMessage());
            }
        } else {
            log.error("{} no search field {}",classObject.getClass().getCanonicalName(),fieldName);
        }
    }

    public static Object getValue(Object classObject, String fieldName) {
        Field field = getField(classObject,fieldName);
        if(field != null) {
            try{
                field.setAccessible(true);
                return field.get(classObject);
            }catch (Exception e) {
                log.error("{} field {} getValue error: {}",classObject.getClass().getCanonicalName(),fieldName,e.getMessage());
            }
        } else {
            log.error("{} no search field {}",classObject.getClass().getCanonicalName(),fieldName);
        }
        return null;
    }

    public static Method getterMethod(Object classObject, String methodName, Object... parameter) {
        if(!methodName.startsWith("get")){
            methodName = "get" + Character.toTitleCase(methodName.charAt(0)) + methodName.substring(1);
        }
        return getMethod(classObject,methodName,parameter);
    }

    public static Method setterMethod(Object classObject, String methodName, Object... parameter) {
        if(!methodName.startsWith("set")){
            methodName = "set" + Character.toTitleCase(methodName.charAt(0)) + methodName.substring(1);
        }
        return getMethod(classObject,methodName,parameter);
    }

    public static Method getMethod(Object classObject, String methodName, Object... parameter) {
        for (Method method : classObject.getClass().getMethods()) {
            if (method.getName().equals(methodName) && matches(method.getParameterTypes(), parameter)) {
                return method;
            }
        }
        Class<?> superClass = classObject.getClass().getSuperclass();
        if (superClass != null) {
            return getMethod(superClass, methodName, parameter);
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Object... parameter) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) && matches(method.getParameterTypes(), parameter)) {
                return method;
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            return getMethod(superClass, methodName, parameter);
        }
        return null;
    }

    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Object... parameter) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && matches(method.getParameterTypes(), parameter)) {
                return method;
            }
        }
        return null;
    }

    private static boolean matches(Class<?>[] parameterTypes, Object[] args) {
        if ((parameterTypes == null) || (parameterTypes.length == 0)) {
            return ((args == null) || (args.length == 0));
        }
        if ((args == null) || (parameterTypes.length != args.length)) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if ((args[i] != null) && (!parameterTypes[i].isAssignableFrom(args[i].getClass()))) {
                return false;
            }
        }
        return true;
    }

    public static Object exeMethod(Object classObject, String methodName, Object... parameter) {
        Method method = getMethod(classObject, methodName, parameter);
        if(method != null){
            try {
                method.setAccessible(true);
                return method.invoke(classObject, parameter);
            }catch (Exception e) {
                log.error("couldn't execution {} method on {}, Error: {}",methodName,classObject.getClass().getCanonicalName(),e.getMessage());
            }
        } else {
            log.error("{} no search method {}",classObject.getClass().getCanonicalName(),methodName);
        }
        return null;
    }

    public static Object exeGetMethod(Object classObject, String methodName, Object... parameter) {
        if(!methodName.startsWith("get")){
            methodName = "get" + Character.toTitleCase(methodName.charAt(0)) + methodName.substring(1);
        }
        return exeMethod(classObject,methodName,parameter);
    }

    public static Object exeSetMethod(Object classObject, String methodName, Object... parameter) {
        if(!methodName.startsWith("set")){
            methodName = "set" + Character.toTitleCase(methodName.charAt(0)) + methodName.substring(1);
        }
        return exeMethod(classObject,methodName,parameter);
    }
}
