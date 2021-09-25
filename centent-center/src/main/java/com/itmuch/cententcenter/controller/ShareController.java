package com.itmuch.cententcenter.controller;


import com.alibaba.nacos.common.utils.StringUtils;
import com.github.pagehelper.PageInfo;
import com.itmuch.cententcenter.DTO.Centent.ShareDTO;
import com.itmuch.cententcenter.Utils.JwtOperator;
import com.itmuch.cententcenter.auth.CheckAuthorization;
import com.itmuch.cententcenter.auth.CheckLogin;
import com.itmuch.cententcenter.entity.Share;
import com.itmuch.cententcenter.service.impl.ShareServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 分享表 前端控制器
 * </p>
 *
 * @author Polaris-ST
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ShareController {

 final private    ShareServiceImpl shareService;
 final private JwtOperator jwtOperator;
    @GetMapping("/{id}")
    @CheckLogin
    public ShareDTO selectById(@PathVariable("id") Integer id) {
         Share.builder().id(id).build();
        return shareService.selectById(id);
    }
    @GetMapping("/q")
    public PageInfo<Share> q(@RequestParam(required = false) String title,
                             @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                             @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                             @RequestHeader(value = "X-Token",required = false) String token ) {
        Integer uid =null;
        if (token.length()>32)
             if (jwtOperator.validateToken(token)) {
                 Claims claimsFromToken = jwtOperator.getClaimsFromToken(token);
                 Object id = claimsFromToken.get("id");
                 uid= (Integer) id;
             }
           //pageSize要做控制，不然传来的参数是100000就会崩
         if (pageSize>100)
             pageSize=100;
         return shareService.q(title,pageNo,pageSize,uid);

    }
    @GetMapping("/exchange/{id}")
    @CheckLogin
    public Share exchangeById(@PathVariable("id") Integer id, HttpServletRequest request)
    {
        return this.shareService.exchangeById(id,request);

    }

}

