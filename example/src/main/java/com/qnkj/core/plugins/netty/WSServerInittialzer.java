package com.qnkj.core.plugins.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * <p>@Author: james</p>
 * <p>@Description: 助手类</P>
 * <p>@Date: 创建日期 2018/11/21 14:36</P>
 * <p>@version V1.0</p>
 **/
public class WSServerInittialzer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline channelPipeline = socketChannel.pipeline();

        //websocker 基于http协议，所以要有http编辑码器
        channelPipeline.addLast("HttpServerCodec",new HttpServerCodec());

        //对写大数据流的支持
        channelPipeline.addLast(new ChunkedWriteHandler());

        //设置传输内容的最大长度为 1024 * 64
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));

        //设置请求协议，这里是websocket的，在使用http协议就用ws；在使用https协议就用wss
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //设置心跳检测的时间
        channelPipeline.addLast(new IdleStateHandler(3,6,9));

        //增加心跳
        channelPipeline.addLast(new HearBaseHandler());

        //定义的助手类
        channelPipeline.addLast(new ChatHandler());
    }
}
