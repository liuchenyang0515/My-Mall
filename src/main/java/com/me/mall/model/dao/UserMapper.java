package com.me.mall.model.dao;

import com.me.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByName(String userName);
    // 一个入参可以不写@Param，两个就需要写了，
    // 注意这里@Param里面userName大小写要和调用传进来的方法一致，否则触发统一异常处理的Exception逻辑
    User selectLogin(@Param("userName") String userName, @Param("password") String password);
}