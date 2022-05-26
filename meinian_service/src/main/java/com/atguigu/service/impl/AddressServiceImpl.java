package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.atguigu.dao.AddressDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: 13936
 * @date: 2022/5/19 16:08
 * @description:
 */

@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    @Override
    public List<Address> findAllMaps() {
        return addressDao.findAllMaps();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page page=addressDao.findPage(queryPageBean.getQueryString());
        PageResult pageResult=new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public void add(Address address) {
        addressDao.add(address);
    }

    @Override
    public void deleteById(Integer id) {
        addressDao.deleteById(id);
    }
}