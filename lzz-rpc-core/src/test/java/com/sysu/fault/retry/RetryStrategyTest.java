package com.sysu.fault.retry;

import com.sysu.fault.retry.impl.ExponentialBackoffRetryStrategy;
import com.sysu.fault.retry.impl.FixedIntervalRetryStrategy;
import com.sysu.fault.retry.impl.NoRetryStrategy;
import com.sysu.model.RpcResponse;
import org.junit.Test;

/**
 * 重试策略测试
 */
public class RetryStrategyTest {

    RetryStrategy retryStrategy = new ExponentialBackoffRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
