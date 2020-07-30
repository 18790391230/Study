package com.wym.juc.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author v_ymmwu
 */
public class FutureTaskTest {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> task = new FutureTask<>(() -> "123");

        new Thread(task).start();
        System.out.println(task.get());

        new Thread(task).start();  //任务已经结束了，不会再执行了
        System.out.println(task.get());

    }

}
