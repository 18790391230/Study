package com.wym.error;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M -XX:+PrintGCDetails
 *
 * Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
 *
 * MetaSpace是方法区在hotspot中的实现，他与永久代最大的区别在于，他不位于虚拟机内存而是直接使用本地内存
 * class metadata（the virtual machines internal presentation of Java Class）元数据即Java类在虚拟机中内部的表现形式
 * Metaspace保存一下信息：
 * 1.虚拟机类加载信息
 * 2.常量池
 * 3.静态变量
 * 4.即时编译后的代码
 *
 * 不断创建类加载到元空间，超过元空间大小则溢出
 */
public class MetaspaceOOMTest {

    public static void main(String[] args) {

        int i = 0;
        try {
            while (true) {
                i++;
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(MetaspaceOOMTest.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(o, objects);
                    }
                });

                enhancer.create();
            }
        } catch (Exception e) {
            System.out.println("创建类的个数：" + i);
            e.printStackTrace();
        }
    }
}
