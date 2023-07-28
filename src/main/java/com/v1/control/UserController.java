package com.v1.control;

import com.alibaba.fastjson.JSONObject;
import com.v1.control.dto.ReturnT;
import com.v1.entity.UserEntity;
import com.v1.server.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    String AppId = "wx90cee06c8019652b";  //公众平台自己的appId
    String AppSecret = "b9203cec38f1480684452233f6d5299c";  //AppSecret

    @GetMapping("/vx/getPhoneNumber")
    public ReturnT wxGetPhoneNumber(@RequestParam("code") String code) {
        RestTemplate restTemplate = new RestTemplate();
        //获取token
        String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + AppId + "&secret=" + AppSecret;
        String tokenJsonData = restTemplate.getForObject(getTokenUrl, String.class);
        JSONObject tokenJsonObject = JSONObject.parseObject(tokenJsonData);
        String token = tokenJsonObject.getString("access_token");
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + token;
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        //利用spring原生http请求工具对接口进行请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(map, new HttpHeaders()), String.class);
        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
        jsonObject = JSONObject.parseObject(jsonObject.getString("phone_info"));
        String phoneNumber = jsonObject.getString("phoneNumber");
        //判断有没有这个手机号码，没有就新建一个用户
        UserEntity userEntity = userService.selectUserByUserName(phoneNumber);
        if (Objects.isNull(userEntity)) {
            userEntity = new UserEntity();
            userEntity.setUsername(phoneNumber);
            userService.register(userEntity);
        }
        return ReturnT.ok(phoneNumber);
    }

    @GetMapping("/vx/login")
    public ReturnT wxLogin(@RequestParam("code") String code,String phoneNumber) {
//        Result<Object> result = new Result();
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + AppId +
                "&secret=" + AppSecret +
                "&js_code=" + code +
                "&grant_type=authorization_code";
        //利用spring原生http请求工具对接口进行请求
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        //请求返回的是Json类型的数据 所以我们需要用到fastjson
        //这个判断是判断我们的请求是否出错，如果没有出错的话就能够拿到openid
        if (StringUtils.contains(jsonData, "errcode")) {
            //出错了
            return ReturnT.error(jsonObject.getString("errmsg"));
        }

        UserEntity userEntity = userService.selectUserByUserName(phoneNumber);
        if(Objects.nonNull(userEntity)) {
            userEntity.setOpenid(jsonObject.getString("openid"));
            userEntity.setSessionKey(jsonObject.getString("session_key"));
            userEntity.setUnionid(jsonObject.getString("unionid"));
            userService.update(userEntity);
        }
        return ReturnT.ok(userEntity.getSessionKey());
    }

    @PostMapping("/login")
    public ReturnT login(@RequestBody UserEntity userEntity) {

        return userService.login(userEntity);
    }

    @GetMapping
    public ReturnT selectUser(Integer id) {
        return userService.selectUser(id);
    }

    @PostMapping("/register")
    public ReturnT register(@RequestBody @Validated UserEntity userEntity) {
        return userService.register(userEntity);
    }
}
