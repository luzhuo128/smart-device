package com.v1.control;

import com.v1.config.MqttProviderClient;
import com.v1.control.dto.MyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 对外暴露发送消息的controller
 */
@RestController
@RequestMapping("/mqtt")
public class MqttController {


    @Resource
    MqttProviderClient mqttProviderClient;

    /**
     * 发送消息
     * @param qos qos
     * @param retained retained
     * @param topic topic
     * @param message message
     * @return 发送结果
     */
    @GetMapping("/sendMessage")
    public String sendMessage(int qos, boolean retained, String topic, String message){
        try {
            mqttProviderClient.publish(qos, retained, topic, message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }
}