/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.cores.clients;

import java.util.logging.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import io.github.kubesys.mirror.cores.Environment;
import io.github.kubesys.mirror.utils.MirrorUtil;

/**
 * @author wuheng@iscas.ac.cn
 * @version 0.2.0
 * @since 2023/07/21
 * 
 *        面向企业基础设施管理，用户量不大，未来支持连接池 目前，为每一个Kubernetes的Kind分配一个客户端
 */
public class RabbitMQClient {

	/**
	 * 日志
	 */
	static final Logger m_logger = Logger.getLogger(RabbitMQClient.class.getName());

	/**
	 * 默认MQ地址
	 */
	static final String DEFAULT_URL = "amqp://kube-message.kube-iscas:5672";

	/**
	 * 默认用户名
	 */
//	static final String DEFAULT_USERNAME = "rabbitmq";
	static final String DEFAULT_USERNAME = "guest";

	/**
	 * 默认密码
	 */
//	static final String DEFAULT_PASSWORD = "onceas";
	static final String DEFAULT_PASSWORD = "guest";

	/**
	 * 默认队列数据超时时间
	 */
	static final String DEFAULT_DURATION = "10000";
	
	
	/**
	 * 默认字符集
	 */
	static final String DEFAULT_CHARSET  = "UTF-8";
	
	/**
	 * 连接池
	 */
	protected Connection connection;

	/**
	 * 连接属性
	 */
	protected AMQP.BasicProperties properties;


	/**
	 * 
	 */
	public RabbitMQClient() {
		this(MirrorUtil.getEnv(Environment.ENV_MQ_URL, DEFAULT_URL),
				MirrorUtil.getEnv(Environment.ENV_MQ_USER, DEFAULT_USERNAME), 
				MirrorUtil.getEnv(Environment.ENV_MQ_PWD, DEFAULT_PASSWORD));
	}

	/**
	 * @param url      url
	 * @param username 用户名
	 * @param password 密码
	 * 
	 */
	public RabbitMQClient(String url, String username, String password) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(url);
			factory.setVirtualHost("/");
			factory.setUsername(username);
			factory.setPassword(password);
			factory.setAutomaticRecoveryEnabled(true);

			this.properties = new AMQP.BasicProperties
					.Builder()
					.expiration(DEFAULT_DURATION) 
					.build();
			this.connection = factory.newConnection();

		} catch (Exception ex) {
			m_logger.severe("wrong rabbitmq parameters，or unavailable network." + ex);
			System.exit(1);
		}
	}

	/**
	 * @param channel channel
	 * @param queue queue
	 * @param data data
	 * @throws Exception Exception
	 */
	public synchronized void publish(Channel channel, String queue, String data) throws Exception {
		channel.basicPublish("", queue, properties, data.getBytes(DEFAULT_CHARSET));
	}

	
	/**
	 * @return Connection
	 */
	public Connection getConnection() {
		return connection;
	}

}
