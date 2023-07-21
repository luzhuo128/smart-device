package com.v1.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.control.dto.ReturnT;
import com.v1.dao.DeviceMapper;
import com.v1.entity.DeviceEntity;
import com.v1.entity.UserEntity;
import com.v1.server.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/21 14:34
 */
@Service
public class DeviceServiceImpl implements DeviceService {


    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public ReturnT save(DeviceEntity deviceEntity) {
        deviceEntity.init();
        return ReturnT.ok(deviceMapper.insert(deviceEntity));
    }

    @Override
    public ReturnT list() {
        QueryWrapper<DeviceEntity> queryWrapper =  new QueryWrapper<DeviceEntity>();
        queryWrapper.eq("is_delete",0);
        return ReturnT.ok(deviceMapper.selectList(queryWrapper));
    }

    @Override
    public ReturnT update(DeviceEntity deviceEntity) {
        DeviceEntity device = deviceMapper.selectById(deviceEntity.getId());
        if (Objects.isNull(device)) {
            return ReturnT.error("设备不存在！");
        }
        return ReturnT.ok(deviceMapper.updateById(deviceEntity));
    }

    @Override
    public ReturnT delete(Long id) {
        DeviceEntity deviceEntity = deviceMapper.selectById(id);
        if (Objects.isNull(deviceEntity)) {
            return ReturnT.error("设备不存在！");
        }
        deviceEntity.setIsDelete(1);
        return ReturnT.ok(deviceMapper.updateById(deviceEntity));
    }
}
