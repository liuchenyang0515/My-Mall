package com.me.mall.controller;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.model.request.AddProductReq;
import com.me.mall.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 描述：后台商品管理Controller
 */
public class ProductAdminController {
    @Resource
    private ProductService productService;

    @PostMapping("admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }
}
