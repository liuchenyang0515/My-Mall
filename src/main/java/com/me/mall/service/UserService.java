package com.me.mall.service;

import com.me.mall.exception.MyMallException;
import com.me.mall.model.pojo.User;

/**
 * 描述：UserService
 */
public interface UserService {
    User getUser();

    void register(String userName, String password) throws MyMallException;

    User login(String userName, String password) throws MyMallException;

    void updateInformation(User user) throws MyMallException;

    boolean checkAdminRole(User user);
}
