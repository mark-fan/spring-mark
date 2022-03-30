package com.markcode.test;

import com.markcode.spring.MarkApplicationContext;
import com.markcode.spring.AppConfig;
import com.markcode.test.service.UserService;

/**
 * @author luofan
 */
public class Test {
    public static void main(String[] args) {
        MarkApplicationContext markApplicationContext = new MarkApplicationContext(AppConfig.class);
        UserService userService = (UserService)markApplicationContext.getBean("userService");
        userService.testOrderService();
    }
}
