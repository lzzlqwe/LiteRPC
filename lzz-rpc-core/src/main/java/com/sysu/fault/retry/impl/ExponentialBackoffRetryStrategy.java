package com.sysu.fault.retry.impl;

import com.github.rholder.retry.*;
import com.sysu.fault.retry.RetryStrategy;
import com.sysu.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 指数退避 - 重试策略
 */
@Slf4j
public class ExponentialBackoffRetryStrategy implements RetryStrategy {
    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                //第一个参数是1000ms，表示初始等待，每次翻倍
                .withWaitStrategy(WaitStrategies.exponentialWait(1000L, 10L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                .withRetryListener(new RetryListener() {
                    @Override
                    public void onRetry(Attempt attempt) {
                        log.info("重试次数 {}", attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
