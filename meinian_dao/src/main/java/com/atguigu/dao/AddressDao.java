package com.atguigu.dao;

import com.atguigu.pojo.Address;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: 13936
 * @date: 2022/5/19 16:10
 * @description:
 */
public interface AddressDao {


    List<Address> findAllMaps();

    Page findPage(String queryString);

    void add(Address address);

    void deleteById(Integer id);
}
