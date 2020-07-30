package com.wym.juc.task;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author v_ymmwu
 */
public class CountDownLatchTest {


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(10);
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println("-1");
                try {
                    Thread.sleep(r.nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cdl.countDown();

            }).start();

        }
        cdl.await();

        System.out.println("结束啦.....");

    }

}
