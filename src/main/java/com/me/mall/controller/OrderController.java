package com.me.mall.controller;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.model.request.CreateOrderReq;
import com.me.mall.model.vo.OrderVO;
import com.me.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述：订单Controller
 */
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * {
     *     "status": 10000,
     *     "msg": "SUCCESS",
     *     "data": "117493809617"
     * }
     * 创建订单后data就是订单号，方便根据订单号查询订单详情
     * @param createOrderReq
     * @return
     */
    @PostMapping("order/create")
    @ApiOperation("创建订单")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq) {
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    /**
     * 根据订单号查询订单详情
     * {
     *     "status": 10000,
     *     "msg": "SUCCESS",
     *     "data": {
     *         "orderNo": "117493809617",
     *         "userId": 17,
     *         "totalPrice": 444,
     *         "receiverName": "小慕",
     *         "receiverMobile": "中国幕城",
     *         "receiverAddress": "中国幕城",
     *         "orderStatus": 10,
     *         "postage": 0,
     *         "paymentType": 1,
     *         "deliveryTime": null,
     *         "payTime": null,
     *         "endTime": null,
     *         "createTime": "2021-03-04T09:49:38.000+0000",
     *         "updateTime": "2021-03-04T09:49:38.000+0000",
     *         "orderStatusName": "未付款",
     *         "orderItemVOList": [
     *             {
     *                 "orderNo": "117493809617",
     *                 "productId": 29,
     *                 "productName": "西兰花沙拉菜 350g 甜玉米粒 青豆豌豆 胡萝卜冷冻方便蔬菜",
     *                 "productImg": "http://127.0.0.1:8083/images/shalacai.jpg",
     *                 "unitPrice": 222,
     *                 "quantity": 1,
     *                 "totalPrice": 222
     *             },
     *             {
     *                 "orderNo": "117493809617",
     *                 "productId": 27,
     *                 "productName": "内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材",
     *                 "productImg": "http://127.0.0.1:8083/images/yangrouchuan.jpg",
     *                 "unitPrice": 222,
     *                 "quantity": 1,
     *                 "totalPrice": 222
     *             }
     *         ]
     *     }
     * }
     * @param orderNo
     * @return
     */
    @PostMapping("order/detail")
    @ApiOperation("前台订单详情")
    public ApiRestResponse detail(@RequestParam String orderNo) {
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }
}
