package com.zbk.myservice.service;

import java.util.concurrent.*;

/**
 * 3.自己手动实现一个FutureTask，更熟悉原理
 *
 * @param <V> the type parameter
 * @author 张卜亢
 * @date 2019.04.20 23:29:38
 */
public class FutureTaskZbk<V> implements Runnable, Future<V> {

    //封装业务逻辑
    Callable<V> callable;

    //返回值
    V result;

    //会传一个Callable，所以需要一个有参构造函数
    public FutureTaskZbk(Callable<V> callable) {
        this.callable = callable;
    }

    //在这个项目中需要用到run()和get()方法，所以主要将这两个方法实现
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //执行完后唤醒其他线程
        synchronized (this) {
            this.notifyAll();
        }
    }

    public V get() throws InterruptedException, ExecutionException {
        if (result != null) {
            return result;
        }
        //加一个等待，将线程阻塞，等待结果
        //这里阻塞的线程是主线程，和上面run里面的不一样，run里面的是启动的子线程
        //run执行完成后执行notifyAll会唤醒主线程
        synchronized (this) {
            this.wait();
        }
        return result;
    }


    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
