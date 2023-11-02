package com.qnkj.core.plugins.netty.enums;

/**
 * <p>@Auther: zou zhi min</p>
 * <p>@Date: 18-11-25 15:59</p>
 * <p>@Description: class description</p>
 */
public enum  ChatNexus {

    /**
     *  聊天的关系
     */
    FRIEND(1,"friend"),
    GROUP(1,"group");

    public Integer type;
    public String content;

    ChatNexus(Integer type, String content){
        this.type = type;
        this.content = content;
    }

}
