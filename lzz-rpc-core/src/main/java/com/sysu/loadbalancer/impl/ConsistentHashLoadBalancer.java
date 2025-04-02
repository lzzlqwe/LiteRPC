package com.sysu.loadbalancer.impl;

import com.sysu.loadbalancer.LoadBalancer;
import com.sysu.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 一致性哈希环，存放各个物理节点生成的虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 每个物理节点对应的虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty()){
            return null;
        }

        // 构建一致性哈希环
        for(ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList){
            for(int i = 0; i < VIRTUAL_NODE_NUM; i++){
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "_" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        // 根据请求参数计算哈希值
        int hash = getHash(requestParams);
        
        // 选择最接近且大于等于调用请求 hash 值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if(entry == null){
            // 如果没有大于等于调用请求 hash 值的虚拟节点，则返回环首部的节点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * Hash算法计算哈希值（可重新实现）
     * @param key
     * @return
     */
    private int getHash(Object key){
        return key.hashCode();
    }
}
