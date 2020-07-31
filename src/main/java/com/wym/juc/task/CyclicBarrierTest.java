package com.wym.juc.task;

import java.text.MessageFormat;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 */
public class CyclicBarrierTest {


    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(7, () -> System.out.println("召唤神龙"));

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            new Thread(()->{
                String s = MessageFormat.format("找到第{0}个龙珠", finalI + 1);
                System.out.println(s);
                try {
                    cb.await();
                    System.out.println("是时候继续了...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}
