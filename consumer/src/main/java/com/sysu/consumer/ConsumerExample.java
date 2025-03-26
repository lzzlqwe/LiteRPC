package com.sysu.consumer;

import com.sysu.config.RpcConfig;
import com.sysu.model.User;
import com.sysu.proxy.ServiceProxyFactory;
import com.sysu.service.UserService;
import com.sysu.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("laizhizheng");
        //调用
        User newUser = userService.getUser(user);
        if(newUser != null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("user == null");
        }
        short number = userService.getNumber();
        System.out.println(number);
    }
}
