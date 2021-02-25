package com.me.mall.controller;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.filter.UserFilter;
import com.me.mall.model.vo.CartVO;
import com.me.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：购物车Controller
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;

    @ApiOperation("购物车列表")
    @PostMapping("/list")
    public ApiRestResponse list() {
        // 内部获取用户ID，防止横向越权
        List<CartVO> cartVOList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        Integer id = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartService.add(id, productId, count);
        return ApiRestResponse.success(cartVOList);
    }
}
