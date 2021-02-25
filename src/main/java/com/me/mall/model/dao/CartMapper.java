package com.me.mall.model.dao;

import com.me.mall.model.pojo.Cart;
import org.apache.ibatis.annotations.Param;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    // 根据用户ID和商品ID可以确定唯一的购物车
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
}