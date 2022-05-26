package com.atguigu.service;

import com.atguigu.pojo.User;

/**
 * @author: 13936
 * @date: 2022/5/16 15:26
 * @description:
 */
public interface UserService {
    User findUserByUsername(String username);
}
