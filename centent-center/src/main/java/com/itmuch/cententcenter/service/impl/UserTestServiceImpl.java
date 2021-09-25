package com.itmuch.cententcenter.service.impl;

import com.itmuch.cententcenter.DTO.User.UserDTO;
import com.itmuch.cententcenter.feignClients.UserTestFeginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTestServiceImpl {
    @Autowired
    UserTestFeginClient userTestFeginClient;

    public UserDTO userDTO(UserDTO userDTO) {
        return userTestFeginClient.dto(userDTO);
    }
}
