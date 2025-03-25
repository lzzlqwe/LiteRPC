package com.sysu.provider;

import com.sysu.register.LocalRegister;
import com.sysu.server.HttpServer;
import com.sysu.server.impl.VertxHttpServer;
import com.sysu.service.UserService;
import com.sysu.service.impl.UserServiceImpl;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 本地注册
        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}

