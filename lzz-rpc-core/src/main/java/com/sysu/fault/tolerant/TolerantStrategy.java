package com.sysu.fault.tolerant;

import com.sysu.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 */
public interface TolerantStrategy {

    /**
     * 容错
     *
     * @param context 上下文，用于传递数据
     * @param e       异常
     * @return
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception;
}
