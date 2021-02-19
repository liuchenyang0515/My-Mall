package com.me.mall.service.impl;

import com.me.mall.exception.MyMallException;
import com.me.mall.exception.MyMallExceptionEnum;
import com.me.mall.model.dao.UserMapper;
import com.me.mall.model.pojo.User;
import com.me.mall.service.UserService;
import com.me.mall.util.MD5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;

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
    public void register(String userName, String password) throws MyMallException {
        // 查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            throw new MyMallException(MyMallExceptionEnum.NAME_EXISTED);
        }
        // 写到数据库
        User user = new User();
        user.setUsername(userName);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        user.setPassword(password); // 密码不能存储明文，得加盐后进行md5哈希，取了摘要后base64编码
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new MyMallException(MyMallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String userName, String password) throws MyMallException {
        String md5Password = null;
        try {
            // 查询时要传入经过md5后的密码
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            throw new MyMallException(MyMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateInformation(User user) throws MyMallException {
        // 更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            throw new MyMallException(MyMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        // 1是普通用户，2是管理员
        return user.getRole().equals(2);
    }
}
