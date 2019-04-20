# 优化四步走
## 1.使用并发编程，开启子线程去并发请求接口

参考位置

```
com.zbk.myservice.service.MyServiceThread
```

## 2.使用并发编程，开启子线程去并发请求接口，子线程使用线程池

参考位置

```
com.zbk.myservice.service.MyServiceThreadPool
```

## 3.使用并发编程，开启子线程去并发请求接口，子线程使用线程池，使用自己写的FurtherTask

参考位置

```
com.zbk.myservice.service.FutureTaskZbk
com.zbk.myservice.service.MyServiceThreadPoolFTZbk
```

## 4.通过异步请求优化可接受的请求数

参考位置

```
com.zbk.myservice.controller.MyCallableController
```

