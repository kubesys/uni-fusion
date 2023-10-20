/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.clients;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @author wuheng@iscas.ac.cn
 * @version 0.2.0
 * @since 2023/07/21
 * 
 *        面向企业基础设施管理，用户量不大，未来支持连接池 目前，为每一个Kubernetes的Kind分配一个客户端
 */
public class RabbitMQClientTest {

	private static final String QUEUE_NAME = "leases";

	public static void main(String[] args) throws Exception {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri("amqp://139.9.165.93:30304");
		factory.setUsername("rabbitmq");
		factory.setPassword("onceas");
		
		
		AMQP.BasicProperties properties = new AMQP.BasicProperties
				.Builder()
				.expiration("10000") 
				.build();
		
		 // 创建连接
//        try (Connection connection = factory.newConnection()) {
//            // 创建通道
//            try (Channel channel = connection.createChannel()) {
//                // 声明队列
//                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//
//                // 发送消息
//                String message = "Hello RabbitMQ!";
//                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
//                System.out.println(" [x] Sent '" + message + "'");
//
//                // 接收消息
////                Consumer consumer = new DefaultConsumer(channel) {
////                    @Override
////                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
////                        String message = new String(body, "UTF-8");
////                        System.out.println(" [x] Received '" + message + "'");
////                    }
////                };
////                
////                channel.basicConsume(QUEUE_NAME, true, consumer);
//            }
//        }
//		factory.setHost("kube-message-5bb49878f6-27dk8");
//		factory.setHost("139.9.165.93"); // RabbitMQ 服务器的主机地址
//		factory.setPort(30304); // RabbitMQ 服务器的端口号
//		factory.setVirtualHost("/");
//		factory.setUsername("root"); // RabbitMQ 的用户名
//		factory.setPassword("onceas"); // RabbitMQ 的密码
//		factory.setMetricsCollector(new StandardMetricsCollector());

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//
//		for (int i = 0; i < 100; i++) {
//			String message = "{\"oderId\":" + ThreadLocalRandom.current().nextInt(1000, 2000) + ", \"items\":[{\"id\":"
//					+ ThreadLocalRandom.current().nextInt(1000, 2000) + "},{\"id\":"
//					+ ThreadLocalRandom.current().nextInt(1, 10) + "}]}";
//
//			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//			System.out.println("Produced: " + message);
//			Thread.sleep(3000);
//		}

//		channel.close();
//		connection.close();

//		channel.addConfirmListener(new ConfirmListener() {
//            @Override
//            public void handleAck(long deliveryTag, boolean multiple) {
//                System.out.println("Message successfully delivered with deliveryTag: " + deliveryTag);
//            }
//
//            @Override
//            public void handleNack(long deliveryTag, boolean multiple) {
//                System.out.println("Message delivery failed with deliveryTag: " + deliveryTag);
//            }
//        });
//
//		try {
//            channel.waitForConfirmsOrDie();
//            System.out.println("Message sent successfully.");
//        } catch (IOException e) {
//            System.out.println("Message failed to send.");
//            e.printStackTrace();
//        }

		// 创建一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received message: " + message);
            }
        };

        // 开始监听队列并消费消息
        channel.basicConsume(QUEUE_NAME, true, consumer);
        
	}
}
