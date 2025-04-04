package com.sysu.consumer;

import com.sysu.config.RpcConfig;
import com.sysu.model.User;
import com.sysu.proxy.ServiceProxyFactory;
import com.sysu.service.UserService;
import com.sysu.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("lzz666");

        //远程调用
        User newUser = userService.getUser(user);
        if(newUser != null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("user == null");
        }
        System.out.println("-----------第一次远程调用---------------");
//
//        newUser = userService.getUser(user);
//        System.out.println(newUser.getName());
//        System.out.println("-----------第二次远程调用---------------");
//
//        newUser = userService.getUser(user);
//        System.out.println(newUser.getName());
//        System.out.println("-----------第三次远程调用---------------");


//        //测试Mock（配置文件application.properties配置rpc.mock=true）
//        //获取代理
//        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
//        User user = new User();
//        user.setName("laizhizheng");
//        //调用
//        User newUser = userService.getUser(user);
//        if(newUser != null){
//            System.out.println(newUser.getName());
//        }else{
//            System.out.println("user == null");
//        }
//        short number = userService.getNumber();
//        System.out.println(number);
    }
}
