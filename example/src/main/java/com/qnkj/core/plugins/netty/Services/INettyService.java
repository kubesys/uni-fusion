package com.qnkj.core.plugins.netty.Services;


import com.qnkj.core.plugins.netty.entitys.Mine;

/**
 * @author Oldhand
 */
public interface INettyService {

    /**
     * 设置用户在线
     */
    void updateUserLine(Mine info);


    void receiveMessage(String profileid,String msg);

    void signMessage(String record);

    Boolean checkOnline(String profileid);

    void sendMessage(String profileid,String msg,String msgid);

    void sendMessage(String to,String from,String msg,String msgid);
}
