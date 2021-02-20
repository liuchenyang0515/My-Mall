package com.me.mall.controller;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.pojo.Category;
import com.me.mall.model.pojo.User;
import com.me.mall.model.request.AddCategoryReq;
import com.me.mall.model.request.UpdateCategoryReq;
import com.me.mall.service.CategoryService;
import com.me.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    @ApiOperation("后台添加目录") // 这个注解是在swagger-ui上方便查看接口api的注释
    @PostMapping("admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session,
                                       @Valid @RequestBody AddCategoryReq addCategoryReq) {
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

    /**
     * eg1:
     * {
     * "id": "31",
     * "name":"冰淇淋",
     * "type":3,
     * "parentId":6,
     * "orderNum":10
     * }
     * 上面请求体返回如下
     * {
     * "status": 10004,
     * "msg": "不允许重名",
     * "data": null
     * }
     * <p>
     * eg2:
     * {
     * "id": "31",
     * "name":"鸭货",
     * "type":2,
     * "parentId":6,
     * "orderNum":10
     * }
     * eg2请求体返回如下：
     * {
     * "status": 10000,
     * "msg": "SUCCESS",
     * "data": null
     * }
     * 实际情况需要根据数据库的数据来比较
     *
     * @param updateCategoryReq
     * @param session
     * @return
     */
    @ApiOperation("后台更新目录") // 这个注解是在swagger-ui上方便查看接口api的注释
    @PostMapping("admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq,
                                          HttpSession session) {
        User currentUser = (User) session.getAttribute(Constant.MY_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_LOGIN);
        }
        // 校验是否是管理员
        if (userService.checkAdminRole(currentUser)) {
            // 是管理员，执行操作
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq, category);
            categoryService.update(category); // 如果出现异常，下面这一行return就不会执行
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(MyMallExceptionEnum.NEED_ADMIN);
        }
    }
}
