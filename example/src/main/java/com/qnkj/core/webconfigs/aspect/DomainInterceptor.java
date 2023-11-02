package com.qnkj.core.webconfigs.aspect;

import com.github.restapi.XN_Rest;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.configure.FreemarkerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author oldhand
 * @date 2019-12-16
*/

@Component
@Slf4j
public class DomainInterceptor implements HandlerInterceptor {

    @Autowired
    public freemarker.template.Configuration configuration;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String host = request.getHeader("Host");
        System.out.println("Interceptor："+request.getRequestURI());
        if (host != null && BaseSaasConfig.existDomain(host)) {
            FreemarkerConfig.updateConfig();
            XN_Rest.setViewer(ProfileUtils.getCurrentProfileId());
            return true;
        }
        return false;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

}
