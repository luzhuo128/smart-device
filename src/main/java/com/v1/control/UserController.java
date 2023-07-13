package com.v1.control;

import com.v1.control.dto.ReturnT;
import com.v1.dao.UserDao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/10 11:08
 */
@RestController("/user")
public class UserController {



    @PostMapping("/login")
    public ReturnT login(@RequestBody UserDao userDao){
        return null;
    }


    @PostMapping("/register")
    public ReturnT register(@RequestBody UserDao userDao){
        return null;
    }
}
