package com.v1.control;

import com.v1.config.MqttConfig;
import com.v1.config.MqttGateway;
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
    private MqttGateway mqttGateway;

    @Resource
    private MqttConfig mqttConfig;

    @GetMapping("/add/{topic}")
    public String addTopic(@PathVariable("topic") String topic) {
        String[] topics = {topic};
        List<String> list = mqttConfig.addListenTopic(topics);
        return list.toString();
    }

    @GetMapping("/pub")
    public String pubTopic() {
        String topic = "temperature1";
        String msg = "client msg at: " + String.valueOf(System.currentTimeMillis());
        mqttGateway.sendToMqtt(topic, 2, msg);
        return "OK";

    }

    @GetMapping("/del/{topic}")
    public String delTopic(@PathVariable("topic") String topic) {
        List<String> list = mqttConfig.removeListenTopic(topic);
        return list.toString();
    }
}