package com.wym.error;

import java.nio.ByteBuffer;

/**
 * -Xms10M -Xmx10M -XX:MaxDirectMemorySize=5M -XX:+PrintGCDetails
 *
 * 物理内存申请失败
 *
 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 */
public class DirectBufferMemoryTest {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("配置的DirectMemorySize是：" + sun.misc.VM.maxDirectMemory() / 1024.0 / 1024 + "MB");
        Thread.sleep(1000);
        ByteBuffer allocate = ByteBuffer.allocateDirect(6 * 1024 * 1024);
    }
}
