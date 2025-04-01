package com.sysu.provider;

import com.sysu.RpcApplication;
import com.sysu.config.RegistryConfig;
import com.sysu.config.RpcConfig;
import com.sysu.model.ServiceMetaInfo;
import com.sysu.register.LocalRegister;
import com.sysu.register.Registry;
import com.sysu.register.RegistryFactory;
import com.sysu.server.HttpServer;
import com.sysu.server.impl.VertxHttpServer;
import com.sysu.server.tcp.VertxTcpServer;
import com.sysu.service.UserService;
import com.sysu.service.impl.UserServiceImpl;

public class ProviderExample {
    public static void main(String[] args) {
        //RPC 框架初始化
        RpcApplication.init();

        // 本地注册
        String serviceName = UserService.class.getName(); //获取服务名
        LocalRegister.register(serviceName, UserServiceImpl.class);

        // 注册服务到服务中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry()); //获取注册中心
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 web 服务
        // HttpServer httpServer = new VertxHttpServer();
        // httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

        // 启动 web 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
