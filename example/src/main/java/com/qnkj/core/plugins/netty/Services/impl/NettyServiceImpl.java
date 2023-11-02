package com.qnkj.core.plugins.netty.Services.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.restapi.XN_Content;
import com.github.restapi.XN_Profile;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.plugins.netty.MineChannelRel;
import com.qnkj.core.plugins.netty.Services.INettyService;
import com.qnkj.core.plugins.netty.entitys.Mine;
import com.qnkj.core.utils.ProfileUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 */
@Slf4j
@Service
public class NettyServiceImpl implements INettyService {

    private static Map<String,Profile> caches = new HashMap<>();

    @Override
    public void updateUserLine(Mine info) {

    }


    @Override
    public void receiveMessage(String profileid, String msg) {
        try {
            String supplierid = SupplierUtils.getSupplierid(profileid);
            XN_Query query = XN_Query.create ( "Content" ).tag("ask")
                    .filter( "type", "eic", "ask" )
                    .filter ("my.deleted", "=", 0)
                    .filter ("my.profileid", "=",profileid)
                    .begin(0)
                    .end(1);
            List<Object> asks = query.execute();
            String record = "";
            if (!asks.isEmpty() ) {
                Content askInfo = (Content)asks.get(0);
                askInfo.my.put("lastaskbody",msg);
                askInfo.my.put("lastasktime", DateTimeUtils.getDatetime(DateTimeUtils.DATE_SHORT_TIME_FORMAT));
                askInfo.save("ask");
                record = askInfo.id;
            } else {
                Content askInfo = XN_Content.create("ask","", profileid);
                askInfo.add("deleted","0");
                askInfo.add("profileid",profileid);
                askInfo.add("supplierid",supplierid);
                askInfo.add("lastaskbody",msg);
                askInfo.add("lastasktime", DateTimeUtils.getDatetime("yyyy-MM-dd HH:mm"));
                askInfo.add("lastreplytime","");
                askInfo.add("lastasktime","");
                askInfo.add("replied","no");
                askInfo.add("respondent","");
                askInfo.add("askstatus","0");
                askInfo.save("ask");
                record = askInfo.id;
            }
            Content askInfo = XN_Content.create("askmessage","", profileid);
            askInfo.add("deleted","0");
            askInfo.add("from",profileid);
            askInfo.add("to","");
            askInfo.add("record",record);
            askInfo.add("body",msg);
            askInfo.add("issign","yes");
            askInfo.add("isread","no");
            askInfo.add("readtime","");
            askInfo.save("askmessage");
        }catch (Exception ignored) {}
    }

    @Override
    public Boolean checkOnline(String profileid) {
        if (!Utils.isEmpty(MineChannelRel.getValue(profileid))) {
            return true;
        }
        return false;
    }

    @Override
    public void sendMessage(String profileid, String msg, String msgid) {
        if (!Utils.isEmpty(MineChannelRel.getValue(profileid))) {
            Channel pushManChannel = MineChannelRel.getValue(profileid);
            Profile info = ProfileUtils.getCurrentUser();
            Mine mine = new Mine();
            mine.setId(profileid);
            mine.setContent(msg);
            mine.setFromid("system");
            mine.setUsername("客服: " + info.givenname);
            if (Utils.isEmpty(info.link)) {
                mine.setAvatar("/images/anonymous.jpg");
            } else {
                mine.setAvatar("/images/avatar/"+info.link);
            }
            mine.setTimestamp(DateTimeUtils.gettimeStamp());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mine",mine);
            jsonObject.put("msgId",msgid);
            jsonObject.put("type","friend");
            pushManChannel.writeAndFlush(new TextWebSocketFrame(jsonObject.toJSONString()));
        }
    }

    @Override
    public void sendMessage(String to, String from, String msg, String msgid) {
        if (!Utils.isEmpty(MineChannelRel.getValue(to))) {
            try {
                Channel pushManChannel = MineChannelRel.getValue(to);
                Profile info;
                if (caches.containsKey(from)) {
                    info = caches.get(from);
                } else {
                    info = XN_Profile.load(from, "profile");
                    caches.put(from,info);
                }
                Mine mine = new Mine();
                mine.setId(to);
                mine.setContent(msg);
                mine.setFromid("system");
                mine.setUsername("客服: " + info.givenname);
                if (Utils.isEmpty(info.link)) {
                    mine.setAvatar("/images/anonymous.jpg");
                } else {
                    mine.setAvatar("/images/avatar/"+info.link);
                }
                mine.setTimestamp(DateTimeUtils.gettimeStamp());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mine", mine);
                jsonObject.put("msgId", msgid);
                jsonObject.put("type", "friend");
                pushManChannel.writeAndFlush(new TextWebSocketFrame(jsonObject.toJSONString()));
            } catch (Exception e) { }
        }
    }

    @Override
    public void signMessage(String record) {
        try {
            if (!Utils.isEmpty(record)) {
                Content askMessage = XN_Content.load(record,"askmessage",0);
                askMessage.my.put("issign","yes");
                askMessage.save("askmessage");
            }
        } catch (Exception e) { }
    }
}
