package com.qnkj.core.webconfigs.configure;


import com.qnkj.common.configs.BaseSaasConfig;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(1)
@WebFilter
@Component
public class WebRequestFilter implements Filter {
    @Autowired
    private RedisSessionDAO redisSession;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String Url = ((HttpServletRequest)request).getRequestURI();
        if(Url.startsWith("/layui/") || Url.startsWith("/images/") || Url.startsWith("/zTree/") ||
                Url.startsWith("/echarts/") || Url.startsWith("/css/") || Url.startsWith("/js/") ||
                Url.startsWith("/iconfont/") || Url.startsWith("/font/") || Url.startsWith("/floweditor/") ||
                Url.startsWith("/tools/") || Url.startsWith("/highcharts/") ||
                Url.startsWith("/bpmn/") ||  Url.startsWith("/wiki/") ||  Url.startsWith("/novnc/") ||
                Url.startsWith("/base/402") || Url.startsWith("/base/403") || Url.startsWith("/base/404") || Url.startsWith("/base/500")){
            chain.doFilter(request, response);
            return;
        }
        FreemarkerConfig.updateConfig();
        String host = request.getServerName();
        if(Url.startsWith("/file/")) {
            if(BaseSaasConfig.getApplication() == null) {
                BaseSaasConfig.existDomain(host);
            }
            chain.doFilter(request, response);
            return;
        }
        if (host != null && BaseSaasConfig.existDomain(host)) {
            redisSession.setKeyPrefix("shiro:" + BaseSaasConfig.getApplication() + ":session:");
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("/base/402").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
