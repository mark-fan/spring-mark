package com.markcode.test.service;

import com.markcode.spring.Autowired;
import com.markcode.spring.Component;

/**
 * @author luofan
 */
@Component("userService")
public class UserService {

    @Autowired
    private OrderService orderService;

    public void testOrderService(){
        System.out.println(orderService);
    }

}
