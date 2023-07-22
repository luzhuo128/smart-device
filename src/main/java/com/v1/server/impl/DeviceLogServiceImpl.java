package com.v1.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.dao.DeviceLogMapper;
import com.v1.entity.DeviceLogEntity;
import com.v1.server.DeviceLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/22 15:12
 */
@Service
public class DeviceLogServiceImpl implements DeviceLogService {

    @Resource
    private DeviceLogMapper deviceLogMapper;

    @Override
    public void save(DeviceLogEntity deviceLogEntity) {
        deviceLogMapper.insert(deviceLogEntity);
    }

    @Override
    public List<DeviceLogEntity> getLog(Integer type) {
        QueryWrapper<DeviceLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("log_type",type);
        return deviceLogMapper.selectList(queryWrapper);
    }
}
