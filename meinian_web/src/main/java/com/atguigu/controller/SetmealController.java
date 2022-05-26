package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/upload")
    //不加RequestParam("imgFile")情况下，该参数名需要和请求参数中的name名一致
    public Result upload(MultipartFile imgFile){
        try {
            //1.获取原始文件名称
            String originalFilename = imgFile.getOriginalFilename();

            //2.生成新的文件名称（防止上传同名文件被覆盖）
            //获取文件名中.的位置
            int index=originalFilename.lastIndexOf(".");
            //从.位置截取，截取到.jpg的字符串后缀
            String suffix = originalFilename.substring(index);
            //通过UUID生成前缀，拼装成一个新的字符串
            String filename = UUID.randomUUID().toString() + suffix;

            //3.调用工具类，上传图片到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);

            //*******补充代码  解决七牛云上垃圾图片的问题*******
            //将上传图片名称存入Redis，基于Redis的Set集合存储(清理七牛云中垃圾图片)
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
            //*******************************************

            //4.返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(Integer[] travelgroupIds,@RequestBody Setmeal setmeal){
        try {
            setmealService.add(travelgroupIds,setmeal);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=setmealService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }
}
