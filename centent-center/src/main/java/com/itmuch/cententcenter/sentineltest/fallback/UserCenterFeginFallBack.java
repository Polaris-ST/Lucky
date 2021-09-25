//package com.itmuch.cententcenter.sentineltest.fallback;
//
//import com.itmuch.cententcenter.DTO.User.UserDTO;
//import com.itmuch.cententcenter.feignClients.UserCenterFeignClient;
//import feign.hystrix.FallbackFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class UserCenterFeginFallBack implements
//        FallbackFactory<UserCenterFeignClient> {
//    @Override
//    public UserCenterFeignClient create(Throwable throwable) {
//        new UserCenterFeignClient() {
//            @Override
//            public UserDTO findByID(Integer id) {
//                log.error("被sentinel流量控制了", throwable);
//                return null;
//            }
//        };
//        return null;
//
//    }
//}
