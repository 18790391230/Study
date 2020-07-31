package com.wym.juc.task;

import java.util.concurrent.Semaphore;

/**
 *
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 7; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println("抢到车位了");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
            }).start();
        }
    }
}
