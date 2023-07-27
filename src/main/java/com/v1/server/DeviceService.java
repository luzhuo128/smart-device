package com.v1.server;

import com.v1.control.dto.CollectDTO;
import com.v1.control.dto.ReturnT;
import com.v1.entity.DeviceEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/21 14:34
 */
public interface DeviceService {

    /**
     * 添加设备
     *
     * @param deviceEntity 设备实体
     * @return ReturnT
     */
    ReturnT save(DeviceEntity deviceEntity);

    /**
     * 设备列表
     *
     * @return ReturnT
     */
    ReturnT list();

    /**
     * 编辑设备
     *
     * @param deviceEntity 设备实体
     * @return ReturnT
     */
    ReturnT update(DeviceEntity deviceEntity);

    /**
     * 删除
     * @param id id
     * @return ReturnT
     */
    ReturnT delete(Long id);

    /**
     * 根据EUI获取详情
     * @param eui eui
     * @return DeviceEntity
     */
    DeviceEntity details(String eui);

    /**
     * 处理采集
     * @param eui
     * @param collectDTO
     */
    void disposeCollect(String eui, CollectDTO collectDTO);

    /**
     * 查询日志
     * @param type
     * @return
     */
    ReturnT getLog(Integer type);

    ReturnT disposeOnline(String eui,Integer onlineFlag);

    DeviceEntity selectById(Long deviceId);
}
