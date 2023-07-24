package com.v1.server;

import com.v1.control.dto.ReturnT;
import com.v1.entity.DeviceStrategyEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/24 15:45
 */
public interface DeviceStrategyService {

    /**
     * 添加设备策略
     * @param deviceStrategyEntity
     * @return
     */
    ReturnT save(DeviceStrategyEntity deviceStrategyEntity);
}
