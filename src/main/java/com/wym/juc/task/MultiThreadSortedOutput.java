package com.wym.juc.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class MultiThreadSortedOutput {

    static volatile int ii = 1;

    public static void main(String[] args) {

        Lock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        new Thread(() -> {
            sleep(3);
            int i = 0;
            try {
                lock.lock();
                latch.countDown();
                while (i++ < 10) {
                    System.out.println("线程A输出：" + ii++);
                    System.out.println("线程A输出：" + ii++);
                    System.out.println("线程A输出：" + ii++);
                    conditionB.signal(); //唤醒线程B，条件队列 -> 同步队列
                    if (i == 10) {
                        break;
                    }
                    conditionA.await();  //释放锁，线程B会抢到锁
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "thread-A").start();

        new Thread(() -> {
            sleep(2);
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 0;

            try {
                lock.lock();
                latch2.countDown();
                while (i++ < 10) {
                    System.out.println("线程B输出：" + ii++);
                    System.out.println("线程B输出：" + ii++);
                    System.out.println("线程B输出：" + ii++);
                    conditionC.signal();
                    if (i == 10) {
                        break;
                    }
                    conditionB.await();  //释放锁
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "thread-B").start();

        new Thread(() -> {
            sleep(1);
            int i = 0;
            try {
                latch2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                lock.lock();
                while (i++ < 10) {
                    System.out.println("线程C输出：" + ii++);
                    System.out.println("线程C输出：" + ii++);
                    System.out.println("线程C输出：" + ii++);
                    conditionA.signal();
                    if (i == 10) {
                        break;
                    }
                    conditionC.await();  //释放锁
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "thread-C").start();
    }

    static void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
