package com.zbk.otherservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OtherServiceController {
    @RequestMapping("/s1")
    public List<ResultBean> service1(String userId) throws InterruptedException {
        List<ResultBean> resultBeans = new ArrayList<>();
        resultBeans.add(new ResultBean(userId, userId));
        Thread.sleep(1000L);
        return resultBeans;
    }

    @RequestMapping("/s2")
    public List<ResultBean> service2(String moneyId) throws InterruptedException {
        List<ResultBean> resultBeans = new ArrayList<>();
        resultBeans.add(new ResultBean(moneyId, moneyId));
        Thread.sleep(2000L);
        return resultBeans;
    }
}
