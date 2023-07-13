package com.v1.control.dto;

import lombok.Data;


/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/13 9:44
 */
@Data
public class ReturnT<T> {

    private String msg;

    private Integer code;

    private Object Data;
}
