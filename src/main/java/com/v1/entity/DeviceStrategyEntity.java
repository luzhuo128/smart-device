package com.v1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/21 14:27
 */
@Data
@TableName("device_strategy")
public class DeviceStrategyEntity {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Long deviceId;

    private Integer enableFlag;

    private String week;

    private String startTime;

    private Integer implementTime;

}
