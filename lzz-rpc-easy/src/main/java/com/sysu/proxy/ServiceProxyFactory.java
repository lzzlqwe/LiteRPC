package com.sysu.proxy;

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
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}

