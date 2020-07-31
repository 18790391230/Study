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
        s.intern();
        System.out.println(s == s2);


        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);
    }

    @Test
    void t2() {
        String s1 = new String("12") + new String("3");
        s1.intern();

        String s2 = "123";
        System.out.println(s1 == s2);

        String s3 = new String("1") + new String("1");
        String s4 = "11";
        s3.intern();
        System.out.println(s3 == s4);

        String s5 = new String("aa");
        s5 = s5.intern();

        String s6 = "aa";
        System.out.println(s5 == s6);
    }
}
