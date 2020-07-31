package com.wym;

import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * -Xms50M -Xmx50M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:gc.log
 */
public class OOMTest {


    public static void main(String[] args) throws Exception {

        new Thread(()->{
            List<Object> list = new ArrayList<>();
            while (true) {
                list.add(new Object());
                list.add(new Object());
                list.add(new Object());
                list.add(new Object());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
