package com.v1.config;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Description:
 *
 * @author : laughing
 * DateTime: 2021-05-18 13:44
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
    /**
     * 发送信息到MQTT服务器
     *
     * @param data 发送的文本
     */
    void sendToMqtt(String data);

    /**
     * 发送信息到MQTT服务器
     *
     * @param topic 主题
     * @param payload 消息主体
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic,
                    String payload);

    /**
     * 发送信息到MQTT服务器
     *
     * @param topic 主题
     * @param qos 对消息处理的几种机制。
     * 0 表示的是订阅者没收到消息不会再次发送，消息会丢失。
     * 1 表示的是会尝试重试，一直到接收到消息，但这种情况可能导致订阅者收到多次重复消息。
     * 2 多了一次去重的动作，确保订阅者收到的消息有一次。
     * @param payload 消息主体
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic,
                    @Header(MqttHeaders.QOS) int qos,
                    String payload);
}
