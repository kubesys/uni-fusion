/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.targets.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.rabbitmq.client.Channel;

import io.github.kubesys.mirror.cores.clients.RabbitMQClient;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.2.0
 * @since    2023/07/25
 *
 */
public class RabbitMQQueueMgr {

	/**
	 * 日志
	 */
	static final Logger m_logger = Logger.getLogger(RabbitMQQueueMgr.class.getName());
	
	/**
	 * 连接通道
	 */
	protected static Map<String, Channel> channels = new HashMap<>();
	
	
	public final RabbitMQClient mqClient;


	public RabbitMQQueueMgr(RabbitMQClient mqClient) {
		this.mqClient = mqClient;
	}
	
	public Channel createQueueIfNeed(String queue) {
		Channel channel = channels.get(queue);
		try {
			if (channel == null) {
				channel = mqClient.getConnection().createChannel();
				channels.put(queue, channel);
				channel.queueDeclare(queue, false, false, false, null);
			}
		} catch (IOException e) {
			m_logger.warning("unable to create queue:" + e);
		}
		return channel;
	}
}
