package com.me.mall.service;

import com.me.mall.model.vo.CartVO;

import java.util.List;

/**
 * 描述：购物车Service
 */
public interface CartService {

    List<CartVO> list(Integer userId);

    // 直接返回CartVO列表获得购物车内容，避免再次发送请求
    List<CartVO> add(Integer userId, Integer productId, Integer count);

    // 更新主要是更新数量
    List<CartVO> update(Integer userId, Integer productId, Integer count);

    // 更新主要是更新数量
    List<CartVO> delete(Integer userId, Integer productId);
}
