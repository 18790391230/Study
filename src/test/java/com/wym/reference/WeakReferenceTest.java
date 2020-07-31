package com.wym.reference;

import java.lang.ref.WeakReference;

/**
 * 弱引用
 * 只要执行gc，就会被清理
 */
public class WeakReferenceTest {


    public static void main(String[] args) {

        Object o = new Object();

        WeakReference<Object> weakReference = new WeakReference<>(o);

        System.out.println(o);
        System.out.println(weakReference.get());

        o = null;
        System.gc();

        System.out.println(o);
        System.out.println(weakReference.get());

    }
}
