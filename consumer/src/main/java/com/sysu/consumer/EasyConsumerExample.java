package com.sysu.consumer;


import com.sysu.model.User;
import com.sysu.proxy.ServiceProxyFactory;
import com.sysu.service.UserService;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // 动态代理，为服务接口创建代理对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("laizhizheng");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}

