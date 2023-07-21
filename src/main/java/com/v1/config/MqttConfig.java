package com.v1.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Description:消息订阅配置
 *
 * @author : laughing
 * DateTime: 2021-05-18 13:31
 */
@Configuration
public class MqttConfig {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final byte[] WILL_DATA;

    static {
        WILL_DATA = "offline".getBytes();
    }
    @Resource
    private MqttReceiveHandle mqttReceiveHandle;

    @Value("${mqtt.server.url}")
    private final String url = "tcp://110.42.210.108:1883";
    @Value("${mqtt.server.port}")
    private final String port = "1883";
    @Value("${mqtt.server.username}")
    private final String username = "admin";
    @Value("${mqtt.server.password}")
    private final String password = "public";
    @Value("${mqtt.client.consumerId}")
    private final String consumerId = "consumerClient";
    @Value("${mqtt.client.publishId}")
    private final String publishId = "publishClient";
    @Value("${mqtt.default.topic}")
    private final String topic = "topic";
    @Value("${mqtt.default.completionTimeout}")
    private final Integer completionTimeout = 3000;

    //消息驱动
    private MqttPahoMessageDrivenChannelAdapter adapter;

    //订阅的主题列表
    private String listenTopics = "";

//    //mqtt消息接收接口
//    private MqttReceiveService mqttReceiveService;
//
//    public void setMqttReceiveService(MqttReceiveService mqttReceiveService){
//        this.mqttReceiveService = mqttReceiveService;
//    }

    /**
     *  MQTT连接器选项
     * **/
    @Bean(value = "getMqttConnectOptions")
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        mqttConnectOptions.setCleanSession(true);
        // 设置超时时间 单位为秒
        mqttConnectOptions.setConnectionTimeout(10);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{url});
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(10);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        mqttConnectOptions.setWill("willTopic", WILL_DATA, 2, false);
        return mqttConnectOptions;
    }

    /**
     * MQTT工厂
     * **/
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * MQTT信息通道（生产者）
     * **/
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（生产者）
     * **/
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(publishId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(topic);
        return messageHandler;
    }

    /**
     * 配置client,监听的topic
     * MQTT消息订阅绑定（消费者）
     * **/
    @Bean
    public MessageProducer inbound() {
        if(adapter == null){
            adapter = new MqttPahoMessageDrivenChannelAdapter(consumerId, mqttClientFactory(),
                    topic);
        }
        String [] topics = listenTopics.split(",");
        for(String topic: topics){
            if(!StringUtils.isEmpty(topic)){
                adapter.addTopic(topic,1);
            }
        }
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * 增加监听的topic
     * @param topicArr 消息列表
     * @return 结果
     */
    public List<String> addListenTopic(String [] topicArr){
        if(adapter == null){
            adapter = new MqttPahoMessageDrivenChannelAdapter(consumerId, mqttClientFactory(),
                    topic);
        }
        List<String> listTopic = Arrays.asList(adapter.getTopic());
        for(String topic: topicArr){
            if(!StringUtils.isEmpty(topic)){
                if(!listTopic.contains(topic)){
                    adapter.addTopic(topic,1);
                }
            }
        }
        return Arrays.asList(adapter.getTopic());
    }

    /**
     * 移除一个监听的topic
     * @param topic
     * @return
     */
    public List<String> removeListenTopic(String topic){
        if(adapter == null){
            adapter = new MqttPahoMessageDrivenChannelAdapter(consumerId, mqttClientFactory(),
                    topic);
        }
        List<String> listTopic = Arrays.asList(adapter.getTopic());
        if(listTopic.contains(topic)){
            adapter.removeTopic(topic);
        }
        return Arrays.asList(adapter.getTopic());
    }

    /**
     * MQTT信息通道（消费者）
     * **/
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（消费者）
     * **/
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                //处理接收消息
                mqttReceiveHandle.handle(message);
                //String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                //String msg   = ((String) message.getPayload()).toString();
                //mqttReceiveService.handlerMqttMessage(topic,msg);
            }
        };
    }
}

