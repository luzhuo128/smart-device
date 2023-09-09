package com.v1.server.impl;

import com.v1.entity.DeviceEntity;
import com.v1.entity.DeviceLogEntity;
import com.v1.server.DeviceLogService;
import com.v1.server.DeviceService;
import com.v1.server.OfflineService;
import com.v1.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/9/9 16:10
 */
@Service
public class OfflineServiceImpl implements OfflineService {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceLogService deviceLogService;


    @Override
    @Scheduled(cron ="0 */10 * * * ?")
    public void selectOfflineDevice() {
        List<DeviceEntity> deviceEntities = deviceService.selectAll();
        for (DeviceEntity deviceEntity : deviceEntities) {
            if (StringUtils.isBlank(deviceEntity.getUploadTime())) {
                continue;
            }
            // 定义日期时间格式化器
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 将字符串解析为 LocalDateTime 对象
            LocalDateTime storedTime = LocalDateTime.parse(deviceEntity.getUploadTime(), formatter);
            // 获取当前时间
            LocalDateTime currentTime = LocalDateTime.now();
            // 计算时间差
            Duration duration = Duration.between(storedTime, currentTime);
            // 判断是否超过10分钟
            if (duration.toMinutes() >= 10) {
                //设置为离线
                deviceEntity.setOnlineFlag(0);
                deviceService.update(deviceEntity);
                //添加离线日志
                //添加采集日志
                DeviceLogEntity deviceLogEntity = new DeviceLogEntity();
                deviceLogEntity.setDeviceId(deviceEntity.getId());
                deviceLogEntity.setLogType(0);
                deviceLogEntity.setUploadTime(DateUtil.getCurrentStringTime());
                deviceLogEntity.setContent("离线");
                deviceLogService.save(deviceLogEntity);
            }
        }
    }

    @Override
//    @Scheduled(cron ="0 */10 * * * ?")
    public void test() {
        System.out.println(DateUtil.getCurrentStringTime());
    }
}
