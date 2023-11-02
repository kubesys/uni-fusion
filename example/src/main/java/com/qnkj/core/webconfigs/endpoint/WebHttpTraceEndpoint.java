package com.qnkj.core.webconfigs.endpoint;

import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;

import java.util.List;

/**
 * @author Oldhand
 */
@WebEndPoint
public class WebHttpTraceEndpoint {

    private final HttpTraceRepository repository;

    public WebHttpTraceEndpoint(HttpTraceRepository repository) {
        this.repository = repository;
    }

    public WebHttpTraceDescriptor traces() {
        return new WebHttpTraceDescriptor(this.repository.findAll());
    }

    public static final class WebHttpTraceDescriptor {

        private final List<HttpTrace> traces;

        private WebHttpTraceDescriptor(List<HttpTrace> traces) {
            this.traces = traces;
        }

        public List<HttpTrace> getTraces() {
            return this.traces;
        }
    }
}
