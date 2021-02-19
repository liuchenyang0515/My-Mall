package com.me.mall.service.impl;

import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.dao.CategoryMapper;
import com.me.mall.model.pojo.Category;
import com.me.mall.model.request.AddCategoryReq;
import com.me.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：目录分类实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq) {
        Category category = new Category();
        // 相同属性就直接复制过去，避免重复的类似如下操作
        // category.setName(addCategoryReq.getName());
        BeanUtils.copyProperties(addCategoryReq, category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        // 如果目录已存在同名，则抛出名字已存在异常
        if (categoryOld != null) {
            // 写到这里将MyMallException extends Exception改为extends RuntimeException
            // 这样就不用老是声明异常了
            throw new MyMallException(MyMallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new MyMallException(MyMallExceptionEnum.CREATE_FAILED);
        }
    }
}
