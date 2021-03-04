package com.me.mall.service.impl;

import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.filter.UserFilter;
import com.me.mall.model.dao.CartMapper;
import com.me.mall.model.dao.OrderItemMapper;
import com.me.mall.model.dao.OrderMapper;
import com.me.mall.model.dao.ProductMapper;
import com.me.mall.model.pojo.Order;
import com.me.mall.model.pojo.OrderItem;
import com.me.mall.model.pojo.Product;
import com.me.mall.model.request.CreateOrderReq;
import com.me.mall.model.vo.CartVO;
import com.me.mall.model.vo.OrderItemVO;
import com.me.mall.model.vo.OrderVO;
import com.me.mall.service.CartService;
import com.me.mall.service.OrderService;
import com.me.mall.util.OrderCodeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;

    // 数据库事务
    @Transactional(rollbackFor = Exception.class) // 遇到任何异常都要回滚
    @Override
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
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverAddress());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0); // 运费，默认为0
        order.setPaymentType(1); // 支付类型,1-在线支付
        // 插入到Order表
        orderMapper.insertSelective(order);
        // 循环保存每个商品到order_item表
        for (int i = 0; i < orderItemList.size(); ++i) {
            OrderItem orderItem = orderItemList.get(i);
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
//            throw new MyMallException(MyMallExceptionEnum.CART_EMPTY); // 模拟没有事务时的异常数据
        }
        // 把结果返回
        return orderNo;
    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (int i = 0; i < orderItemList.size(); ++i) {
            OrderItem orderItem = orderItemList.get(i);
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
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

    @Override
    public OrderVO detail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        // 订单不存在，则报错
        if (order == null) {
            throw new MyMallException(MyMallExceptionEnum.NO_ORDER);
        }
        // 订单存在，需要判断所属，不能拿到别人的订单
        Integer userId = UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)) {
            throw new MyMallException(MyMallExceptionEnum.NOT_YOUR_ORDER);
        }
        // 目前拿到的信息不足以拼装，继续获取
        OrderVO orderVO = getOrderVO(order);
        return orderVO;
    }

    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        // 获取订单对应的orderItemVOList
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        ArrayList<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (int i = 0; i < orderItemList.size(); ++i) {
            OrderItem orderItem = orderItemList.get(i);
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue()); // 订单状态
        return orderVO;
    }
}
