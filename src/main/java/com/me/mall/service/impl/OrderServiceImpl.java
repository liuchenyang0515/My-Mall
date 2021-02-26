package com.me.mall.service.impl;

import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.filter.UserFilter;
import com.me.mall.model.request.CreateOrderReq;
import com.me.mall.model.vo.CartVO;
import com.me.mall.service.CartService;
import com.me.mall.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：订单Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private CartService cartService;

    public String create(CreateOrderReq createOrderReq) {
        // 拿到用户ID
        Integer userId = UserFilter.currentUser.getId();
        // 从购物车查找勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>(); // 购物车被选中的商品
        for (int i = 0; i < cartVOList.size(); ++i) {
            CartVO cartVO = cartVOList.get(i);
            if (cartVO.getSelected().equals(Constant.Cart.CHECKED)) {
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList = cartVOListTemp;
        // 如果购物车已勾选的为空，报错
        if (CollectionUtils.isEmpty(cartVOList)) {
            throw new MyMallException(MyMallExceptionEnum.CART_EMPTY);
        }
        // 判断商品是否存在、上下架状态、库存

        // 把购物车对象转为订单item对象

        // 扣库存

        // 把购物车中的已勾选商品删除

        // 生成订单

        // 生成订单号。有独立的规则

        // 循环保存每个商品到order_item表

        //
        return null;
    }
}
