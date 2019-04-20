package com.zbk.otherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 提供返回结果和暂停几秒模拟慢请求接口
 *
 * @author 张卜亢
 * @date 2019.04.21 00:47:59
 */
@SpringBootApplication
public class OtherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtherServiceApplication.class, args);
    }

}
