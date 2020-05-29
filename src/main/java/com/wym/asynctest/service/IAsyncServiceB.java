package com.wym.asynctest.service;

import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author v_ymmwu
 */
public interface IAsyncServiceB {

    void fooA();

    CompletableFuture<String> fooB();

    ListenableFuture<String> fooC();

    Future<String> fooD();
}
