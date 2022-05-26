package com.atguigu.dao;

import com.atguigu.pojo.Role;

import java.util.Set;

/**
 * @author: 13936
 * @date: 2022/5/16 15:28
 * @description:
 */
public interface RoleDao {

    Set<Role> findRolesByUserId(Integer userId);
}
