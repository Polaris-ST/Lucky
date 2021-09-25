package com.itmuch.usercenter.controller;

import com.itmuch.usercenter.entity.User;
import com.itmuch.usercenter.service.impl.UserServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserTestController {
    @Autowired
    UserServiceImpl userService;


}
