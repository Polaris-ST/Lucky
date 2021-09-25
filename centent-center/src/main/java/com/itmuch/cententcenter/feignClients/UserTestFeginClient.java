package com.itmuch.cententcenter.feignClients;

import com.itmuch.cententcenter.DTO.User.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("user-center")
public interface UserTestFeginClient {
    @GetMapping("/user/demo")
    public UserDTO dto(@SpringQueryMap UserDTO user);

}
