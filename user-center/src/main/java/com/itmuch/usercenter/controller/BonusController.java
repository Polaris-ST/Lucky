package com.itmuch.usercenter.controller;

import com.itmuch.usercenter.DTO.messaging.UserAddBonusMsgDTO;
import com.itmuch.usercenter.DTO.user.UserAddBonusDTO;
import com.itmuch.usercenter.entity.User;
import com.itmuch.usercenter.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/users")
public class BonusController {
    final private UserServiceImpl userService;
    @PutMapping("/add-bonus")
    public User addBonus(@RequestBody UserAddBonusDTO addBonusDTO)
    {

        //调用以前的方法
        Integer userId = addBonusDTO.getUserId();
        userService.addBonus(UserAddBonusMsgDTO.builder()
                .bonus(addBonusDTO.getBonus())
                .userID(userId)
                .description("购买")
                .event("BUY").build()
                 );
        return userService.getBaseMapper().selectById(userId);
    }
}
