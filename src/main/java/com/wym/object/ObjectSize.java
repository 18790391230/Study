package com.wym.object;

import org.openjdk.jol.info.ClassLayout;

public class ObjectSize {

    public static void main(String[] args) {

        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        //执行结果：16,前8个字节是markword，后面的4个字节是class pointer，开启指针压缩则为4字节（默认开启），不开启则为8
        // java.lang.Object object internals:
        // OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
        //      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
        //      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
        //      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
        //     12     4        (loss due to the next object alignment)
        //Instance size: 16 bytes
        //Space losses: 0 bytes internal + 4 bytes external = 4 bytes total//
    }
}
