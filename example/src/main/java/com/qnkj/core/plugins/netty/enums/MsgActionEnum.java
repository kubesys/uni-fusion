package com.qnkj.core.plugins.netty.enums;

/**
 * <p>@Auther: zou zhi min</p>
 * <p>@Date: 18-11-24 16:07</p>
 * <p>@Description: 发送消息的动作枚举</p>
 */
public enum MsgActionEnum {

    /**
     *
     */
    CONNECT(1,"第一次（或重连）初始化连接"),
    CHAT(2,"聊天消息"),
    SIGNED(3,"消息签收"),
    KEEPALIVE(4,"客户端保持心跳");

    public final Integer type;
    public final String content;


    MsgActionEnum(Integer type,String content){
        this.type = type;
        this.content = content;
    }
}
