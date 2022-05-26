package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void addSetmealAndTravelGroup(Map<String, Integer> map);

    Page findPage(String queryString);

    List getSetmeal();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map<String, Object>> getSetmealReport();
}
