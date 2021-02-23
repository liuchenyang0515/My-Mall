package com.me.mall.service;

import com.me.mall.common.ApiRestResponse;
import com.me.mall.model.pojo.Product;
import com.me.mall.model.request.AddProductReq;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：商品Service
 */
public interface ProductService {

    void add(AddProductReq addProductReq);

    ApiRestResponse upload(HttpServletRequest request, @RequestParam("file") MultipartFile file);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);
}
