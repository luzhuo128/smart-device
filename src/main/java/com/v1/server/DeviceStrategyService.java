package com.v1.server;

import com.v1.control.dto.DeviceStrategyDTO;
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
     * @param deviceStrategyDTO
     * @return
     */
    ReturnT save(DeviceStrategyDTO deviceStrategyDTO);

    ReturnT update(DeviceStrategyDTO deviceStrategyDTO);

    ReturnT list(Long deviceId);

    ReturnT delete(Integer deviceId);
}
