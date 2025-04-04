package com.sysu.fault.tolerant.impl;

import com.sysu.fault.tolerant.TolerantStrategy;
import com.sysu.model.RpcRequest;
import com.sysu.model.RpcResponse;
import com.sysu.model.ServiceMetaInfo;
import com.sysu.server.tcp.VertxTcpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 转移到其他服务节点 - 容错策略
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("故障转移异常", e);

        // context里存储request,selectServiceMetaInfoList
        List<ServiceMetaInfo> serviceMetaInfoList = (List<ServiceMetaInfo>) context.getOrDefault("serviceMetaInfoList", new ArrayList<>());
        if (serviceMetaInfoList.isEmpty()) {
            throw new RuntimeException("暂无可用服务", e);
        }
        RpcRequest rpcRequest = (RpcRequest) context.getOrDefault("rpcRequest", null);
        try {
            // 每个服务都试一次
            return VertxTcpClient.doRequest(rpcRequest, serviceMetaInfoList.get(0));
        } catch (Exception err) {
            serviceMetaInfoList.remove(0);
            context.put("serviceMetaInfoList", serviceMetaInfoList);
            return doTolerant(context, e);
        }
    }
}
