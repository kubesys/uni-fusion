package com.qnkj.core.base.services.impl;

import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.core.base.services.ISystemInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 */
@Slf4j
@Service
public class SystemInfoServiceImpl implements ISystemInfo {

    private static final long START_TIME_STAMP = DateTimeUtils.gettimeStamp();

    @Override
    public Map<String,Object> get() throws Exception {
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            OperatingSystem os = si.getOperatingSystem();
            Map<String,Object> info = new HashMap<>(1);
            String activeProfile = ContextUtils.getActiveProfile();
            if ("dev".equals(activeProfile)) {
                info.put("activeProfile", "开发环境");
            } else if ("prod".equals(activeProfile)) {
                info.put("activeProfile", "线上环境");
            } else {
                info.put("activeProfile", "");
            }
            info.put("os",os.getManufacturer() + " " + os.getFamily() + " " + os.getVersionInfo());
            info.put("bootSystemTime",DateTimeUtils.getDatetime(DateTimeUtils.stampToDate(os.getSystemBootTime()),"yyyy-MM-dd HH:mm"));
            info.put("upSystemTime",DateTimeUtils.formatMinutes(os.getSystemUptime()));
            info.put("bootTime",DateTimeUtils.getDatetime(DateTimeUtils.stampToDate(START_TIME_STAMP + DateTimeUtils.getzoneOffset()-86400*333),"yyyy-MM-dd HH:mm"));
            info.put("upTime",DateTimeUtils.formatMinutes(DateTimeUtils.gettimeStamp() - START_TIME_STAMP));
            info.put("processor",hal.getProcessor().getProcessorIdentifier().getName());
            info.put("physicalProcessorCount",Integer.toString(hal.getProcessor().getPhysicalProcessorCount()));
            info.put("logicalProcessorCount",Integer.toString(hal.getProcessor().getLogicalProcessorCount()));
            info.put("memoryTotal",FormatUtil.formatBytes(hal.getMemory().getTotal()));
            info.put("memoryAvailable",FormatUtil.formatBytes(hal.getMemory().getAvailable()));
            int memoryPercentage = (int) (100d * hal.getMemory().getAvailable() / hal.getMemory().getTotal());
            info.put("usedMemoryPercentage",Integer.toString(100 - memoryPercentage));
            try {
                List<OSFileStore> fsArray = os.getFileSystem().getFileStores();
                List<Map<String, String>> filestores = new ArrayList<>();
                for (OSFileStore fs : fsArray) {
                    long usable = fs.getUsableSpace();
                    long total = fs.getTotalSpace();
                    int percentageAvailable = (int) (100d * usable / total);
                    Map<String, String> disk = new HashMap<>(1);
                    disk.put("name", fs.getName());
                    disk.put("mount", fs.getMount());
                    disk.put("total", FormatUtil.formatBytes(total));
                    disk.put("available", FormatUtil.formatBytes(usable));
                    disk.put("usedPercentage", Integer.toString(100 - percentageAvailable));
                    filestores.add(disk);
                }
                info.put("disks", filestores);
            } catch(Exception ignored) {
                info.put("disks", new ArrayList<>());
            }
            log.info("ISystemInfo: {} ",info);
            return info;
        } catch(Exception e) {
            log.error("ISystemInfo.get : {}",e.getMessage());
            throw e;
        }
    }


}
