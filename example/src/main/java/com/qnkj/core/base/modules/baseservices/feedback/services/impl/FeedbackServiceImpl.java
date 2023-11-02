package com.qnkj.core.base.modules.baseservices.feedback.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.models.Content;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.baseservices.feedback.services.IfeedbackService;
import com.qnkj.core.utils.ProfileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-12-17
 */

@Slf4j
@Service
public class FeedbackServiceImpl implements IfeedbackService {

    @Override
    public void saveFeedBack(Map<String, Object> Request, BaseEntityUtils viewEntitys) {
        try {
            if (Request.containsKey("name") && Request.containsKey("mobile") && Request.containsKey("company")) {
                Content reportsettingContent = XN_Content.create(viewEntitys.getTabName(), "", ProfileUtils.getCurrentProfileId());
                reportsettingContent.add("name", Request.get("name"));
                reportsettingContent.add("mobile", Request.get("mobile"));
                reportsettingContent.add("company", Request.get("company"));
                reportsettingContent.add("industry", Request.get("industry"));
                reportsettingContent.add("memo", Request.get("memo"));
                reportsettingContent.add("deleted", "0");
                reportsettingContent.add("supplierid", "0");
                reportsettingContent.save(viewEntitys.getTabName() + ",report");
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
