package com.me.mall.service.impl;

import com.me.mall.common.Constant;
import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.dao.CartMapper;
import com.me.mall.model.dao.CategoryMapper;
import com.me.mall.model.dao.ProductMapper;
import com.me.mall.model.pojo.Cart;
import com.me.mall.model.pojo.Product;
import com.me.mall.model.vo.CartVO;
import com.me.mall.service.CartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：购物车Service实现类
 */
@Service
public class CartServiceImpl implements CartService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private CartMapper cartMapper;

    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOS = cartMapper.selectList(userId); // 得到拼装好的VO对象列表
        for (int i = 0; i < cartVOS.size(); ++i) {
            CartVO cartVO = cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOS;
    }


    @Override
    // 直接返回CartVO列表获得购物车内容，避免再次发送请求
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
//        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) { // 没查到
            // 这个商品之前不再购物车，需要新增一个记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        } else {
            // 这个商品已经在购物车了，则数量相加
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED); // 点击新增商品，一般是想买，默认勾选
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    // 校验对应商品状态，是否存在、上架以及库存量，判断是否可以完成购买
    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        // 判断商品是否存在，商品是否上架
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new MyMallException(MyMallExceptionEnum.NOT_SALE);
        }
        // 判断商品库存
        if (count > product.getStock()) {
            throw new MyMallException(MyMallExceptionEnum.NOT_ENOUGH);
        }
    }
}
