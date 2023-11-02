package com.github.restapi;


/**
 * @author oldhand
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class XN_Rest {

    private static final ThreadLocal<String> APPLICATION = new ThreadLocal<>();
    private static final ThreadLocal<String> VIEWER = new ThreadLocal<>();

    public static String getApplication() {
        String app = APPLICATION.get();
        if (app == null || app.isEmpty() ) {
            return RestConfig.getApplication();
        }
        return app;
    }

    public static void setApplication(String str) {
        APPLICATION.set(str);
    }

    public static String getViewer() {
        return VIEWER.get();
    }

    public static void setViewer(String str) {
        VIEWER.set(str);
    }

    public void unload() {
        VIEWER.remove();
        APPLICATION.remove();
    }
}
