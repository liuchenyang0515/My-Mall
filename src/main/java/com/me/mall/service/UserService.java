package com.me.mall.service;

import com.me.mall.model.pojo.User;

/**
 * 描述：UserService
 */
public interface UserService {
    User getUser();

    void register(String userName, String password);
}
