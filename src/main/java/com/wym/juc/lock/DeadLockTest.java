package com.wym.juc.lock;

public class DeadLockTest {

    public static void main(String[] args) {

        Object lockA = new Object();
        Object lockB = new Object();

        new Thread(()->{

            synchronized (lockA) {

                System.out.println(Thread.currentThread().getName() + "获取到A锁");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lockB) {
                    System.out.println(Thread.currentThread().getName() + "获取到B锁");
                }
            }
        }, "Thread1").start();

        new Thread(()->{

            synchronized (lockB) {

                System.out.println(Thread.currentThread().getName() + "获取到B锁");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lockA) {
                    System.out.println(Thread.currentThread().getName() + "获取到A锁");
                }
            }
        }, "Thread2").start();

    }
}
