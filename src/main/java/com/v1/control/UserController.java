package com.v1.control;

import com.v1.control.dto.ReturnT;
import com.v1.entity.UserEntity;
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
 * @date 2023/7/10 11:08
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ReturnT login(@RequestBody UserEntity userEntity){
        return userService.login(userEntity);
    }

    @GetMapping
    public ReturnT selectUser(Integer id){
        return userService.selectUser(id);
    }

    @PostMapping("/register")
    public ReturnT register(@RequestBody @Validated UserEntity userEntity){
        return userService.register(userEntity);
    }
}
