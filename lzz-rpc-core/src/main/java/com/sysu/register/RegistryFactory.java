package com.sysu.register;

import com.sysu.register.impl.EtcdRegistry;
import com.sysu.spi.SpiLoader;

/**
 * 注册中心工厂（用于获取注册中心对象）
 */
public class RegistryFactory {

    /**
     * 使用静态代码块，在工厂首次加载时，就会调用 Spiloader 的 load 方法加载注册中心的所有实现类，
     * 之后就可以通过调用 getlnstance 方法获取指定的实现类对象了。
     */
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }

}

