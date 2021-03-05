package com.me.mall.controller;

import com.github.pagehelper.PageInfo;
import com.me.mall.common.ApiRestResponse;
import com.me.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述：订单后台管理Controller
 */
@RestController
public class OrderAdminController {
    @Resource
    private OrderService orderService;

    /**
     * {
     *     "status": 10000,
     *     "msg": "SUCCESS",
     *     "data": {
     *         "total": 3,
     *         "list": [
     *             {
     *                 "orderNo": "117493809617",
     *                 "userId": 17,
     *                 "totalPrice": 444,
     *                 "receiverName": "小慕",
     *                 "receiverMobile": "中国幕城",
     *                 "receiverAddress": "中国幕城",
     *                 "orderStatus": 0,
     *                 "postage": 0,
     *                 "paymentType": 1,
     *                 "deliveryTime": null,
     *                 "payTime": null,
     *                 "endTime": "2021-03-05T06:46:16.000+0000",
     *                 "createTime": "2021-03-04T09:49:38.000+0000",
     *                 "updateTime": "2021-03-04T09:49:38.000+0000",
     *                 "orderStatusName": "用户已取消",
     *                 "orderItemVOList": [
     *                     {
     *                         "orderNo": "117493809617",
     *                         "productId": 29,
     *                         "productName": "西兰花沙拉菜 350g 甜玉米粒 青豆豌豆 胡萝卜冷冻方便蔬菜",
     *                         "productImg": "http://127.0.0.1:8083/images/shalacai.jpg",
     *                         "unitPrice": 222,
     *                         "quantity": 1,
     *                         "totalPrice": 222
     *                     },
     *                     {
     *                         "orderNo": "117493809617",
     *                         "productId": 27,
     *                         "productName": "内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材",
     *                         "productImg": "http://127.0.0.1:8083/images/yangrouchuan.jpg",
     *                         "unitPrice": 222,
     *                         "quantity": 1,
     *                         "totalPrice": 222
     *                     }
     *                 ]
     *             },
     *             {
     *                 "orderNo": "116433309270",
     *                 "userId": 17,
     *                 "totalPrice": 444,
     *                 "receiverName": "小慕",
     *                 "receiverMobile": "中国幕城",
     *                 "receiverAddress": "中国幕城",
     *                 "orderStatus": 10,
     *                 "postage": 0,
     *                 "paymentType": 1,
     *                 "deliveryTime": null,
     *                 "payTime": null,
     *                 "endTime": null,
     *                 "createTime": "2021-03-04T08:43:33.000+0000",
     *                 "updateTime": "2021-03-04T08:43:33.000+0000",
     *                 "orderStatusName": "未付款",
     *                 "orderItemVOList": [
     *                     {
     *                         "orderNo": "116433309270",
     *                         "productId": 27,
     *                         "productName": "内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材",
     *                         "productImg": "http://127.0.0.1:8083/images/yangrouchuan.jpg",
     *                         "unitPrice": 222,
     *                         "quantity": 1,
     *                         "totalPrice": 222
     *                     }
     *                 ]
     *             },
     *             {
     *                 "orderNo": "114101309461",
     *                 "userId": 17,
     *                 "totalPrice": 4416,
     *                 "receiverName": "小慕",
     *                 "receiverMobile": "中国幕城",
     *                 "receiverAddress": "中国幕城",
     *                 "orderStatus": 10,
     *                 "postage": 0,
     *                 "paymentType": 1,
     *                 "deliveryTime": null,
     *                 "payTime": null,
     *                 "endTime": null,
     *                 "createTime": "2021-02-28T06:10:13.000+0000",
     *                 "updateTime": "2021-02-28T06:10:13.000+0000",
     *                 "orderStatusName": "未付款",
     *                 "orderItemVOList": [
     *                     {
     *                         "orderNo": "114101309461",
     *                         "productId": 22,
     *                         "productName": "即食海参大连野生辽刺参 新鲜速食 特级生鲜海产 60~80G",
     *                         "productImg": "http://127.0.0.1:8083/images/haishen.jpg",
     *                         "unitPrice": 699,
     *                         "quantity": 6,
     *                         "totalPrice": 4194
     *                     },
     *                     {
     *                         "orderNo": "114101309461",
     *                         "productId": 27,
     *                         "productName": "内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材",
     *                         "productImg": "http://127.0.0.1:8083/images/yangrouchuan.jpg",
     *                         "unitPrice": 222,
     *                         "quantity": 1,
     *                         "totalPrice": 222
     *                     }
     *                 ]
     *             }
     *         ],
     *         "pageNum": 1,
     *         "pageSize": 10,
     *         "size": 3,
     *         "startRow": 1,
     *         "endRow": 3,
     *         "pages": 1,
     *         "prePage": 0,
     *         "nextPage": 0,
     *         "isFirstPage": true,
     *         "isLastPage": true,
     *         "hasPreviousPage": false,
     *         "hasNextPage": false,
     *         "navigatePages": 8,
     *         "navigatepageNums": [
     *             1
     *         ],
     *         "navigateFirstPage": 1,
     *         "navigateLastPage": 1
     *     }
     * }
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("admin/order/list")
    @ApiOperation("管理员订单列表")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }
}
