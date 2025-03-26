package com.sysu.consumer;

import com.sysu.config.RpcConfig;
import com.sysu.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        //测试配置文件读取
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpcConfig);
    }
}
