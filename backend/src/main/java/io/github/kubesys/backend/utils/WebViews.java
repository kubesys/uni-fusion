package io.github.kubesys.backend.utils;


import lombok.extern.slf4j.Slf4j;


/**
 * @author bingshuai@nj.iscas.ac.cn
 * @since 11.01
 */
@Slf4j
public class WebViews {
    public static final String VIEW_PREFIX = "views/";

    public static String view(String viewName) {
        if(viewName.startsWith("/views/") || viewName.startsWith("/error/")) {
            return viewName;
        }
        if(viewName.startsWith("views/")) {
            return "/" + viewName;
        }
        if(viewName.startsWith("/")) {
            return VIEW_PREFIX + viewName.substring(1);
        }else {
            return VIEW_PREFIX + viewName;
        }
    }

}
