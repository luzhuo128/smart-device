package com.v1.control.dto;

import com.v1.utils.DateUtil;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/26 14:37
 */
@Data
public class DeviceReturnDTO {

    private String who ;

    private String cmd ;

    private String clock;


    public void defaultDeviceReturnDTO(){
        this.who = "app";
        this.cmd  ="0000";
        this.clock = DateUtil.getCurrentStringTime1();
    }


}
