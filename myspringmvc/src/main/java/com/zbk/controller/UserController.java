package com.zbk.controller;

import com.zbk.annaotation.ZbkAutowired;
import com.zbk.annaotation.ZbkController;
import com.zbk.annaotation.ZbkRequestMapping;
import com.zbk.annaotation.ZbkRequestParam;
import com.zbk.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ZbkController
@ZbkRequestMapping("/user")
public class UserController {

    @ZbkAutowired("userService")
    private UserService userService;

    @ZbkRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @ZbkRequestParam("name") String name, @ZbkRequestParam("age") String age) {
        try {
            PrintWriter printWriter = response.getWriter();
            String result = userService.query(name, age);
            printWriter.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
