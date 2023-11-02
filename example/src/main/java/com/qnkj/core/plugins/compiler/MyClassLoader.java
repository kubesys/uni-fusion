package com.qnkj.core.plugins.compiler;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author oldhand
 */
@Slf4j
public class MyClassLoader extends ClassLoader {
    /**
     * 需要加载类的路径
     */
    private String classPath;

    public MyClassLoader() {
    }

    public MyClassLoader(String classPath) {
        super();
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;
        // 获取class文件字节码数组
        byte[] clazzByteArr = getData();
        if (clazzByteArr != null) {
            // 将class的字节码数组转换成class类的实例
            clazz = defineClass(name, clazzByteArr, 0, clazzByteArr.length);
            log.info("defineClass : ok");
        }
        return clazz;
    }

    /**
     * 获取class文件字节数组
     *
     */
    private byte[] getData() {
        File file = new File(this.classPath);
        if (file.exists()) {
            try(FileInputStream in = new FileInputStream(file)){
                try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int size;
                    while ((size = in.read(buffer)) != -1) {
                        out.write(buffer, 0, size);
                    }
                    return out.toByteArray();
                }
            }catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

}
