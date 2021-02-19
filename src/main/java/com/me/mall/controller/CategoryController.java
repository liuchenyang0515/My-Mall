package com.me.mall.controller;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.pojo.User;
import com.me.mall.model.request.AddCategoryReq;
import com.me.mall.service.CategoryService;
import com.me.mall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 描述：目录Controller
 */
@Controller
public class CategoryController {
    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;

    /**
     * 比如post请求的请求体如下
     * {
     * "name":"鸭货",
     * "type":2,
     * "parentId":6,
     * "orderNum":10
     * }
     * 添加成功后去me_mall_category表里面查看数据
     * 这里利用@RequestBody注解提取数据到bean对象
     *
     * @param session
     * @param addCategoryReq
     * @return
     */
    @PostMapping("admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @RequestBody AddCategoryReq addCategoryReq) {
        if (addCategoryReq.getName() == null || addCategoryReq.getType() == null ||
                addCategoryReq.getParentId() == null || addCategoryReq.getOrderNum() == null) {
            return ApiRestResponse.error(MyMallExceptionEnum.PARAM_NOT_NULL);
        }
        User currentUser = (User) session.getAttribute(Constant.MY_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_LOGIN);
        }
        // 校验是否是管理员
        if (userService.checkAdminRole(currentUser)) {
            // 是管理员，执行操作
            categoryService.add(addCategoryReq); // 如果出现异常，下面这一行return就不会执行
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_ADMIN);
        }
    }
}
