package com.wym.asynctest.util;

/**
 * @author v_ymmwu
 */
public class ThreadUtils {

    public static void threadSleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
