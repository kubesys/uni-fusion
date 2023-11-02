package com.qnkj.core.base.modules.settings.backupmanage.service.impl;

import com.github.restapi.XN_Backup;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.backupmanage.service.IBackupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author oldhand
 */
@Slf4j
@Service
public class BackupServiceImpl implements IBackupService {
    @Override
    public HashMap<String, Object> getListViewEntity(HttpServletRequest request, BaseEntityUtils viewEntitys, Class<?> dataFace) {
        try {
            List<Object> result = XN_Backup.load("backup");
            List<Object> lists = new ArrayList<>();
            if (!result.isEmpty()) {
                result.forEach(item -> {
                    try {
                        Object data = dataFace.getDeclaredConstructor(Object.class).newInstance(item);
                        lists.add(data);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
            }
            HashMap<String,Object> infoMap = new HashMap<>(1);
            infoMap.put("list", lists);
            infoMap.put("total", result.size());
            return infoMap;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return new HashMap<>(1);
    }
}
