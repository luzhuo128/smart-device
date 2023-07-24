package com.v1.server.impl;

import com.v1.control.dto.ReturnT;
import com.v1.dao.DeviceStrategyMapper;
import com.v1.entity.DeviceStrategyEntity;
import com.v1.server.DeviceStrategyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Override
    public ReturnT save(DeviceStrategyEntity deviceStrategyEntity) {
        //下发到设备
        return ReturnT.ok(deviceStrategyMapper.insert(deviceStrategyEntity));
    }
}
