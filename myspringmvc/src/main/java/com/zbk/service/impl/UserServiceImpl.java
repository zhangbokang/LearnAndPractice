package com.zbk.service.impl;

import com.zbk.annaotation.ZbkService;
import com.zbk.service.UserService;

@ZbkService("userService")
public class UserServiceImpl implements UserService {
    public String query(String name, String age) {
        return "name=" + name + ",age=" + age;
    }
}
