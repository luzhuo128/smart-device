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
@TableName("device")
public class DeviceEntity {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    @NotNull(message = "设备编号不能为空！")
    private String eui;

    private Integer onlineFlag;

    private String autoWatering;

    private String liquidFertilizer;

    private Integer atomize;

    private Integer atmosphere;

    private Integer isDelete;

    @NotNull(message = "用户ID不能为空！")
    private Long userId;

    public void init() {
        this.onlineFlag = 0;
        this.autoWatering = "0";
        this.liquidFertilizer = "0";
        this.atomize = 0;
        this.atmosphere = 0;
        this.isDelete = 0;
    }
}
