package com.wym.juc.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class CompletableFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> batchReqFuture = CompletableFuture.supplyAsync(()->{
            try {
                System.out.println("推荐批量接口开始调用");
                Thread.sleep(1000);
                System.out.println("推荐批量接口调用结束");
            } catch (InterruptedException e) {}
            return "推荐批量接口调用成功";
        });

        CompletableFuture invokeFengchaoFuture = CompletableFuture.runAsync(() -> {
            try {
                System.out.println("开始调用蜂巢");
                Thread.sleep(1000);
                System.out.println("结束蜂巢结束");
            } catch (InterruptedException e) {
            }
            System.out.println("蜂巢接口调用成功");
        });

        String batchReqResult = batchReqFuture.get();
        System.out.println("开始使用推荐接口结果进行rerank");

        invokeFengchaoFuture.get();

        System.out.println("执行planrerank");

        System.out.println("执行结束");
    }
}
