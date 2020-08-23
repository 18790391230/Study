package com.wym.error;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * -XX:MaxDirectMemorySize=40m -verbose:gc -XX:+PrintGCDetails -XX:+DisableExplicitGC
 */
public class TestDirectByteBuffer {

    public static void main(String[] args) {
        List<ByteBuffer> list = new ArrayList<ByteBuffer>();
        while(true) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1 * 1024 * 1024);
            //list.add(buffer);
        }
    }
}
