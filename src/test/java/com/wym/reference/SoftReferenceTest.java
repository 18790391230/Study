package com.wym.reference;

import java.lang.ref.SoftReference;

/**
 * 软引用
 * 如果堆空间够用，就不回收
 * 适用场景：缓存   Map<String, SoftReference<Bitmap>> map = new ...
 * -Xms10M -Xmx10M
 */
public class SoftReferenceTest {


    public static void main(String[] args) {
//        spaceEnough();

        spaceNotEnough();
    }

    private static void spaceEnough() {
        Object o = new Object();
        SoftReference<Object> softReference = new SoftReference<>(o);
        System.out.println(o);
        System.out.println(softReference.get());

        o = null;
        System.gc();

        System.out.println("再次o:" + o);
        System.out.println("再次 softReference.get():" + softReference.get());
    }

    private static void spaceNotEnough() {
        Object o = new Object();
        SoftReference<Object> softReference = new SoftReference<>(o);
        System.out.println(o);
        System.out.println(softReference.get());

        o = null;
        try {
            byte[] bs = new byte[10 * 1024 * 1024];
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("再次o:" + o);
            System.out.println("再次 softReference.get():" + softReference.get());
        }

    }
}
