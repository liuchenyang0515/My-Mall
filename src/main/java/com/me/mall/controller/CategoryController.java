package com.me.mall.controller;

import com.github.pagehelper.PageInfo;
import com.me.mall.common.ApiRestResponse;
import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.pojo.Category;
import com.me.mall.model.pojo.User;
import com.me.mall.model.request.AddCategoryReq;
import com.me.mall.model.request.UpdateCategoryReq;
import com.me.mall.model.vo.CategoryVO;
import com.me.mall.service.CategoryService;
import com.me.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

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

    @ApiOperation("后台删除目录") // 这个注解是在swagger-ui上方便查看接口api的注释
    @PostMapping("admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id) {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    /**
     * pageNum=1&pageSize=10
     * 数据示范
     * RESPONSE: {"status":10000,"msg":"SUCCESS","data":{"total":19,"list":[{"id":3,"name":"新鲜水果",
     * "type":1,"parentId":0,"orderNum":1,"createTime":1576603020000,"updateTime":1577524286000},
     * {"id":5,"name":"海鲜水产","type":1,"parentId":0,"orderNum":2,"createTime":1576603020000,"updateTime":1577521520000},
     * {"id":6,"name":"精选肉类","type":1,"parentId":0,"orderNum":3,"createTime":1576603020000,"updateTime":1577521521000},
     * {"id":9,"name":"冷饮冻食","type":1,"parentId":0,"orderNum":4,"createTime":1576820728000,"updateTime":1577521522000},
     * {"id":10,"name":"蔬菜蛋品","type":1,"parentId":0,"orderNum":5,"createTime":1576820728000,"updateTime":1577521523000},
     * {"id":27,"name":"美味菌菇","type":1,"parentId":0,"orderNum":7,"createTime":1576820728000,"updateTime":1581348036000},
     * {"id":4,"name":"橘子橙子","type":2,"parentId":3,"orderNum":1,"createTime":1576603020000,"updateTime":1577521510000},
     * {"id":7,"name":"螃蟹","type":2,"parentId":5,"orderNum":1,"createTime":1576603020000,"updateTime":1577521515000},
     * {"id":16,"name":"牛羊肉","type":2,"parentId":6,"orderNum":1,"createTime":1576603020000,"updateTime":1577521518000},
     * {"id":17,"name":"冰淇淋","type":2,"parentId":9,"orderNum":1,"createTime":1576603020000,"updateTime":1577521518000}],
     * "pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"pages":2,"prePage":0,"nextPage":2,
     * "isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,
     * "navigatepageNums":[1,2],"navigateFirstPage":1,"navigateLastPage":2}}
     *
     * @param pageNum  第几页
     * @param pageSize 每页有多少数据
     * @return 通用返回对象
     */
    @ApiOperation("后台目录列表") // 这个注解是在swagger-ui上方便查看接口api的注释
    @GetMapping("admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum,
                                                @RequestParam Integer pageSize) {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /***
     * 这里需要一步步调试，方便观察逻辑步骤
     * RESPONSE: {"status":10000,"msg":"SUCCESS","data":[
     * {"id":3,"name":"新鲜水果","type":1,"parentId":0,"orderNum":1,"createTime":1576603020000,"updateTime":1577524286000,
     * "childCategory":[{"id":4,"name":"橘子橙子","type":2,"parentId":3,"orderNum":1,"createTime":1576603020000,"updateTime":1577521510000,
     * "childCategory":[{"id":19,"name":"果冻橙","type":3,"parentId":4,"orderNum":1,"createTime":1576603020000,"updateTime":1581352622000,
     * "childCategory":[]}]},
     * {"id":11,"name":"草莓","type":2,"parentId":3,"orderNum":2,"createTime":1576603020000,"updateTime":1577519082000,"childCategory":[]},
     * {"id":12,"name":"奇异果","type":2,"parentId":3,"orderNum":3,"createTime":1576603020000,"updateTime":1577521512000,"childCategory":[]},
     * {"id":14,"name":"车厘子","type":2,"parentId":3,"orderNum":4,"createTime":1576603020000,"updateTime":1577521512000,"childCategory":[]},
     * {"id":28,"name":"其他水果","type":2,"parentId":3,"orderNum":4,"createTime":1576603020000,"updateTime":1577521512000,"childCategory":[]}]},
     * {"id":5,"name":"海鲜水产","type":1,"parentId":0,"orderNum":2,"createTime":1576603020000,"updateTime":1577521520000,
     * "childCategory":[{"id":7,"name":"螃蟹","type":2,"parentId":5,"orderNum":1,"createTime":1576603020000,"updateTime":1577521515000,"childCategory":[]},
     * {"id":8,"name":"鱼类","type":2,"parentId":5,"orderNum":2,"createTime":1576603020000,"updateTime":1577521516000,"childCategory":[]},
     * {"id":13,"name":"海参","type":2,"parentId":5,"orderNum":3,"createTime":1576603020000,"updateTime":1577521517000,"childCategory":[]}]},
     * {"id":6,"name":"精选肉类","type":1,"parentId":0,"orderNum":3,"createTime":1576603020000,"updateTime":1577521521000,
     * "childCategory":[{"id":16,"name":"牛羊肉","type":2,"parentId":6,"orderNum":1,"createTime":1576603020000,"updateTime":1577521518000,"childCategory":[]}]},
     * {"id":9,"name":"冷饮冻食","type":1,"parentId":0,"orderNum":4,"createTime":1576820728000,"updateTime":1577521522000,
     * "childCategory":[{"id":17,"name":"冰淇淋","type":2,"parentId":9,"orderNum":1,"createTime":1576603020000,"updateTime":1577521518000,"childCategory":[]}]},
     * {"id":10,"name":"蔬菜蛋品","type":1,"parentId":0,"orderNum":5,"createTime":1576820728000,"updateTime":1577521523000,
     * "childCategory":[{"id":18,"name":"蔬菜综合","type":2,"parentId":10,"orderNum":1,"createTime":1576603020000,"updateTime":1581353307000,"childCategory":[]}]},
     * {"id":27,"name":"美味菌菇","type":1,"parentId":0,"orderNum":7,"createTime":1576820728000,"updateTime":1581348036000,
     * "childCategory":[{"id":15,"name":"火锅食材","type":2,"parentId":27,"orderNum":5,"createTime":1576603020000,"updateTime":1581352953000,"childCategory":[]}]}]}
     * @return
     */
    @ApiOperation("前台目录列表") // 这个注解是在swagger-ui上方便查看接口api的注释
    @GetMapping("category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer() {
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
