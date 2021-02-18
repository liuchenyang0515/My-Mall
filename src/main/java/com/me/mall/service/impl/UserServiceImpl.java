package com.me.mall.service.impl;

import com.me.mall.model.dao.UserMapper;
import com.me.mall.model.pojo.User;
import com.me.mall.service.UserService;
import com.mysql.cj.protocol.x.ReusableInputStream;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：UserService实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Override
    public User getUser() {
        // 通过主键查询一个对象
        return userMapper.selectByPrimaryKey(2);
    }

    @Override
    public void register(String userName, String password) {
        // 查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {

        }
    }
}
