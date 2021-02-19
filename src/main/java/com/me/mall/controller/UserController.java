package com.me.mall.controller;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.pojo.User;
import com.me.mall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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

    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("username") String username, @RequestParam("password") String password) throws MyMallException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_PASSWORD);
        }
        // 密码长度不能少于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(MyMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(username, password);
        return ApiRestResponse.success();
    }

    /**
     * 登录接口
     * 登录成功返回的json例子如下
     * {
     * "status": 10000,
     * "msg": "SUCCESS",
     * "data": {
     * "id": 16,
     * "username": "mumu6",
     * "password": null,
     * "personalizedSignature": "天气晴朗",
     * "role": 1,
     * "createTime": "2021-02-18T13:20:58.000+0000",
     * "updateTime": "2021-02-19T06:17:59.000+0000"
     * }
     * }
     * 其中role=1是普通用户，2是管理员，新注册的用户一律role=1
     *
     * @param username
     * @param password
     * @param session
     * @return
     * @throws MyMallException
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpSession session) throws MyMallException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        // 保存用户信息时，不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.MY_MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    /**
     * 更新个性签名
     * {
     * "status": 10000,
     * "msg": "SUCCESS",
     * "data": null
     * }
     *
     * @param session
     * @param signature 个性签名
     * @return
     * @throws MyMallException
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature) throws MyMallException {
        User currentUser = (User) session.getAttribute(Constant.MY_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId()); // 利用主键id去查找那条数据
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    /**
     * 退出登录在controller层操作就可以，不需要到service层
     *
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.MY_MALL_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录接口
     * @param username
     * @param password
     * @param session
     * @return
     * @throws MyMallException
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpSession session) throws MyMallException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        // 校验是否是管理员
        if (userService.checkAdminRole(user)) {
            // 是管理员
            // 保存用户信息时，不保存密码
            user.setPassword(null);
            session.setAttribute(Constant.MY_MALL_USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_ADMIN);
        }
    }
}
