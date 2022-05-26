package com.atguigu.dao;

import com.atguigu.pojo.Permission;

import java.util.Set;

/**
 * @author: 13936
 * @date: 2022/5/16 15:29
 * @description:
 */
public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
