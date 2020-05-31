package com.wym.juc.function;

import com.google.common.collect.Lists;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionTest {


//    public static void main(String[] args) {
////        Consumer
//
//        Function<String, String> function = String::toLowerCase;
//
//        List<String> list = new ArrayList<>();
//        list.add("ABC");
//        list.add("EFG");
//        List<String> collect = list.stream().map(function).collect(Collectors.toList());
//
//        collect.forEach(System.out::println);
//
//    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
//1.
//        Function<Integer, String> f1 = e -> e + "111";
//        Function<String, String> f2 = e -> e + "222";

//        List<String> collect = list.stream().map(f1.andThen(f2)).collect(Collectors.toList());

        //2.
//        Function<String, String> f1 = e -> e + "111";
        Function<String, String> f1 = FunctionTest::ff1;
        Function<Integer, String> f2 = e -> e + "222";
        List<String> collect = list.stream().map(f1.compose(f2)).collect(Collectors.toList());

        list.stream().map(f1.compose(e -> e + "222").compose(e -> e + "111")).collect(Collectors.toList());

        for (String s : collect) {
            System.out.println(s);
        }
        list.forEach(e->{
            String s = f1(e, a -> a + "111", b -> b + "222");
            System.out.println(s);
        });
    }

    private static String f1(int val, Function<Integer, String> beforeFunction, Function<String, String> afterFunction) {
        return beforeFunction.andThen(afterFunction).apply(val);
    }

    private static String ff1(String s) {
        return null;
    }
}
