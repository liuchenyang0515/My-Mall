package com.me.mall.model.dao;

import com.me.mall.model.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    Category selectByName(String Name);

    List<Category> selectList();

    List<Category> selectCategoriesByParentId(Integer parentId);
}