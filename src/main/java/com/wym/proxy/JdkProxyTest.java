package com.wym.proxy;

/**
 * jdk动态代理，不需要实现类
 */
public class JdkProxyTest {


    public static void main(String[] args) {
        JdkProxy<InterfaceTest> jdkProxy = new JdkProxy<>();
        InterfaceTest inter = jdkProxy.createProxy(InterfaceTest.class);

        System.out.println(inter.execute());

    }
}
