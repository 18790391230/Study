package com.wym.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 */
public class JdkProxy<T> implements InvocationHandler {


    public T createProxy(Class<?> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke方法被调用");
//        method.invoke(proxy, args);
        return "成功调用了invoke()";
    }
}
