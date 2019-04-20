package com.zbk.otherservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OtherServiceController {
    @RequestMapping("/s1")
    public String service1() throws InterruptedException {
        Thread.sleep(1000L);
        return "service1";
    }

    @RequestMapping("/s2")
    public String service2() throws InterruptedException {
        Thread.sleep(2000L);
        return "service2";
    }
}
