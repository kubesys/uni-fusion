package com.qnkj.core.base.modules.management.announcements.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.management.announcements.entitys.Announcements;
import com.qnkj.core.base.modules.management.announcements.services.IAnnouncementsService;
import com.qnkj.core.utils.ProfileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by Auto Generator
 * create date 2020-11-17
 */

@Slf4j
@Service
public class AnnouncementsServiceImpl implements IAnnouncementsService {

    @Override
    public List<Announcements> get() throws Exception {
        List<Announcements> lists = new ArrayList<>();

        XN_Query query = XN_Query.create ( "Content" ).tag("announcements")
                .filter( "type", "eic", "announcements" )
                .filter ("my.deleted", "=",'0')
                .filter("my.approvalstatus ", "in", Arrays.asList("2","4"))
                .order("my.istop","D_N")
                .order ("published","DESC")
                .begin(0)
                .end(-1);
        List<Object> announcements = query.execute();

        if (!announcements.isEmpty() ) {
            announcements.forEach(item ->
                lists.add(new Announcements(item))
            );
        } else {
            Content newcontent = XN_Content.create("announcements","", ProfileUtils.getCurrentProfileId());
            newcontent.add("deleted","0");
            newcontent.add("profileid","my");
            newcontent.add("publisher","管理员");
            newcontent.add("title","测试公告");
            newcontent.add("body","测试公告");
            newcontent.add("istop","0");
            newcontent.add("isnew","0");
            newcontent.add("approvalstatus","2");
            newcontent.add("status","0");
            newcontent.save("announcements");
            lists.add(new Announcements(newcontent));
        }
        return lists;
    }

    @Override
    public Announcements get(String announcementid) throws Exception {
        Content announcement = XN_Content.load(announcementid,"announcements");
        return new Announcements(announcement);
    }



    @Override
    public void setTop(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        if(!Utils.isEmpty(httpRequest.get("ids")) && !Utils.isEmpty(httpRequest.get("istop"))){
            Object ids = httpRequest.get("ids");
            if(ids instanceof String){
                ids = Collections.singletonList(ids);
            }
            List<Object> list = XN_Content.loadMany((List<String>) ids,viewEntitys.getTabName());
            for(Object item : list){
                ((Content)item).my.put("istop",httpRequest.get("istop"));
            }
            XN_Content.batchsave(list,viewEntitys.getTabName());
        } else {
            throw new Exception("置顶失败！参数错误");
        }
    }

    @Override
    public void cancelRelease(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        if(!Utils.isEmpty(httpRequest.get("ids"))){
            Object ids = httpRequest.get("ids");
            if(ids instanceof String){
                ids = Collections.singletonList(ids);
            }
            List<Object> list = XN_Content.loadMany((List<String>) ids,viewEntitys.getTabName());
            for(Object item : list){
                ((Content)item).my.put("approvalstatus","0");
            }
            XN_Content.batchsave(list,viewEntitys.getTabName());
        } else {
            throw new Exception("撤消发布失败！参数错误");
        }
    }

    @Override
    public Object getActionVerify(String param, String modulename, String recordId, BaseEntityUtils viewEntitys) {
        log.info("getActionVerify : param => {} record => {} ",param,recordId);
        if (param.compareTo("SubmitOnline") == 0) {
            try {
                Content announcement = XN_Content.load(recordId,"announcements");
                String approvalstatus = announcement.my.get("approvalstatus").toString();
                if (Utils.isEmpty(approvalstatus)) {
                    return false;
                }
                if ("1".equals(approvalstatus) || "2".equals(approvalstatus) || "4".equals(approvalstatus)) {
                    return false;
                }
            } catch(Exception e) {
                return false;
            }
            return true;
        }
        return null;
    }

}
