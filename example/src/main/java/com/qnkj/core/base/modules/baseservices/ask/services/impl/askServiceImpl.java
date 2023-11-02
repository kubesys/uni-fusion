package com.qnkj.core.base.modules.baseservices.ask.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Profile;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.baseservices.ask.entitys.ask;
import com.qnkj.core.base.modules.baseservices.ask.entitys.askMessage;
import com.qnkj.core.base.modules.baseservices.ask.services.IaskService;
import com.qnkj.core.plugins.netty.Services.INettyService;
import com.qnkj.core.utils.ProfileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by Auto Generator
 * create date 2021-08-18
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class askServiceImpl implements IaskService {

    private final INettyService nettyService;

    private static Map<String,Profile> caches = new HashMap<>();

    @Override
    public List<String> getPopupEditViewFields() {
        return Arrays.asList("askstatus");
    }

    @Override
    public void reply(String record, String replyBody) {
        try {
            String currentProfileid = ProfileUtils.getCurrentProfileId();
            Content content = XN_Content.load(record,"",0);
            ask ask = new ask(content);
            ask.deleted = 0;
            ask.replied = "yes";
            ask.respondent = currentProfileid;
            if ("0".equals(ask.askstatus)) {
                ask.askstatus = "1";
            } else if ("1".equals(ask.askstatus)) {
                ask.askstatus = "2";
            }
            ask.lastreplytime = DateTimeUtils.getNowDate("yyyy-MM-dd HH:mm");
            ask.toContent(content);
            content.save("ask,report,report_ask");

            Content askInfo = XN_Content.create("askmessage","", currentProfileid);
            askInfo.add("deleted","0");
            askInfo.add("from",currentProfileid);
            askInfo.add("to",ask.profileid);
            askInfo.add("record",ask.id);
            askInfo.add("body",replyBody);
            askInfo.add("issign","no");
            askInfo.add("isread","no");
            askInfo.add("readtime","");
            askInfo.save("askmessage");
            String msgid = askInfo.id;
            if (nettyService.checkOnline(ask.profileid)) {
                nettyService.sendMessage(ask.profileid,replyBody,msgid);
            }

        }catch (Exception ignored) { }
    }

    @Override
    public Map<String, Object> getAskMessage(Map<String, Object> httpRequest) {
        try {
            int page = 1;
            int limit = ProfileUtils.getPageLimit();
            String sortBy = "published";
            String sortType = "desc";
            if(!Utils.isEmpty(httpRequest.get("page"))){
                page = Double.valueOf(httpRequest.getOrDefault("page","1").toString()).intValue();
            }
            if(httpRequest.containsKey("limit") && httpRequest.get("limit").toString().compareTo("") != 0){
                limit = Double.valueOf(httpRequest.getOrDefault("limit","20").toString()).intValue();
            }
            if(!Utils.isEmpty(httpRequest.get("sortBy"))){
                sortBy = httpRequest.getOrDefault("sortBy","published").toString();
            }
            if(!Utils.isEmpty(httpRequest.get("sortType"))){
                sortType = httpRequest.getOrDefault("sortType","desc").toString();
            }
            if(!Utils.isEmpty(httpRequest.get("record"))) {
                if (!Arrays.asList("title", "published", "author", "updated").contains(sortBy)) {
                    sortBy = "my." + sortBy;
                }
                String record = httpRequest.get("record").toString();
                XN_Query query = XN_Query.contentQuery().tag("askmessage")
                        .filter("type", "eic", "askmessage")
                        .filter("my.record", "=", record)
                        .order(sortBy,sortType).notDelete()
                        .alwaysReturnTotalCount()
                        .begin((page - 1) * limit)
                        .end(page * limit);
                List<Object> result = query.execute();
                List<Object> listEntitys = new ArrayList<>();
                for(Object item : result) {
                    listEntitys.add(new askMessage(item));
                }
                long finalCount = query.getTotalCount();
                Map<String,Object> info = new HashMap<>(1);
                info.put("list", listEntitys);
                info.put("total", finalCount);
                return info;
            }

        }catch (Exception ignored) { }
        Map<String,Object> info = new HashMap<>(1);
        info.put("list", new ArrayList<>());
        info.put("total", 0);
        return info;
    }

    @Override
    public List<askMessage> getUnsignMessage(String profileid) {
        List<askMessage> lists = new LinkedList<>();
        try{
            List<Object> asks = XN_Query.contentQuery().tag("ask")
                    .filter("type","eic","ask")
                    .notDelete()
                    .filter("my.profileid","=",profileid)
                    .end(1).execute();
            if (!asks.isEmpty()) {
                Content ask = (Content)asks.get(0);
                List<Object> list = XN_Query.contentQuery().tag("askmessage")
                        .filter("type","eic","askmessage")
                        .notDelete()
                        .filter("my.record","=",ask.id)
                        .filter("my.issign","=","no")
                        .end(-1).execute();
                for(Object item: list){
                    lists.add(new askMessage(item));
                }
            }
        }catch (Exception ignored){ }
        return lists;
    }

    @Override
    public List<Object> getAllAskMessage(String profileid) {
        try{
            String currentProfileId = ProfileUtils.getCurrentProfileId();
            List<Object> asks = XN_Query.contentQuery().tag("ask")
                    .filter("type","eic","ask")
                    .notDelete()
                    .filter("my.profileid","=",profileid)
                    .end(1).execute();
            if (!asks.isEmpty()) {
                Content ask = (Content)asks.get(0);
                List<Object> list = XN_Query.contentQuery().tag("askmessage")
                        .filter("type","eic","askmessage")
                        .notDelete()
                        .filter("my.record","=",ask.id)
                        .end(-1).execute();
                List<Object> listEntitys = new ArrayList<>();
                for(Object item : list) {
                    Content info = (Content)item;
                    if (info.my.containsKey("from") && info.my.containsKey("body") && !Utils.isEmpty(info.my.get("from"))) {
                        String from = info.my.get("from").toString();
                        String body = info.my.get("body").toString();
                        Profile profileInfo;
                        if (caches.containsKey(from)) {
                            profileInfo = caches.get(from);
                        } else {
                            profileInfo = XN_Profile.load(from, "profile");
                            caches.put(from,profileInfo);
                        }
                        String avatar = "/images/anonymous.jpg";
                        if (!Utils.isEmpty(profileInfo.link)) {
                            avatar = "/images/avatar/"+profileInfo.link;
                        }
                        String givenname = profileInfo.givenname;
                        if (!currentProfileId.equals(from)) {
                            givenname = "客服: " + givenname;
                        }
                        String finalAvatar = avatar;
                        String finalGivenname = givenname;
                        Map<String,Object> infoMap = new HashMap<>(1);
                        infoMap.put("username", finalGivenname);
                        infoMap.put("id", info.id);
                        infoMap.put("avatar", finalAvatar);
                        infoMap.put("timestamp", DateTimeUtils.getDateTimesTamp(info.published));
                        infoMap.put("content", body);
                        listEntitys.add(infoMap);
                    }
                }
                return listEntitys;
            }
        }catch (Exception ignored){ }
        return new ArrayList<>();
    }
}
