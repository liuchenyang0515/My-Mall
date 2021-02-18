package com.me.mall.controller;

import com.me.mall.model.pojo.User;
import com.me.mall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 描述：用户控制器
 */
@Controller
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public User personalPage() {
        return userService.getUser();
    }

    public void register() {

    }
}
