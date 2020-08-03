package com.wym.error;

import java.util.ArrayList;
import java.util.List;

/**
 *-XX:MaxDirectMemorySize=5M -XX:+PrintGCDetails -Xms=10M -Xmx=10M
 *
 * Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
 */
public class OverheadLimitExceededTest {

    public static void main(String[] args) {


        int i = 0;
        List<String> list = new ArrayList<>();

        try {
            while (true) {
                list.add(String.valueOf(++i).intern());
            }
        } catch (Exception e) {
            System.out.println("i=====" + i);
            throw e;
        }
    }
}
