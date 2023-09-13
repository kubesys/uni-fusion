// import Stomp from 'stompjs';
// import {MQTT_SERVICE, MQTT_USERNAME, MQTT_PASSWORD, MQTT_topic} from './mqtt';
//
// const client = Stomp.client(MQTT_SERVICE);
//
// function onConnected() {
//     const topic = MQTT_topic;
//     const subscribeHeaders = {
//         durable: false, // 设置队列为非持久化
//         'auto-delete': false,
//         exclusive: false
//     };
//     client.subscribe(topic, responseCallback, subscribeHeaders, onFailed);
// }
//
// function onFailed(msg: any) {
//     console.log("MQ Failed: " + msg);
// }
//
// function responseCallback(msg: any) {
//     console.log("MQ msg => " + msg.body);
//     // location.reload();
// }
//
// function connect() {
//     const headers = {
//         login: MQTT_USERNAME,
//         password: MQTT_PASSWORD,
//     };
//     client.connect(headers, onConnected, onFailed);
// }
//
// connect();
