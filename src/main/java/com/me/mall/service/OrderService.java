package com.me.mall.service;

import com.github.pagehelper.PageInfo;
import com.me.mall.model.request.CreateOrderReq;
import com.me.mall.model.vo.OrderVO;

/**
 * 描述：订单Service
 */
public interface OrderService {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    String qrcode(String orderNo);
}
