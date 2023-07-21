package com.v1.control;

import com.v1.control.dto.ReturnT;
import com.v1.entity.DeviceEntity;
import com.v1.server.DeviceService;
import com.v1.server.UserService;
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
@RequestMapping("/device")
public class DeviceControl {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public ReturnT save(@RequestBody @Validated DeviceEntity deviceEntity){

        return deviceService.save(deviceEntity);
    }

    @GetMapping
    public ReturnT list(){
        return deviceService.list();
    }

    @PutMapping
    public ReturnT update(@RequestBody DeviceEntity deviceEntity){

        return deviceService.update(deviceEntity);
    }

    @DeleteMapping("/{id}")
    public ReturnT delete(@PathVariable Long id){
        return deviceService.delete(id);
    }
}

