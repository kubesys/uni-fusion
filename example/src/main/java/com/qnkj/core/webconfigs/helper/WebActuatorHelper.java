package com.qnkj.core.webconfigs.helper;

import com.google.common.base.Predicates;
import com.qnkj.core.webconfigs.annotation.Helper;
import com.qnkj.core.webconfigs.endpoint.WebMetricsEndpoint;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oldhand
 */
@Helper
@RequiredArgsConstructor
public class WebActuatorHelper {
    private final WebMetricsEndpoint metricsEndpoint;

    public List<WebMetricsEndpoint.WebMetricResponse> getMetricResponseByType(String type) {
        WebMetricsEndpoint.ListNamesResponse listNames = metricsEndpoint.listNames();
        Set<String> names = listNames.getNames();
        Iterable<String> jvm = names.stream()
                .filter(Predicates.containsPattern(type)::apply)
                .collect(Collectors.toList());
        List<WebMetricsEndpoint.WebMetricResponse> metricResponseList = new ArrayList<>();
        jvm.forEach(s -> {
            WebMetricsEndpoint.WebMetricResponse metric = metricsEndpoint.metric(s, null);
            metricResponseList.add(metric);
        });
        return metricResponseList;
    }

}
