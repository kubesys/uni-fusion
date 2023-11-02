package com.qnkj.core.plugins.netty;

import com.alibaba.fastjson.JSONObject;
import com.github.restapi.XN_Rest;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.baseservices.ask.entitys.askMessage;
import com.qnkj.core.base.modules.baseservices.ask.services.IaskService;
import com.qnkj.core.plugins.netty.Services.INettyService;
import com.qnkj.core.plugins.netty.entitys.Mine;
import com.qnkj.core.plugins.netty.enums.MsgActionEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>@Author: james</p>
 * <p>@Description: 处理消息的handler；TextWebSocketFrame在netty中yoghurt为websocket 专门处理文本的对象</P>
 * <p>@Date: 创建日期 2018/11/21 14:50</P>
 * <p>@version V1.0</p>
 **/
@Slf4j
@Component
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    public INettyService nettyService;

    @Autowired
    public IaskService askService;

    private static ChatHandler chathandler;

    @PostConstruct
    public void init() {
        chathandler = this;
        chathandler.nettyService = this.nettyService;
        chathandler.askService = this.askService;
    }

    /**
     * 建立管道组
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //解析websocket传过来的json数据
        JSONObject jsonObject = (JSONObject) JSONObject.parse(msg.text());
        log.info("jsonobject : {} ",jsonObject);

        //获取推送人对象
        String pushManStr = jsonObject.get("mine").toString();
        //把发送人json转成发送人对象
        Mine pushMan = JSONObject.parseObject(pushManStr, Mine.class);
        log.info("当前发送人: {} ", pushMan);
        if (Utils.isNotEmpty(pushMan.getApplication())) {
            XN_Rest.setApplication(pushMan.getApplication());

            //判断管道组中有没有当前聊天的管道
            if(pushMan.getAction().equals(MsgActionEnum.CONNECT.type)){
                log.info("第一次进入或重新链接");
                pushMan.setStatus("online");
                chathandler.nettyService.updateUserLine(pushMan);
                //websocket第一次打开的时候，把推送人id和当前channel进行绑定
                MineChannelRel.put(pushMan.getId(),ctx.channel());
                List<askMessage> unsignLists = chathandler.askService.getUnsignMessage(pushMan.getId());
                if (!unsignLists.isEmpty()) {
                    for(askMessage item : unsignLists) {
                        chathandler.nettyService.sendMessage(item.to,item.from,item.body,item.id);
                    }
                }

            }else if(pushMan.getAction().equals(MsgActionEnum.CHAT.type)){
                log.info("开始聊天");
                chathandler.nettyService.receiveMessage(pushMan.getId(),pushMan.getContent());
            }else if(pushMan.getAction().equals(MsgActionEnum.SIGNED.type)){
                log.info("消息开始签收");
                chathandler.nettyService.signMessage(pushMan.getExtand());
            } else {
                log.info("开始心跳检测");
            }
        }
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   ctx  </p>
     * <p>@return       |   void</P>
     * <p>@date         |   2018/11/21 14:58</P>
     * <p>@description  |   当客户端建立连接后，获取客户端的channle,放到channelGroup中去进行管理</p>
     ***/
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   ctx  </p>
     * <p>@return       |   void</P>
     * <p>@date         |   2018/11/21 14:59</P>
     * <p>@description  |   添加说明</p>
     ***/
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved，channelGroup会自动移除客户端的channle
        channelGroup.remove(ctx.channel());

        //根据mineId修改用户的煮状态
//        MineService mineService = (MineService)SpringUtil.getBean("mineServiceImpl");
//
//        Mine mine = new Mine();
//        mine.setId(MineChannelRel.getKey(ctx.channel()));
//        mine.setStatus("offline");
//
//        mineService.updateById(mine);

    }

    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-24 - 下午4:13 </p>
     * <p>@param:        ctx  中文描述： </p>
     * <p>@param:        cause  中文描述： </p>
     * <p>@return:       void</p>
     * <p>@version:      v1.0</p>
     * <p>@description:  异常处理</p>
     ***/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().closeFuture();
        channelGroup.remove(ctx.channel());
    }
}
