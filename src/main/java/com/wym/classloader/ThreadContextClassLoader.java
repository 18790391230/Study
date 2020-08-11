package com.wym.classloader;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * 破坏双亲委派模型
 * 通过Thread.currentThread().setContextClassLoader()将AppClassloader设置为线程上下文类加载器
 * 通过Thread.currentThread().getContextClassLoader()获取AppClassloader加载classpath路径
 *
 *
 * 在ServiceLoader中调用
 *      public static <S> ServiceLoader<S> load(Class<S> service) {
 *         ClassLoader cl = Thread.currentThread().getContextClassLoader();
 *         return ServiceLoader.load(service, cl);
 *     }
 *     使用AppClassloader加载classpath下SPI的实现
 */
public class ThreadContextClassLoader {


    public static void main(String[] args) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            //都是由 sun.misc.Launcher$AppClassLoader加载
            System.out.println(driver.getClass() + "       " + driver.getClass().getClassLoader());
        }

        //classLoader=null，因为属于rt.jar, 有BootstrapClassLoader加载
        System.out.println(DriverManager.class + "   " + DriverManager.class.getClassLoader());
    }
}
