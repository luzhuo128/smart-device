package com.v1.config;

import com.alibaba.fastjson.JSON;
import com.v1.control.dto.CollectDTO;
import com.v1.server.DeviceService;
import com.v1.server.impl.DeviceServiceImpl;
import com.v1.utils.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MqttConsumerCallback implements MqttCallback {


    /**
     * 断开连接回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("与服务器断开连接...");
    }

    /**
     * 监听消息回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        DeviceService deviceService = (DeviceServiceImpl) SpringBeanUtils.applicationContext.getBean("deviceServiceImpl");
        RedisTemplate<String, String> redisTemplate = (RedisTemplate<String, String>) SpringBeanUtils.applicationContext.getBean("redisTemplate");
        log.info("接收消息主题：{}", topic);
        log.info("接收消息Qos：{}", mqttMessage.getQos());
        log.info("接收消息内容：{}", new String(mqttMessage.getPayload()));
        log.info("接收消息retained：{}", mqttMessage.isRetained());
        try {
            String[] topics = topic.split("/");
            String eui = topics[2];
            if ("collect".equals(topics[1])) {
                //采集
                CollectDTO collectDTO = JSON.parseObject(new String(mqttMessage.getPayload()), CollectDTO.class);
                deviceService.disposeCollect(eui, collectDTO);
            } else if ("slave".equals(topics[1])) {
                CollectDTO collectDTO = JSON.parseObject(new String(mqttMessage.getPayload()), CollectDTO.class);
                if (collectDTO.getCmd().equals("0000")) {
                    //上线
                    deviceService.disposeOnline(eui, 1);
                }
            } else if ("keeplive".equals(topics[1])) {
                //拿到eui 刷新redis
                ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
                valueOps.set(eui, eui, 50, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送消息完成回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
