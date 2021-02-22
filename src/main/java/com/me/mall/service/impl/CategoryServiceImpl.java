package com.me.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.dao.CategoryMapper;
import com.me.mall.model.pojo.Category;
import com.me.mall.model.request.AddCategoryReq;
import com.me.mall.model.vo.CategoryVO;
import com.me.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void update(Category updateCategory) {
        if (updateCategory.getName() != null) {
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            // 名字一样，但是id不一样，那么就冲突了
            if (categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())) {
                throw new MyMallException(MyMallExceptionEnum.NAME_EXISTED);
            }
        }
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (count == 0) {
            throw new MyMallException(MyMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        // 查不到记录，无法删除，删除失败
        if (categoryOld == null) {
            throw new MyMallException(MyMallExceptionEnum.DELETE_FAILED);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MyMallException(MyMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum,
                                 Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "type, order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    /**
     * 可以在这里打断点查看第二次后走不走这个断点或者看redis数据库
     * 127.0.0.1:6379> keys *
     * 1) "listCategoryForCustomer::SimpleKey []"
     * 在30s内可以看到redis有数据
     * 过了30s后
     * 127.0.0.1:6379> keys *
     * (empty list or set)
     * <p>
     * 对于访问量大的接口和常用的接口
     * 利用springboot整合redis整合cache做法大大提高访问速度
     *
     * @return
     */
    @Override
    @Cacheable(value = "listCategoryForCustomer")
    public List<CategoryVO> listCategoryForCustomer() {
        ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
        recursivelyFindCategories(categoryVOList, 0);
        return categoryVOList;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Integer parentId) {
        // 递归获取所有子类别，并组合成为一个"目录树"
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if (!CollectionUtils.isEmpty(categoryList)) {
            for (int i = 0; i < categoryList.size(); ++i) {
                Category category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVOList.add(categoryVO);
                // 这里当前目录id作为下一次的父id，查询有没有对应的子目录
                recursivelyFindCategories(categoryVO.getChildCategory(), categoryVO.getId());
            }
        }
    }
}
