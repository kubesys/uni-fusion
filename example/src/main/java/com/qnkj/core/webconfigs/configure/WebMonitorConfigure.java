package com.qnkj.core.webconfigs.configure;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oldhand
 */
@Configuration
public class WebMonitorConfigure {

    @Bean
    public HttpTraceRepository inMemoryHttpTraceRepository(){
        return new InMemoryHttpTraceRepository();
    }
}
