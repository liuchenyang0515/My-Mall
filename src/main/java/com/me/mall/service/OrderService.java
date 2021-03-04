package com.me.mall.service;

import com.me.mall.model.request.CreateOrderReq;
import com.me.mall.model.vo.OrderVO;

/**
 * 描述：订单Service
 */
public interface OrderService {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);
}
