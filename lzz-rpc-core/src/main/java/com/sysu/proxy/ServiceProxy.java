package com.sysu.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.sysu.RpcApplication;
import com.sysu.config.RpcConfig;
import com.sysu.constant.ProtocolConstant;
import com.sysu.constant.RpcConstant;
import com.sysu.loadbalancer.LoadBalancer;
import com.sysu.loadbalancer.LoadBalancerFactory;
import com.sysu.model.RpcRequest;
import com.sysu.model.RpcResponse;
import com.sysu.model.ServiceMetaInfo;
import com.sysu.protocol.*;
import com.sysu.register.Registry;
import com.sysu.register.RegistryFactory;
import com.sysu.serializer.Serializer;
import com.sysu.serializer.SerializerFactory;
import com.sysu.serializer.impl.JDKSerializer;
import com.sysu.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理（JDK 动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造消息协议的请求体
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {

            // 从注册中心获取服务提供者地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig(); //获取全局rpc配置
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry()); //获取注册中心
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey()); //根据服务名获取服务提供者地址
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }

            //负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            System.out.println("负载均衡选择的服务器" + selectedServiceMetaInfo.getServiceAddress());

            // 发送 RPC 请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}
