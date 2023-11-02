package com.qnkj.core.webconfigs.configure;

import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
  freemarker 全局配置
 */


@Configuration
@Component
public class FreemarkerConfig {

    @Value("${build.version}")
    private String version;
    @Value("${build.project-version}")
    private String projectVersion;

    @Autowired
    private freemarker.template.Configuration configuration;
    private static FreemarkerConfig freemarkerConfig;

    public static freemarker.template.Configuration getFreemarkerConfig() {
        return freemarkerConfig.configuration;
    }

    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {
        freemarkerConfig = this;
        this.configuration.setSharedVariable("version", version);
        Date date = DateTimeUtils.string2date(version,"yyyyMMddHHmm");
        this.configuration.setSharedVariable("releaseDate", DateTimeUtils.getDatetime(date,"yyyy-MM-dd"));
        this.configuration.setSharedVariable("projectVersion", projectVersion);
        this.configuration.setSharedVariable("ConvertString",new FreemarkerConvertString());
        this.configuration.setSharedVariable("UnConvertString",new FreemarkerUnConvertString());
        this.configuration.setSharedVariable("ConvertHtml",new FreemarkerConvertHtml());
        this.configuration.setDefaultEncoding("UTF-8");
    }



    public static void updateConfig() {
        SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
        try {
            freemarkerConfig.configuration.setSharedVariable("copyright", saasUtils.getCopyright());
            freemarkerConfig.configuration.setSharedVariable("softwareCompany", saasUtils.getCompany());
            freemarkerConfig.configuration.setSharedVariable("softwareName", saasUtils.getPlatformName());
            freemarkerConfig.configuration.setSharedVariable("softwareNickname", saasUtils.getCompanyNickname());
            freemarkerConfig.configuration.setSharedVariable("softwareDesc", saasUtils.getPlatformDesc());
            freemarkerConfig.configuration.setSharedVariable("icpBeian", saasUtils.getICP());
            freemarkerConfig.configuration.setSharedVariable("policeBeian", saasUtils.getPolice());
            freemarkerConfig.configuration.setSharedVariable("isregister", saasUtils.isRegister());
            freemarkerConfig.configuration.setSharedVariable("issingleon", saasUtils.isSingle());
        }catch (Exception ignored) {}
    }
}
