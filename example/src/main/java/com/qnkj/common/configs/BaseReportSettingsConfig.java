package com.qnkj.common.configs;


import com.qnkj.common.entitys.ReportSettingsInitInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表配置基础类
 *
 * @author yl
 */
public class BaseReportSettingsConfig {
    private BaseReportSettingsConfig() {}
    public static List<ReportSettingsInitInfo> reportSettingsList = new ArrayList<>();
}
