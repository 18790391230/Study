package com.wym.juc.notify;

import java.util.concurrent.CountDownLatch;

public class WaitAndNotify {


    public static void main(String[] args) {

        Object lock = new Object();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(()->{
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 2; i <= 100; i+=2) {
                synchronized (lock) {

                    System.out.println("我是" + Thread.currentThread().getName() + " i=" + i);
                    try {
                        lock.notify();
                        if(i != 100) {
                            lock.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "threadB").start();

        new Thread(()->{

            for (int i = 1; i < 100; i+=2) {
                synchronized (lock) {
                    if (countDownLatch.getCount() > 0) {
                        countDownLatch.countDown();
                    }
                    System.out.println("我是" + Thread.currentThread().getName() + " i=" + i);
                    try {
                        lock.notify();
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "threadA").start();

    }
}
