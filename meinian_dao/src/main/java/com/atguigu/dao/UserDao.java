package com.atguigu.dao;

import com.atguigu.pojo.User;

/**
 * @author: 13936
 * @date: 2022/5/16 15:28
 * @description:
 */
public interface UserDao {
    User findUserByUsername(String username);
}
