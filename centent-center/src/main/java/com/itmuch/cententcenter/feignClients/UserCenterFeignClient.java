package com.itmuch.cententcenter.feignClients;

import com.itmuch.cententcenter.Config.GlobalFeignConfiguration;
import com.itmuch.cententcenter.Config.UserCenterFeignConfiguration;
import com.itmuch.cententcenter.DTO.User.UserAddBonusDTO;
import com.itmuch.cententcenter.DTO.User.UserDTO;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "user-center",configuration = GlobalFeignConfiguration.class)
public interface UserCenterFeignClient {
    /*
     根据实例注册中心的user-center实例来查询
     */
    @GetMapping("/users/{id}")
    UserDTO findByID(@PathVariable Integer id);
    @PutMapping("/users/add-bonus")
    UserDTO addBonus(@RequestBody UserAddBonusDTO userAddBonusDTO);
}
