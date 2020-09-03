package com.wym.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.text.MessageFormat;

/**
 *
 */
public class HelloWorldTest {


    public static void main(String[] args) {
        //构建KieServices
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();

        //获取kmodule.xml中配置的名为ksession-rule的session，默认为有状态的
        KieSession kieSession = kieContainer.newKieSession("ksession-rule");
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setType("hello");

        kieSession.insert(helloWorld);
        int count = kieSession.fireAllRules();

        System.out.println(MessageFormat.format("命中了{0}条规则", count));
        System.out.println("折扣为" + helloWorld.getDiscount());
    }
}
