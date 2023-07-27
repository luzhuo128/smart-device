package com.v1.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.config.MqttProviderClient;
import com.v1.control.dto.DeviceStrategyDTO;
import com.v1.control.dto.ReturnT;
import com.v1.dao.DeviceStrategyMapper;
import com.v1.entity.DeviceEntity;
import com.v1.entity.DeviceStrategyEntity;
import com.v1.server.DeviceService;
import com.v1.server.DeviceStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/24 15:45
 */
@Service
public class DeviceStrategyServiceImpl implements DeviceStrategyService {

    @Resource
    private DeviceStrategyMapper deviceStrategyMapper;
    @Autowired
    private DeviceService deviceService;
    @Resource
    MqttProviderClient mqttProviderClient;


    @Override
    public ReturnT save(DeviceStrategyDTO entity) {
        DeviceEntity details = deviceService.details(entity.getEui());
        if (Objects.isNull(details)) {
            return ReturnT.error("未找到设备!");
        }

        StringBuilder weekStringBuilder = new StringBuilder();
        weekStringBuilder.append("[");
        for (int i = 0; i < entity.getTactics().getWeek().length; i++) {
            // 将整数转换为字符串并添加到 StringBuilder 中
            weekStringBuilder.append(entity.getTactics().getWeek()[i]);

            // 如果不是最后一个元素，添加逗号分隔符
            if (i < entity.getTactics().getWeek().length - 1) {
                weekStringBuilder.append(",");
            }
        }
        weekStringBuilder.append("]");
        DeviceStrategyEntity deviceStrategy = new DeviceStrategyEntity();
        deviceStrategy.setDeviceId(details.getId());
        deviceStrategy.setEnableFlag(entity.getChange());
        deviceStrategy.setDeviceType(entity.getName());
        deviceStrategy.setWeek(weekStringBuilder.toString());
        if (Objects.nonNull(entity.getTactics().getLighttime())) {
            StringBuilder lightTimeStringBuilder = new StringBuilder();
            lightTimeStringBuilder.append("[");
            for (int i = 0; i < entity.getTactics().getLighttime().length; i++) {
                // 将整数转换为字符串并添加到 StringBuilder 中
                lightTimeStringBuilder.append(entity.getTactics().getLighttime()[i]);

                // 如果不是最后一个元素，添加逗号分隔符
                if (i < entity.getTactics().getLighttime().length - 1) {
                    lightTimeStringBuilder.append(",");
                }
            }
            lightTimeStringBuilder.append("]");
            deviceStrategy.setStartTime(lightTimeStringBuilder.toString());
        } else {
            StringBuilder timeStringBuilder = new StringBuilder();
            timeStringBuilder.append("[");
            for (int i = 0; i < entity.getTactics().getTime().length; i++) {
                // 将整数转换为字符串并添加到 StringBuilder 中
                timeStringBuilder.append(entity.getTactics().getTime()[i]);

                // 如果不是最后一个元素，添加逗号分隔符
                if (i < entity.getTactics().getTime().length - 1) {
                    timeStringBuilder.append(",");
                }
            }
            timeStringBuilder.append("]");
            deviceStrategy.setStartTime(timeStringBuilder.toString());
        }
        //下发到设备
        entity.setEnable(1);
        mqttProviderClient.publish(0, true, "/fb/dowm/control/" + entity.getEui(), JSON.toJSONString(entity));
        //生成一个六位数的ID存入mysql，编辑/删除的时候用
        while (true) {
            Random random = new Random();
            int randomNumber = random.nextInt(900000) + 100000;
            deviceStrategy.setEquipmentId(randomNumber);
            QueryWrapper<DeviceStrategyEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("equipment_id", randomNumber);
            List<DeviceStrategyEntity> deviceStrategyEntities = deviceStrategyMapper.selectList(queryWrapper);
            if (deviceStrategyEntities.size() == 0) {
                break;
            }
        }

        return ReturnT.ok(deviceStrategyMapper.insert(deviceStrategy));
    }

    @Override
    public ReturnT update(DeviceStrategyDTO entity) {
        DeviceEntity details = deviceService.details(entity.getEui());
        if (Objects.isNull(details)) {
            return ReturnT.error("未找到设备!");
        }
        DeviceStrategyEntity details1 = this.details(entity.getId());
        if (Objects.isNull(details1)) {
            return ReturnT.error("未找到策略!");
        }
        entity.setId(details1.getEquipmentId());
        DeviceStrategyEntity deviceStrategy = new DeviceStrategyEntity();
        deviceStrategy.setId(details1.getId());
        deviceStrategy.setDeviceId(details.getId());
        deviceStrategy.setEnableFlag(entity.getChange());
        deviceStrategy.setDeviceType(entity.getName());
        StringBuilder weekStringBuilder = new StringBuilder();
        weekStringBuilder.append("[");
        for (int i = 0; i < entity.getTactics().getWeek().length; i++) {
            // 将整数转换为字符串并添加到 StringBuilder 中
            weekStringBuilder.append(entity.getTactics().getWeek()[i]);

            // 如果不是最后一个元素，添加逗号分隔符
            if (i < entity.getTactics().getWeek().length - 1) {
                weekStringBuilder.append(",");
            }
        }
        weekStringBuilder.append("]");
        deviceStrategy.setWeek(weekStringBuilder.toString());
        if (Objects.nonNull(entity.getTactics().getLighttime())) {
            StringBuilder lightTimeStringBuilder = new StringBuilder();
            lightTimeStringBuilder.append("[");
            for (int i = 0; i < entity.getTactics().getLighttime().length; i++) {
                // 将整数转换为字符串并添加到 StringBuilder 中
                lightTimeStringBuilder.append(entity.getTactics().getLighttime()[i]);

                // 如果不是最后一个元素，添加逗号分隔符
                if (i < entity.getTactics().getLighttime().length - 1) {
                    lightTimeStringBuilder.append(",");
                }
            }
            lightTimeStringBuilder.append("]");
            deviceStrategy.setStartTime(lightTimeStringBuilder.toString());
        } else {
            StringBuilder timeStringBuilder = new StringBuilder();
            timeStringBuilder.append("[");
            for (int i = 0; i < entity.getTactics().getTime().length; i++) {
                // 将整数转换为字符串并添加到 StringBuilder 中
                timeStringBuilder.append(entity.getTactics().getTime()[i]);

                // 如果不是最后一个元素，添加逗号分隔符
                if (i < entity.getTactics().getTime().length - 1) {
                    timeStringBuilder.append(",");
                }
            }
            timeStringBuilder.append("]");
            deviceStrategy.setStartTime(timeStringBuilder.toString());
        }
        //下发到设备
        mqttProviderClient.publish(0, true, "/fb/dowm/control/" + entity.getEui(), JSON.toJSONString(entity));
        return ReturnT.ok(deviceStrategyMapper.updateById(deviceStrategy));
    }

    @Override
    public ReturnT list(Long deviceId) {
        QueryWrapper<DeviceStrategyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceId);
        return ReturnT.ok(deviceStrategyMapper.selectList(queryWrapper));
    }

    @Override
    public ReturnT delete(Integer strategyId) {
        DeviceStrategyEntity entity = this.details(strategyId);
        if (Objects.isNull(entity)) {
            return ReturnT.error("未找到策略!");
        }
        DeviceEntity details = deviceService.selectById(entity.getDeviceId());
        if (Objects.isNull(details)) {
            return ReturnT.error("未找到设备!");
        }
        DeviceStrategyDTO strategyDTO = new DeviceStrategyDTO();
        strategyDTO.setWho("app");
        strategyDTO.setName(entity.getDeviceType());
        strategyDTO.setId(entity.getEquipmentId());
        mqttProviderClient.publish(0, true, "/fb/dowm/control/" + details.getEui(), JSON.toJSONString(entity));
        return ReturnT.ok(deviceStrategyMapper.deleteById(strategyId));
    }

    private DeviceStrategyEntity details(Integer id) {
        return deviceStrategyMapper.selectById(id);
    }

}
