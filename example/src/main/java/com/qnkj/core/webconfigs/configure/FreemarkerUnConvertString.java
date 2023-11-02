package com.qnkj.core.webconfigs.configure;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * create by 徐雁
 * create date 2021/3/24
 */

public class FreemarkerUnConvertString implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        if(list.size() != 1){
            return new TemplateModelException("wrong param number, must be one!");
        }
        if(list.get(0) instanceof SimpleScalar) {
            return (list.get(0)).toString().replaceAll("&#38;","&").replaceAll( "&#60;","<").replaceAll("&#62;",">").replaceAll("&#34;","\"").replaceAll("&#39;","'");
        }
        return list.get(0);
    }
}
