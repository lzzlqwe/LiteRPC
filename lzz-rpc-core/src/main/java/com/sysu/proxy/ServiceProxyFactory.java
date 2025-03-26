package com.sysu.proxy;

import com.sysu.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂（工厂模式，用于创建服务的代理对象）
 */
public class ServiceProxyFactory {

    /**
     * 根据服务类创建一个代理对象
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if(RpcApplication.getRpcConfig().isMock()){ //如果开启了mock配置
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    public static <T> T getMockProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}

