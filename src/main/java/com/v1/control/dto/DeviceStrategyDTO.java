package com.v1.control.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/26 10:43
 */
@Data
public class DeviceStrategyDTO {


    private String who;

    private String name;

    private Integer id;

    private Integer change;

    private TacticsDTO tactics;

    @NotNull(message = "设备编号不能为空！")
    private String eui;

    private Integer enable;

}
