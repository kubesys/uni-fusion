import amqp from 'amqplib';

const AMQP_SERVER = 'amqp://139.9.165.93:5672'; // AMQP服务器地址
const AMQP_USERNAME = 'rabbitmq';
const AMQP_PASSWORD = 'onceas';

// 连接到AMQP服务器
async function connect() {
    try {
        console.log(1)
        const credentials = { username: AMQP_USERNAME, password: AMQP_PASSWORD };
        console.log(1)
        const connection = await amqp.connect(AMQP_SERVER, credentials);
        console.log(1)
        const channel = await connection.createChannel();

        // 定义一个队列
        const queue = 'pods';
        await channel.assertQueue(queue, { durable: false });

        // 发送消息到队列
        const message = 'Hello, AMQP!';
        channel.sendToQueue(queue, Buffer.from(message));

        console.log(`[x] Sent ${message}`);

        // 接收消息
        channel.consume(queue, (msg) => {
            console.log(`[x] Received ${msg.content.toString()}`);
        }, { noAck: true }); // noAck设置为true表示不需要发送确认消息

    } catch (error) {
        console.error('Error:', error);
    }
}

connect();
