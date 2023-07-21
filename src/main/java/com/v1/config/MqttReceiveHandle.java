package com.v1.config;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MqttReceiveHandle implements MqttCallback {

    private final Logger logger = LoggerFactory.getLogger(MqttReceiveHandle.class);

    public void handle(Message<?> message) {
        try {
            logger.info("{},客户端号：{},主题：{}，QOS:{}，消息接收到的数据：{}",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    message.getHeaders().get(MqttHeaders.ID),
                    message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC),
                    message.getHeaders().get(MqttHeaders.RECEIVED_QOS),
                    message.getPayload());
            //处理mqtt数据
            this.handle(message.getPayload().toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理错误" + e.getMessage());
        }

    }

    private void handle(String str) throws Exception {
        logger.info(str);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.warn("连接丢失");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        logger.info("消息到达:" + topic + "\n" + "消息内容：" + new String(mqttMessage.getPayload()) + "\nclientId：" + mqttMessage.getId());

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("clientId:" + iMqttDeliveryToken.getClient().getClientId());
    }
}
