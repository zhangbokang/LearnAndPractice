package com.zbk.myservice.service;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.alibaba.fastjson.JSONObject;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 1.使用并发编程，开启子线程去并发请求接口
 *
 * @author 张卜亢
 * @date 2019.04.20 19:14:34
 */
@Service
public class MyServiceThread {

    @Autowired
    private RemoteService remoteService;

    public String getData(final String userId, final String moneyId) {

        //记录一个方法开始时间
        long ctm = System.currentTimeMillis();

        //错误示例，Runable没有返回值
        /*new Thread(new Runnable() {
            public void run() {
                //xxxx
            }
        });*/

        //使用Callable，Callable有返回值
        Callable<JSONArray> queryUserData = new Callable<JSONArray>() {
            public JSONArray call() throws Exception {
                //调用远程接口
                //用户接口信息
                String v1 = remoteService.getUser(userId);
                JSONArray u = JSONArray.parseArray(v1);
                return u;
            }
        };
        Callable<JSONArray> queryMoneyData = new Callable<JSONArray>() {
            public JSONArray call() throws Exception {
                //调用远程接口
                //余额接口信息
                String v2 = remoteService.getMoney(moneyId);
                JSONArray m = JSONArray.parseArray(v2);
                return m;
            }
        };

        //封装FutureTask，它继承了Runable，所以可以放到new Thtead()中
        FutureTask<JSONArray> queryUserFutrue = new FutureTask<JSONArray>(queryUserData);
        FutureTask<JSONArray> queryMoneyFutrue = new FutureTask<JSONArray>(queryMoneyData);

        new Thread(queryUserFutrue).start();
        new Thread(queryMoneyFutrue).start();

        //合并数组
        JSONArray result = new JSONArray();
        try {
            //下面的get方法是阻塞方法，会等请求接口结果返回后再走下面的代码
            result.addAll(queryUserFutrue.get());
            result.addAll(queryMoneyFutrue.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //方法调用的时间打印
        System.out.println("方法调用总共花费：" + (System.currentTimeMillis() - ctm));

        //返回合并的结果
        return result.toString();
    }
}
