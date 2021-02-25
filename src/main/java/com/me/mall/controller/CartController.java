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

    /**
     * 这里利用接口可以添加不存在的商品，但是在显示购物车列表的时候，因为表连接的条件me_mall_product p on p.id = c.product_id，
     * 购物车cart表的商品在商品product表不存在，就过滤掉了
     *
     * @param productId
     * @param count
     * @return
     */
    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        Integer id = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartService.add(id, productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("更新购物车")
    @PostMapping("/update")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
        Integer id = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartService.update(id, productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("删除购物车")
    @PostMapping("/delete")
    public ApiRestResponse delete(@RequestParam Integer productId) {
        // 不能传入userID, cartID, 否则可以删除别人的购物车
        Integer id = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartService.delete(id, productId);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("选中/取消购物车某商品")
    @PostMapping("/select")
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
        // 不能传入userID, cartID, 否则可以删除别人的购物车
        Integer id = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartService.selectOrNot(id, productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("全选中/全取消购物车某商品")
    @PostMapping("/selectAll")
    public ApiRestResponse selectAll(@RequestParam Integer selected) {
        // 不能传入userID, cartID, 否则可以删除别人的购物车
        Integer id = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartService.selectAllOrNot(id, selected);
        return ApiRestResponse.success(cartVOList);
    }
}
