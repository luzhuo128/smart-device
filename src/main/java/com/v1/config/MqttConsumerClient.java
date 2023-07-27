package com.v1.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MqttConsumerClient {

    @Value("${spring.mqtt.username}")
    private String username;
    @Value("${spring.mqtt.password}")
    private String password;
    @Value("${spring.mqtt.url}")
    private String hostUrl;
    @Value("${spring.mqtt.client.consumer.id}")
    private String clientId;
    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    /**
     * mqtt 客户端
     */
    private MqttClient mqttClient;

    /**
     * 初始化客户端
     */
    @PostConstruct
    public void init() {
        // 初始化逻辑
        connect();
    }

    /**
     * 初始化客户端连接服务器
     */
    public void connect() {
        try {
            // 创建服务器连接
            mqttClient = new MqttClient(hostUrl, clientId, new MemoryPersistence());

            // 配置连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            // 是否清空session，设置为false表示服务器会保留客户端的连接记录，客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            // 设置为true表示每次连接到服务端都是以新的身份
            options.setCleanSession(true);
            // 配置用户名和密码
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            // 设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            // 设置心跳时间 单位为秒，表示服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            // 设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (clientId + "与服务器断开连接").getBytes(), 0, false);
            // 设置回调
            mqttClient.setCallback(new MqttConsumerCallback());
            // 执行连接
            mqttClient.connect(options);

            // 配置订阅主题
            int[] qos = {1, 1, 1};
            String[] topics = {"fb/up/#", "test1", "test2"};
            // 执行订阅
            mqttClient.subscribe(topics, qos);
        }  catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端断开连接
     */
    public void disConnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
