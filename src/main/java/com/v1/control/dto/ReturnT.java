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

    private T Data;

    public static ReturnT ok(){
        return new ReturnT(200);
    }

    public static <T> ReturnT ok(T data){
        return new ReturnT(data);
    }

    private ReturnT(){
    }


    private ReturnT(Integer code){
        this.code = code;
        this.msg = "操作成功";
    }

    private ReturnT(T data){
        this.code = 200;
        this.msg = "操作成功";
        this.Data = data;
    }

    private ReturnT(String msg){
        this.code = 500;
        this.msg = msg;
    }

    public static ReturnT error(String message) {
        return new ReturnT(message);
    }
}
