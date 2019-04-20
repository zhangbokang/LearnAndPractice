package com.zbk.myservice.service;

import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 0.未做任何优化，用普通方式直接串行调用远程接口
 *
 * @author 张卜亢
 * @date 2019.04.20 19:14:34
 */
@Service
public class MyService {

    @Autowired
    private RemoteService remoteService;

    public String getData(String userId, String moneyId) {

        //记录一个方法开始时间
        long ctm = System.currentTimeMillis();

        //分别调用两个接口
        //结果集进行合并
        //用户接口信息
        String v1 = remoteService.getUser(userId);
        //余额接口信息
        String v2 = remoteService.getMoney(moneyId);
        //使用fastjson将结果进行合并
        /*
        //合并对象
        JSONObject u = JSONObject.parseObject(v1);
        JSONObject m = JSONObject.parseObject(v2);
        //创建一个新的JSONObject并将两个对象都添加进来
        JSONObject result = new JSONObject();
        result.putAll(u);
        result.putAll(m);
        */

        //合并数组
        JSONArray u = JSONArray.parseArray(v1);
        JSONArray m = JSONArray.parseArray(v2);
        JSONArray result = new JSONArray();
        result.addAll(u);
        result.addAll(m);
        //方法调用的时间打印
        System.out.println("方法调用总共花费：" + (System.currentTimeMillis() - ctm));

        //返回合并的结果
        return result.toString();
    }
}
