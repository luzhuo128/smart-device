package com.v1.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.control.dto.ReturnT;
import com.v1.dao.UserMapper;
import com.v1.entity.UserEntity;
import com.v1.server.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/13 16:38
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    public ReturnT login(UserEntity userEntity) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userEntity.getUsername(), userEntity.getPassword());
        try {
            subject.login(token);
        }catch (Exception e){
            return ReturnT.error("登陆失败！");
        }
        return ReturnT.ok();
    }

    public ReturnT selectUser(Integer id) {
        return ReturnT.ok(userMapper.selectById(id));
    }

    public ReturnT register(UserEntity userEntity) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>();
        queryWrapper.eq("username", userEntity.getUsername());
        List<UserEntity> userEntities = userMapper.selectList(queryWrapper);
        if (!userEntities.isEmpty()) {
            return ReturnT.error("用户已存在！");
        }
        return ReturnT.ok(userMapper.insert(userEntity));
    }

    public UserEntity selectUserByUserName(String username) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }


}
