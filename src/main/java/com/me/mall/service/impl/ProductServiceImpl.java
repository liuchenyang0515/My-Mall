package com.me.mall.service.impl;

import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.dao.ProductMapper;
import com.me.mall.model.pojo.Product;
import com.me.mall.model.request.AddProductReq;
import com.me.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：商品服务实现类
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Override
    public void add(AddProductReq addProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MyMallException(MyMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(productOld);
        if (count == 0) {
            throw new MyMallException(MyMallExceptionEnum.CREATE_FAILED);
        }
    }
}
