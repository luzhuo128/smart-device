package com.v1.entity;

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
 * @date 2023/7/10 11:11
 */
@Data
@TableName("user")
public class UserEntity {

    @TableId
    private Long id;

    @NotNull(message = "用户名称不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;

    private String img;

    private String gender;

    private String openid;
    private String sessionKey;
    private String unionid;
}
