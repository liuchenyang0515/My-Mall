package com.me.mall.service;

import com.me.mall.model.request.CreateOrderReq;

/**
 * 描述：订单Service
 */
public interface OrderService {

    String create(CreateOrderReq createOrderReq);
}
