package com.qnkj.core.base.modules.baseservices.ask.services;

import com.qnkj.core.base.modules.baseservices.ask.entitys.askMessage;
import com.qnkj.core.base.services.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-08-18
 * @author Generator
 */
public interface IaskService extends IBaseService {

    void reply(String record,String replyBody);

    Map<String, Object> getAskMessage(Map<String, Object> httpRequest);

    List<askMessage> getUnsignMessage(String profileid);

    List<Object> getAllAskMessage(String profileid);

}
