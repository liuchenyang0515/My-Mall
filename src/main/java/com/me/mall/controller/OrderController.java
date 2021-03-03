package com.me.mall.controller;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.filter.UserFilter;
import com.me.mall.model.request.CreateOrderReq;
import com.me.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述：订单Controller
 */
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("order/create")
    @ApiOperation("创建订单")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq) {
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }
}
