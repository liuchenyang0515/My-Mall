package com.me.mall.service.impl;

import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.filter.UserFilter;
import com.me.mall.model.dao.CartMapper;
import com.me.mall.model.dao.ProductMapper;
import com.me.mall.model.pojo.Order;
import com.me.mall.model.pojo.OrderItem;
import com.me.mall.model.pojo.Product;
import com.me.mall.model.request.CreateOrderReq;
import com.me.mall.model.vo.CartVO;
import com.me.mall.service.CartService;
import com.me.mall.service.OrderService;
import com.me.mall.util.OrderCodeFactory;
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
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CartMapper cartMapper;

    public String create(CreateOrderReq createOrderReq) {
        // 拿到用户ID
        Integer userId = UserFilter.currentUser.getId();
        // 从购物车查找勾选的商品
        List<CartVO> cartVOList = cartService.list(userId); // 获取的是某个用户购物车有效的商品(商品存在且未下架)
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
        validSaleStatusAndStock(cartVOList);
        // 把购物车对象转为订单item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);
        // 扣库存
        for (int i = 0; i < orderItemList.size(); ++i) {
            OrderItem orderItem = orderItemList.get(i);
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock = product.getStock() - orderItem.getQuantity();
            if (stock < 0) {
                throw new MyMallException(MyMallExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }
        // 把购物车中的已勾选商品删除
        cleanCart(cartVOList);
        // 生成订单
        Order order = new Order();
        // 生成订单号,有独立的规则
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        // 循环保存每个商品到order_item表

        //
        return null;
    }

    // 把购物车中的已勾选商品删除
    private void cleanCart(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); ++i) {
            CartVO cartVO = cartVOList.get(i);
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); ++i) {
            CartVO cartVO = cartVOList.get(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            // 记录商品快照信息
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); ++i) {
            CartVO cartVO = cartVOList.get(i);
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            // 判断商品是否存在，商品是否上架
            if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
                throw new MyMallException(MyMallExceptionEnum.NOT_SALE);
            }
            // 判断商品库存
            if (cartVO.getQuantity() > product.getStock()) {
                throw new MyMallException(MyMallExceptionEnum.NOT_ENOUGH);
            }
        }
    }
}
