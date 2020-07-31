package com.wym.asynctest.service.impl;

import com.wym.asynctest.service.IAsyncServiceB;
import com.wym.asynctest.util.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 */
@Slf4j
@Service
public class AsyncServiceB implements IAsyncServiceB {


    @Async
    @Override
    public void fooA() {
        ThreadUtils.threadSleep(3);
    }

    @Async
    @Override
    public CompletableFuture<String> fooB() {
        ThreadUtils.threadSleep(4);
        return CompletableFuture.supplyAsync(()->"测试CompletableFuture");
    }

    @Async
    @Override
    public ListenableFuture<String> fooC() {

        ListenableFuture<String> future = new AsyncResult<>("测试ListenableFuture");
        ThreadUtils.threadSleep(5);
        return future;
    }

    @Async
    @Override
    public Future<String> fooD() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ThreadUtils.threadSleep(2);
        Future<String> submit = executorService.submit(() -> "测试Future");
        return submit;
    }
}
