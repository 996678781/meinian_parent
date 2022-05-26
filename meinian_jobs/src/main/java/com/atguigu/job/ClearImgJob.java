package com.atguigu.job;

import com.atguigu.constant.RedisConstant;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    JedisPool jedisPool;

    //清理图片
    public void clearImg(){
        //两个集合取差集来获取垃圾数据的集合
        Set<String> pics=jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String pic : pics) {
            //利用七牛云删除图片
            QiniuUtils.deleteFileFromQiniu(pic);
            System.out.println("删除垃圾图片：pic="+pic);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }
    }
}
