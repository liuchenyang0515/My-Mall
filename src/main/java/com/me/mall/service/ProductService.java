package com.me.mall.service;

import com.github.pagehelper.PageInfo;
import com.me.mall.model.pojo.Category;
import com.me.mall.model.request.AddCategoryReq;
import com.me.mall.model.request.AddProductReq;
import com.me.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * 描述：商品Service
 */
public interface ProductService {

    void add(AddProductReq addProductReq);
}
