package com.qnkj.core.base.modules.baseservices.feedback.services;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.baseservices.feedback.entitys.feedback;
import com.qnkj.core.base.services.IBaseService;

import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-12-17
 */
public interface IfeedbackService extends IBaseService {

   default feedback load(String record) throws Exception {
        return (feedback)IBaseService.super.load(record, "feedback", 0, feedback.class);
   }
   void  saveFeedBack(Map<String, Object> Request, BaseEntityUtils viewEntitys);
}
