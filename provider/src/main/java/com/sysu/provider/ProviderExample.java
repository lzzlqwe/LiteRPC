package com.sysu.provider;

import com.sysu.RpcApplication;
import com.sysu.register.LocalRegister;
import com.sysu.server.HttpServer;
import com.sysu.server.impl.VertxHttpServer;
import com.sysu.service.UserService;
import com.sysu.service.impl.UserServiceImpl;

public class ProviderExample {
    public static void main(String[] args) {
        //RPC 框架初始化
        RpcApplication.init();

        // 本地注册
        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
