
package com.v1.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttProviderCallback implements MqttCallback {

    @Value("${spring.mqtt.client.provider.id}")
    private String clientId;

    /**
     * 断开连接回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error(clientId + "与服务器断开连接...");
    }

    /**
     * 消息送达回调
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("信息已送达...");
    }

    /**
     * 发送成功回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        IMqttAsyncClient client = iMqttDeliveryToken.getClient();
        log.info(client.getClientId() + "发布消息成功...");
    }
}
