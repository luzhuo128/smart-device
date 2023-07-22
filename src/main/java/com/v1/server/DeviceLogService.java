package com.v1.server;

import com.v1.entity.DeviceLogEntity;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/22 15:12
 */
public interface DeviceLogService {

    /**
     * 添加日志
     * @param deviceLogEntity
     */
    void save(DeviceLogEntity deviceLogEntity);

    List<DeviceLogEntity> getLog(Integer type);
}
