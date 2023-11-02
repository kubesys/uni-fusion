package com.qnkj.core.base.modules.lcdp.pagedesign.Util;

import cn.hutool.core.io.IORuntimeException;
import com.qnkj.common.configs.BasePageDesignConfig;
import com.qnkj.common.entitys.PageDesignInitInfo;
import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.FileUtils;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class PagedesignConfigGenerator {
    public static void generate(List<PageDesignInitInfo> list, String author) throws Exception{
        String packagename = ContextUtils.findPackageNameByBootClass();
        StringBuilder classBuilder=new StringBuilder();
        classBuilder.append("package ").append(packagename).append(".configs;\n\n")
                .append("import java.util.Arrays;\n")
                .append("import com.qnkj.common.configs.BasePageDesignConfig;\n")
                .append("import com.qnkj.common.entitys.PageDesignInitInfo;\n")
                .append("import org.springframework.stereotype.Component;\n")
                .append("import javax.annotation.PostConstruct;\n\n")
                .append("/**\n* @author ").append(author).append("\n* @date ").append(LocalDate.now().toString()).append("\n*/\n")
                .append("@Component(\"PageDesignDataConfig\")\n")
                .append("public class PageDesignDataConfig {\n")
                .append("\t@PostConstruct\n")
                .append("\tpublic void init() {\n");
        if(!list.isEmpty()){
            classBuilder.append("\t\t/*页面设计配置数据*/\n");
            classBuilder.append("\t\tBasePageDesignConfig.pageDesignList = Arrays.asList(\n");
            for(int i=0;i<list.size();i++) {
                if (i == list.size()-1) {
                    classBuilder.append("\t\t\t").append(list.get(i).toInitString()).append("\n");
                } else {
                    classBuilder.append("\t\t\t").append(list.get(i).toInitString()).append(",\n");
                }
            }
            classBuilder.append("\t\t\t);\n").append("\t}\n").append("}");
            String filePath = getAdminFilePath( packagename + ".configs", "PageDesignDataConfig.java");
            File file = new File(filePath);
            try {
                FileUtils.touch(file);
                FileUtils.writeUtf8String(classBuilder.toString(), file);
            } catch (IORuntimeException e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    private static String getAdminFilePath(String packageName, String fileName) {
        String packagePath = ContextUtils.getLocalFilepath("java" + File.separator, true);
        packagePath += packageName.replace(".", File.separator) + File.separator;
        return packagePath + fileName;
    }

    public static BasePageDesignConfig getBasePageDesignConfig() {
        try{
            String packagename = ContextUtils.findPackageNameByBootClass();
            Class<?> clazz=Class.forName(packagename + ".configs.PageDesignDataConfig");
            return (BasePageDesignConfig) clazz.getConstructor().newInstance();
        }catch (Exception e){
            return null;
        }
    }
}
