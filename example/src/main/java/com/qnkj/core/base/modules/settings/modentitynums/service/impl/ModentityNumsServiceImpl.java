package com.qnkj.core.base.modules.settings.modentitynums.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.models.Content;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.modentitynums.entity.ModentityNums;
import com.qnkj.core.base.modules.settings.modentitynums.service.IModentityNumsService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * create by 徐雁
 * @author clubs
 */
@Service
public class ModentityNumsServiceImpl implements IModentityNumsService {

    @Override
    public void save(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        try {
            String record = httpRequest.getOrDefault("record","").toString();
            if(!Utils.isEmpty(record)) {
                Content conn = XN_Content.load(record, viewEntitys.getTabName());
                ModentityNums modentityNums = new ModentityNums(conn);
                modentityNums.fromRequest(httpRequest);
                modentityNums.toContent(conn);
                conn.save(viewEntitys.getTabName());
            } else {
                throw new Exception("保存失败，参数错误");
            }
        }catch (Exception e){
            throw new Exception("保存失败");
        }
    }

    @Override
    public ModentityNums getNumInfoById(String record, BaseEntityUtils viewEntitys) {
        ModentityNums modentityNums = new ModentityNums();
        if(!Utils.isEmpty(record)){
            try {
                Content conn = XN_Content.load(record, viewEntitys.getTabName());
                modentityNums.fromContent(conn);
            }catch (Exception ignored) {}
        }
        return  modentityNums;
    }
}
