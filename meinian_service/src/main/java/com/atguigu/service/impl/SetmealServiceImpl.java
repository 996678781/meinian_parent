package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {
        //1.保存套餐
        setmealDao.add(setmeal);//主键回填
        //2.保存关联数据
        Integer setmealId = setmeal.getId();
        setSetmealAndTravelGroup(travelgroupIds,setmealId);

        //******补充代码 解决垃圾图片问题********
        String pic = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
        //***********************************
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //利用分页插件
        //开启分页功能
        //limit (currentPage-1)*pageSize,pageSize
        PageHelper.startPage(currentPage,pageSize);  //底层是limit  ?,? 第一个？表示开始索引，第二个？表示查询的条数
        //注意这里导的Page是github中的page
        Page page=setmealDao.findPage(queryString);  //返回当前页数据
        return new PageResult(page.getTotal(),page.getResult()); //1.总记录数， 2.分页数据集合
    }

    @Override
    public List getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public Setmeal getSetmealById(Integer id) {
        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Map<String, Object>> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }

    private void setSetmealAndTravelGroup(Integer[] travelgroupIds, Integer setmealId) {
        if(travelgroupIds!=null && travelgroupIds.length>0){
            for (Integer travelgroupId : travelgroupIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("travelgroupId",travelgroupId);
                map.put("setmealId",setmealId);
                setmealDao.addSetmealAndTravelGroup(map);
            }
        }
    }
}
