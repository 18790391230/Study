package com.wym.asynctest.service.impl;

import com.wym.asynctest.service.IAsyncServiceA;
import com.wym.asynctest.service.IAsyncServiceB;
import com.wym.asynctest.util.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

/**
 *
 */
@Slf4j
@Service
public class AsyncServiceA implements IAsyncServiceA {

    @Autowired
    private IAsyncServiceB asyncServiceB;

    @Override
    public void foo1() {
        asyncServiceB.fooA();
    }

    @Override
    public void foo2() {
        CompletableFuture<String> completableFuture = asyncServiceB.fooB();
        ThreadUtils.threadSleep(2);
        log.info("foo2做了一些事");
        completableFuture.thenAccept((s) -> log.info("completableFuture执行完成，结果是：{}", s));

    }

    @Override
    public void foo3() {
        ListenableFuture<String> listenableFuture = asyncServiceB.fooC();
    }

    @Override
    public void foo4() {
        asyncServiceB.fooD();
    }
}
