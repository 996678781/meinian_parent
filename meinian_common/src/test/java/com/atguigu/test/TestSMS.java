package com.atguigu.test;

import com.atguigu.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class TestSMS {
    public static void main(String[] args) {
        String host = "http://dingxintz.market.alicloudapi.com";  //固定地址
        String path = "/dx/notifySms";      //固定映射
        String method = "POST";         //固定的post请求
        String appcode = "01d6331564d9400f9111541bd21e8d3d";
        //请求头
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);  //注意APPCODE后必须加一个空格

        //请求参数  三个参数名称是固定的
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "13936763538");
        querys.put("param", "code:9999");  //code：开头，只支持数字和字母
        querys.put("tpl_id", "TP18040315");//测试模板id，固定。如果需要自定义模板，请联系客服
        Map<String, String> bodys = new HashMap<String, String>();   //请求体（没有数据  可以不管）


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
