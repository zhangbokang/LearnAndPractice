package com.zbk.myservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 通过http调用远程服务的service
 *
 * @author 张卜亢
 * @date 2019.04.20 19:01:33
 */
@Service
public class RemoteService {
//    @Autowired
    private RestTemplate restTemplate = new RestTemplate(); //使用的httpclient，做了封装

    private String baseUrl = "http://127.0.0.1:8091/api/";
    //调用远程接口获取用户
    public String getUser(String userId) {
        long curTimeMillis = System.currentTimeMillis();
        String result = restTemplate.getForEntity(baseUrl+"s1?userId=" + userId, String.class).getBody();
        System.out.println("获取用户花费时间：" + (System.currentTimeMillis() - curTimeMillis));
        return result;
    }

    //调用远程接口获取余额
    public String getMoney(String moneyId) {
        long curTimeMillis = System.currentTimeMillis();
        String result = restTemplate.getForEntity(baseUrl+"s2?moneyId=" + moneyId, String.class).getBody();
        System.out.println("获取余额花费时间：" + (System.currentTimeMillis() - curTimeMillis));
        return result;
    }
}
