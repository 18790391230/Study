package com.wym.string;

import org.junit.jupiter.api.Test;

/**
 *
 */
public class StringTest {

    @Test
    void t1() {


        String s = new String("1");
        String s2 = "1";
        s = s.intern();  //如果常量池中包含了s，则直接返回false，否则这个对象被添加到常量池并返回引用
        System.out.println(s == s2);  //true


        String s3 = new String("1") + new String("1");
//        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);  //false
    }

    @Test
    void t2() {
        String s1 = new String("12") + new String("3");  // + 号是通过StringBuilder或StringBuffer类及append方法实现的,最后调用toString赋值

        String s2 = "123";
        System.out.println(s1 == s2);  //false

        String s3 = new String("1") + new String("1");
        String s4 = "11";
        System.out.println(s3.intern() == s4);  //true
    }

    @Test
    void t3() {

        //下面语句会创建几个对象？如果常量池中已经存在abc，则创建一个，否则，创建2个(先将"abc"创建在常量池中，然后拷贝到堆中，详情看new String(str)方法解释)
        String str = new String("abc");
        System.out.println(str);

        //会创建多少个对象?  ！！！！注意："A" + "B"会被编译器优化为"AB"，于是，和上面的abc一样，可能创建一个或2个对象
        String str1 = new String("A" + "B");  //
        System.out.println(str1);

        //会创建多少个对象？首先，如果abc不存在，则new String("abc")需要创建2个对象，由于 + "abc"是通过StringBuilder及其toString()方法完成的，所以又需要创建2个对象
        String str2 = new String("abc") + "abc";
        System.out.println(str2);
    }

    @Test
    void t4() {

        String str1= "abc";
        String str2= new String("abc");
        String str3= str2.intern();
        System.out.println(str1 == str2); //false
        System.out.println(str2 == str3); //false
        System.out.println(str1 == str3); //true
    }

    @Test
    void t5() {

        Boolean b = true;
        Boolean b2 = true;
        System.out.println(b == b2);

    }
}
