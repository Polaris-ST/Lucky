package com.itmuch.usercenter.controller;


import com.itmuch.usercenter.DTO.user.JwtTokenRespDTO;
import com.itmuch.usercenter.DTO.user.LoginRespDTO;
import com.itmuch.usercenter.DTO.user.UserLoginDTO;
import com.itmuch.usercenter.DTO.user.UserRespDTO;
import com.itmuch.usercenter.Utils.JwtOperator;
import com.itmuch.usercenter.auth.CheckLogin;
import com.itmuch.usercenter.entity.User;
import com.itmuch.usercenter.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.miniapp.api.WxMaService;
import me.chanjar.weixin.miniapp.bean.WxMaJscode2SessionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 分享 前端控制器
 * </p>
 *
 * @author Polaris-ST
 * @since 2021-08-04
 */
@Slf4j
@RestController
@RequestMapping("/users")

@RequiredArgsConstructor(onConstructor = @_(@Autowired))

public class UserController {
    private final   UserServiceImpl userService;
    private final WxMaService service;
    private final JwtOperator jwtOperator;
    @GetMapping("/gen-token")
    public String token()
    {
        Map<String,Object> map=new HashMap<>(3);
        map.put("id","1");
        map.put("wxNickname","佳林");
        map.put("role","user");
        String token = jwtOperator.generateToken(map);
        return token;
    }
//    @CheckLogin
    @GetMapping("/{id}")
    @CheckLogin
    public User findById(@PathVariable("id") Integer id) {
//      return userService.findById(id);
        return userService.getBaseMapper().selectById(id);
    }
    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody UserLoginDTO loginDTO) throws WxErrorException {
        //微信小程序服务端校验是否已经登录
        WxMaJscode2SessionResult result = service.getUserService()
                .getSessionInfo(loginDTO.getCode());

        //获取微信的opendID 用户的唯一标识
        String openid = result.getOpenid();

        //看用户是否注册 如果没有注册就插入
        User user = userService.login(loginDTO, openid);
        //颁发Token
        Map<String,Object> map=new HashMap<>(3);
        map.put("id",user.getId());
        map.put("wxNickname",user.getWxNickname());
        map.put("role",user.getRoles());
        String token = jwtOperator.generateToken(map);
        log.info("用户{}登录成功，生成的token：{}，有效期到{}",user.getWxNickname(),token,
                jwtOperator.getExpirationTime());
        //构建相应

        return LoginRespDTO.builder()
                .token(JwtTokenRespDTO.builder()
                        .expirationTime(jwtOperator.getExpirationTime().getTime())
                        .token(token)
                        .build())
                .user(UserRespDTO.builder()
                        .avatarUrl(user.getAvatarUrl())
                        .bonus(user.getBonus())
                        .wxNickname(user.getWxNickname())
                        .id(user.getId()).build()).build();
    }
}

