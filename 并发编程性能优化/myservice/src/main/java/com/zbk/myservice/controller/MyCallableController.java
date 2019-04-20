package com.zbk.myservice.controller;

import com.zbk.myservice.service.MyServiceThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * 4.通过异步请求优化可接受的请求数
 * 开启异步请求支持需要在web.xml中的servlet标签中配置<async-supported>true</async-supported>来开启异步请求支持
 * 异步请求支持springmvc中配置参考文档
 *  https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-async-configuration-spring-mvc
 *
 *
 * @author 张卜亢
 * @date 2019.04.20 23:43:19
 */
@RestController
public class MyCallableController {

    @Autowired
    private MyServiceThread myServiceThread;

    @RequestMapping(value = {"/s1"})
    public Callable<String> service() {
        System.out.println("开始主线程：" + Thread.currentThread() + System.currentTimeMillis());
        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                System.out.println("开始子线程：" + Thread.currentThread() + System.currentTimeMillis());
                String result = myServiceThread.getData("u10", "m10");
                System.out.println("结束子线程：" + Thread.currentThread() + System.currentTimeMillis());
                return result;
            }
        };

        System.out.println("结束主线程：" + Thread.currentThread() + System.currentTimeMillis());
        return callable;
    }
}
