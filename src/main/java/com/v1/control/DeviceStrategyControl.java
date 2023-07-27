package com.v1.control;

import com.v1.control.dto.DeviceStrategyDTO;
import com.v1.control.dto.ReturnT;
import com.v1.entity.DeviceEntity;
import com.v1.entity.DeviceStrategyEntity;
import com.v1.server.DeviceService;
import com.v1.server.DeviceStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/21 14:27
 */
@RestController
@RequestMapping("/device/strategy")
public class DeviceStrategyControl {

    @Autowired
    private DeviceStrategyService deviceStrategyService;


    @PostMapping
    public ReturnT save(@RequestBody @Validated DeviceStrategyDTO deviceStrategyDTO){
        return deviceStrategyService.save(deviceStrategyDTO);
    }

    @PutMapping
    public ReturnT update(@RequestBody @Validated DeviceStrategyDTO deviceStrategyDTO){
        return deviceStrategyService.update(deviceStrategyDTO);
    }

    @GetMapping("/{deviceId}")
    public ReturnT list(@PathVariable Long deviceId){
        return deviceStrategyService.list(deviceId);
    }

    @DeleteMapping("/{deviceId}")
    public ReturnT delete(@PathVariable Integer deviceId){
        return deviceStrategyService.delete(deviceId);
    }
}

