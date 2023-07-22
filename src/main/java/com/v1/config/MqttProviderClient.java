package com.v1.config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MqttProviderClient {

    @Value("${spring.mqtt.username}")
    private String username;
    @Value("${spring.mqtt.password}")
    private String password;
    @Value("${spring.mqtt.url}")
    private String hostUrl;
    @Value("${spring.mqtt.client.provider.id}")
    private String clientId;
    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    private MqttClient mqttClient;

    /**
     * 初始化生产者客户端
     */
    @PostConstruct
    public void init() {
        connect();
    }

    /**
     * 客户端连接服务端
     */
    public void connect(){
        try {
            // 初始化客户端
            mqttClient = new MqttClient(hostUrl, clientId, new MemoryPersistence());

            // 配置客户端连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            //是否清空session，设置false表示服务器会保留客户端的连接记录（订阅主题，qos）,客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            //设置为true表示每次连接服务器都是以新的身份
            options.setCleanSession(true);
            // 配置用户名和密码
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            // 设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            // 设置心跳时间 单位为秒，表示服务器每隔 1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            // 设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (clientId + "与服务器断开连接").getBytes(), 0, false);
            // 配置回调
            mqttClient.setCallback(new MqttProviderCallback());
            // 执行连接
            mqttClient.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    /**
     * 客户端消息发送
     * @param qos qos
     * @param retained retained
     * @param topic topic
     * @param message message
     */
    public void publish(int qos,boolean retained,String topic,String message){
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        mqttMessage.setPayload(message.getBytes());
        // 主题的目的地，用于发布/订阅信息
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        // 提供一种机制来跟踪消息的传递进度
        // 用于在以非阻塞方式（在后台运行）执行发布是跟踪消息的传递进度
        MqttDeliveryToken token;
        try {
            // 将指定消息发布到主题，但不等待消息传递完成，返回的token可用于跟踪消息的传递状态
            // 一旦此方法干净地返回，消息就已被客户端接受发布，当连接可用，将在后台完成消息传递
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
