package com.v1.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.control.dto.CollectDTO;
import com.v1.control.dto.ReturnT;
import com.v1.dao.DeviceMapper;
import com.v1.entity.DeviceEntity;
import com.v1.entity.DeviceLogEntity;
import com.v1.entity.UserEntity;
import com.v1.server.DeviceLogService;
import com.v1.server.DeviceService;
import com.v1.utils.DateUtil;
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
    @Autowired
    private DeviceLogService deviceLogService;

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

    @Override
    public DeviceEntity details(String eui) {
        QueryWrapper<DeviceEntity> queryWrapper =  new QueryWrapper<DeviceEntity>();
        queryWrapper.eq("eui",eui);
        return deviceMapper.selectOne(queryWrapper);
    }

    @Override
    public void disposeCollect(String eui, CollectDTO collectDTO) {
        DeviceEntity details = this.details(eui);
        if(Objects.nonNull(details)){
            if(collectDTO.getName().equals("water")){
                details.setAutoWatering(collectDTO.getCmd());
            }else if(collectDTO.getName().equals("fert")){
                details.setLiquidFertilizer(collectDTO.getCmd());
            }
        }
        this.update(details);
        //添加采集日志
        DeviceLogEntity deviceLogEntity = new DeviceLogEntity();
        deviceLogEntity.setCode(collectDTO.getCmd());
        deviceLogEntity.setDeviceId(details.getId());
        deviceLogEntity.setLogType(3);
        deviceLogEntity.setUploadTime(DateUtil.getCurrentStringTime());
        if(collectDTO.getName().equals("water")){
            if(collectDTO.getCmd().equals("0000")){
                deviceLogEntity.setContent("满水状态");
            }else{
                deviceLogEntity.setContent("不满水状态");
            }
        }else{
            if(collectDTO.getCmd().equals("0000")){
                deviceLogEntity.setContent("液体肥:充足");
            }else{
                deviceLogEntity.setContent("液体肥:不充足");
            }
        }
        deviceLogService.save(deviceLogEntity);
    }

    @Override
    public ReturnT getLog(Integer type) {
        return ReturnT.ok(deviceLogService.getLog(type));
    }

    @Override
    public ReturnT disposeOnline(String eui,Integer onlineFlag) {
        DeviceEntity details = this.details(eui);
        if(Objects.isNull(details)){
            return ReturnT.error("未找到设备");
        }
        details.setOnlineFlag(onlineFlag);
        this.update(details);
        //添加采集日志
        DeviceLogEntity deviceLogEntity = new DeviceLogEntity();
        deviceLogEntity.setDeviceId(details.getId());
        deviceLogEntity.setLogType(onlineFlag);
        deviceLogEntity.setUploadTime(DateUtil.getCurrentStringTime());
        deviceLogEntity.setContent("上线");
        deviceLogService.save(deviceLogEntity);
        return ReturnT.ok();
    }
}
