package com.zbk.myservice.controller;

import com.zbk.myservice.service.MyService;
import com.zbk.myservice.service.MyServiceThread;
import com.zbk.myservice.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 普通同步请求controller
 *
 * @author 张卜亢
 * @date 2019.04.21 00:02:17
 */
@RestController
public class MyController {

    @Autowired
    private RemoteService remoteService;

    @Autowired
    private MyService myService;

    @Autowired
    private MyServiceThread myServiceThread;

    @RequestMapping(value = {"", "/"})
    public String service() {
        long ctm = System.currentTimeMillis();
        //直接调用远程接口
//        String result = remoteService.getUser("u10") + remoteService.getMoney("m10");
//        System.out.println("请求总共花费：" + (System.currentTimeMillis() - ctm));
        //调用普通顺序串行执行的接口
//        String result = myService.getData("u10", "m10");
        //调用优化后的service
        String result = myServiceThread.getData("u10", "m10");
        return result;
    }
}
