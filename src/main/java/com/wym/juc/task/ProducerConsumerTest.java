package com.wym.juc.task;

import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author v_ymmwu
 */
public class ProducerConsumerTest {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
        new Thread(consumer).start();

        Thread.sleep(10000);
        producer.stop();
        consumer.stop();
        System.out.println("\n结束了。。。。。");
    }

    static abstract class Parent{
        BlockingQueue<String> queue = null;
        volatile boolean stop = false;

        public void stop() {
            stop = true;
        }
    }
    static class Producer extends Parent implements Runnable{

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @SneakyThrows
        @Override
        public void run() {

            int i = 0;
            while (!stop) {
                Thread.sleep(1000);
                System.out.println("生产了：" + i);
                queue.offer(String.valueOf(i++));
            }
        }
    }

    static class Consumer extends Parent implements Runnable{

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (!stop) {
                try {
                    String rev = queue.poll(2, TimeUnit.SECONDS);
                    System.out.println("消费了：" + rev);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("异常结束");
                    break;
                }
            }
        }
    }
}
