package com.qnkj.core.utils;

import com.qnkj.common.utils.Utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;

@Component
public class FreeMakerUtils {
    @Autowired
    private freemarker.template.Configuration configuration;
    public static FreeMakerUtils freemakerutils;

    @PostConstruct
    public void init() {
        freemakerutils = this;
        freemakerutils.configuration = this.configuration;
    }
    /**
     * 加载模板文件
     *
     * @param filename 模板文件名称
     * @return
     */
    public static String loadTemplateFile(String filename) throws IOException {
        if (!filename.endsWith(".html")) {
            filename += ".html";
        }
        if (filename.startsWith("/")) {
            filename = filename.substring(1);
        }
        /*if (!filename.startsWith("templates/")) {
            filename = "templates/" + filename;
        }*/
        Resource resource = new ClassPathResource(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
        reader.close();
        return result.toString();
    }

    /**
     * 解析模板
     *
     * @param html
     * @throws IOException
     * @throws TemplateException
     */
    public static String make(String html, Map<String, Object> models, Configuration freemarkerConfig) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Template template = new Template("template", html, (!Utils.isEmpty(freemarkerConfig)) ? freemarkerConfig : freemakerutils.configuration);
        template.process(models, stringWriter);
        return stringWriter.toString();
    }

    /**
     * 返回标准的注册企业界面
     *
     * @param models 模板数据
     * @return String
     * @throws IOException
     * @throws TemplateException
     */
    public static String register(Map<String, Object> models) throws IOException, TemplateException {
        return FreeMakerUtils.register(models, null);
    }

    public static String register(Map<String, Object> models, Configuration freemarkerConfig) throws IOException, TemplateException {
        return make(loadTemplateFile("register"), models, freemarkerConfig);
    }

    /**
     * 返回标准的登录界面
     *
     * @param models 模板数据
     * @return String
     * @throws IOException
     * @throws TemplateException
     */
    public static String login(Map<String, Object> models) throws IOException, TemplateException {
        return FreeMakerUtils.login(models, null);
    }

    public static String login(Map<String, Object> models, Configuration freemarkerConfig) throws IOException, TemplateException {
        return make(loadTemplateFile("login"), models, freemarkerConfig);
    }

    /**
     * 返回标准的密码找回界面
     *
     * @param models 模板数据
     * @return String
     * @throws IOException
     * @throws TemplateException
     */
    public static String forgetPassword(Map<String, Object> models) throws IOException, TemplateException {
        return FreeMakerUtils.forgetPassword(models, null);
    }

    public static String forgetPassword(Map<String, Object> models, Configuration freemarkerConfig) throws IOException, TemplateException {
        return make(loadTemplateFile("forgetPassword"), models, freemarkerConfig);
    }

    /**
     * 返回标准的错误界面
     *
     * @param err 错误类型
     * @return String
     */
    public static String error(int err, Map<String, Object> model) {
        return FreeMakerUtils.error(err, model, null);
    }

    public static String error(int err, Map<String, Object> model, Configuration freemarkerConfig) {
        switch (err) {
            case 403:
                try {
                    return make(loadTemplateFile("403"), model, freemarkerConfig);
                } catch (Exception e) {
                    return "";
                }
            case 404:
                try {
                    return make(loadTemplateFile("404"), model, freemarkerConfig);
                } catch (Exception e) {
                    return "";
                }
            default:
                try {
                    return make(loadTemplateFile("500"), model, freemarkerConfig);
                } catch (Exception e) {
                    return "";
                }
        }
    }

    /**
     * 返回标准的列表界面
     *
     * @param models 模板数据
     * @return String
     * @throws IOException
     * @throws TemplateException
     */
    public static String listView(Map<String, Object> models) throws IOException, TemplateException {
        return FreeMakerUtils.listView(models, null);
    }

    public static String listView(Map<String, Object> models, Configuration freemarkerConfig) throws IOException, TemplateException {
        return null;
    }

    /**
     * 返回标准的编辑界面
     *
     * @param models 模板数据
     * @return String
     * @throws IOException
     * @throws TemplateException
     */
    public static String editView(Map<String, Object> models) throws IOException, TemplateException {
        return FreeMakerUtils.editView(models, null);
    }

    public static String editView(Map<String, Object> models, Configuration freemarkerConfig) throws IOException, TemplateException {
        return null;
    }

    /**
     * 返回标准的树型弹窗界面
     *
     * @param models 模板数据
     * @return String
     * @throws IOException
     * @throws TemplateException
     */
    public static String popupTreeView(Map<String, Object> models) throws IOException, TemplateException {
        return FreeMakerUtils.popupTreeView(models, null);
    }

    public static String popupTreeView(Map<String, Object> models, Configuration freemarkerConfig) throws IOException, TemplateException {
        return null;
    }

    /**
     * 返回标准的弹窗界面
     *
     * @param models 模板数据
     * @return String
     * @throws IOException
     * @throws TemplateException
     */
    public static String popupView(Map<String, Object> models) throws IOException, TemplateException {
        return FreeMakerUtils.popupView(models, null);
    }

    public static String popupView(Map<String, Object> models, Configuration freemarkerConfig) throws IOException, TemplateException {
        return null;
    }
}
