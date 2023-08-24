import Stomp from 'stompjs';
import { MQTT_SERVICE, MQTT_USERNAME, MQTT_PASSWORD, MQTT_topic } from './mqtt';

const client = Stomp.client(MQTT_SERVICE);

function onConnected(msg: any) {
    const topic = MQTT_topic;
    client.subscribe(topic, responseCallback, onFailed);
}

function onFailed(msg: any) {
    console.log("MQ Failed: " + msg);
}

function responseCallback(msg: any) {
    console.log("MQ msg => " + msg.body);
}

function connect() {
    const headers = {
        login: MQTT_USERNAME,
        password: MQTT_PASSWORD,
    };
    client.connect(headers, onConnected, onFailed);
}

connect();
