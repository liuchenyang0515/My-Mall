package com.me.mall.controller;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.filter.UserFilter;
import com.me.mall.model.vo.CartVO;
import com.me.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获得的购物车列表List<CartVO>，CartVO对象的属性是为了提供给前端，并不是某一个数据库数据直接注入的。
     * 是购物车表me_mall_cart和商品表me_mall_product做表连接之后自己选择性返回的字段数据注入到CartVO实体类
     * 获取的是某个用户购物车有效的商品(商品存在且未下架)
     * @return
     */
    @ApiOperation("购物车列表")
    @GetMapping("/list")
    public ApiRestResponse list() {
        // 内部获取用户ID，防止横向越权
        List<CartVO> cartVOList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 这里利用接口可以添加不存在的商品，但是在显示购物车列表的时候，因为表连接的条件me_mall_product p on p.id = c.product_id，
     * 购物车cart表的商品在商品product表不存在，就过滤掉了
     *
     * @param productId 商品id
     * @param count 商品数量
     * @return
     */
    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        Integer id = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartService.add(id, productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 查询购物车有没有这件商品，如果有，就进行数量上的更新
     * @param productId
     * @param count
     * @return
     */
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
