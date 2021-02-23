package com.me.mall.service;

import com.github.pagehelper.PageInfo;
import com.me.mall.model.pojo.Category;
import com.me.mall.model.request.AddCategoryReq;
import com.me.mall.model.vo.CategoryVO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 描述：分类目录Service
 */
public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum,
                          Integer pageSize);

    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
