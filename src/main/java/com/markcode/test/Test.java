package com.markcode.test;

import com.markcode.spring.MarkApplicationContext;
import com.markcode.spring.AppConfig;

/**
 * @author luofan
 */
public class Test {
    public static void main(String[] args) {
        MarkApplicationContext markApplicationContext = new MarkApplicationContext(AppConfig.class);
        Object userService1 = markApplicationContext.getBean("userService");
        Object userService2 = markApplicationContext.getBean("userService");
        Object userService3 = markApplicationContext.getBean("userService");
        System.out.println(userService1);
        System.out.println(userService2);
        System.out.println(userService3);
    }
}
