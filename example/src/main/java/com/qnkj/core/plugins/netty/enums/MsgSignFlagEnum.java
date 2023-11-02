package com.qnkj.core.plugins.netty.enums;

/**
 * <p>@Auther: zou zhi min</p>
 * <p>@Date: 18-11-24 17:54</p>
 * <p>@Description: class description</p>
 */
public enum  MsgSignFlagEnum {
    /**
     *
     */
    unsign(0,"未签收"),
    signed(1,"已签收");

    public final Integer type;
    public final String content;

    MsgSignFlagEnum(Integer type,String content){
        this.type = type;
        this.content = content;
    }

    public Integer getType(){
        return type;
    }

}
