package com.v1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("device_log")
public class DeviceLogEntity {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long deviceId;
    private Integer logType;
    private String uploadTime;
    private String content;
    private String code;


}
