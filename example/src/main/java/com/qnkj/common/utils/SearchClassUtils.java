package com.qnkj.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * create by 徐雁
 * create date 2020/11/20
 */

@Slf4j
public class SearchClassUtils {

    private final static List<String> PACKAGES = Arrays.asList("com.qnkj.common",
                                                                "com.qnkj.core",
                                                                "com.qnkj.flow",
                                                                "com.qnkj.generator",
                                                                "com.qnkj.report",
                                                                "com.github.restapi",
                                                                "org.activiti");
    /**
     * @return 系统所有开发类的名称
     */
    public static List<String> getAllClassNames() {
        List<String> classNames = new ArrayList<>();
        for (String item : PACKAGES) {
            classNames.addAll(getPackageClass(item));
        }
        return classNames;
    }
    /**
     * 跟据packageName获取所有Java类
     *
     * @param packageName 包的名称字符串
     * @return 所有类的名称
     */
    @SuppressWarnings({"rawtypes"})
    private static List<String> getPackageClass(String packageName) {
        return FindClassUtils.getPackageClass(packageName).stream().map( v -> v.getName() ).collect(Collectors.toList());
    }
}
