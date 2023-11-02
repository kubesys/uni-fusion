package com.qnkj.core.plugins.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>@Author: james</p>
 * <p>@Description: 处理消息的handler；TextWebSocketFrame在netty中yoghurt为websocket 专门处理文本的对象</P>
 * <p>@Date: 创建日期 2018/11/21 14:50</P>
 * <p>@version V1.0</p>
 **/
@Slf4j
public class HearBaseHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断evt是不是IdleStateEvent（读、写空闲）
       if(evt instanceof IdleStateEvent){
        IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
        //判断websocket是否进入到读空闲状态
        if(idleStateEvent.state() == IdleState.READER_IDLE){
            log.debug("进入读空闲状态......");
        }else if(idleStateEvent.state() == IdleState.WRITER_IDLE){
            log.debug("进入写空闲状态");
        }else if(idleStateEvent.state() == IdleState.ALL_IDLE){
            //只要是读写都空闲了，关闭当前通信的管道
            Channel channel = ctx.channel();
            channel.closeFuture();
        }
       }
    }
}
