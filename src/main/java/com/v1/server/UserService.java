package com.v1.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.control.dto.ReturnT;
import com.v1.entity.UserEntity;
import org.apache.catalina.User;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/10 11:10
 */
public interface UserService {

    /**
     * 登陆
     * @param userEntity user
     * @return ReturnT
     */
    ReturnT login(UserEntity userEntity);

    /**
     * 获取用户信息
     * @param id id
     * @return ReturnT
     */
    ReturnT selectUser(Integer id);

    /**
     * 注册用户
     * @param userEntity 用户entity
     * @return returnT
     */
    ReturnT register(UserEntity userEntity);
}
