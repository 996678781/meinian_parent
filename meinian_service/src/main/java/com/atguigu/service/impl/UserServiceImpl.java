package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.UserDao;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: 13936
 * @date: 2022/5/16 15:27
 * @description:
 */

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}
