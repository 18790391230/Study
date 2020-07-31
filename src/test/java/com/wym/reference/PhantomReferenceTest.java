package com.wym.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 *
 */
public class PhantomReferenceTest {

    public static void main(String[] args) throws InterruptedException {


        Object o = new PhantomReferenceTest();
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        PhantomReference<Object> phantomReference = new PhantomReference<>(o, queue);

        System.out.println(o);
        System.out.println(phantomReference.get());  //null
        System.out.println(queue.poll());

        System.out.println("========================");

        o = null;
        System.gc();

        System.out.println(o);
        System.out.println(phantomReference.get());
        System.out.println(queue.poll());

        Thread.sleep(1);
        System.out.println("最后了....");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("我真的被回收了......");
    }
}
