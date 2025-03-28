package com.sysu;


import com.sysu.config.RegistryConfig;
import com.sysu.config.RpcConfig;
import com.sysu.constant.RpcConstant;
import com.sysu.register.Registry;
import com.sysu.register.RegistryFactory;
import com.sysu.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC 框架应用
 * 相当于 holder，存放了项目全局用到的变量。双检锁单例模式实现
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig; //static，单例。volatile 保证了变量的可见性，防止指令重排序

    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());

        //注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
    }

    /**
     * 初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置，懒加载
     *
     * @return
     */
    public static RpcConfig getRpcConfig() {
        // 第一检：避免不必要的同步（提高性能）
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) { // 同步类锁，保证线程安全
                // 第二检：避免多线程下重复创建实例
                if (rpcConfig == null) {
                    init(); // 初始化单例实例
                }
            }
        }
        return rpcConfig;
    }
}

