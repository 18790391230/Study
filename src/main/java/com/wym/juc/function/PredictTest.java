package com.wym.juc.function;

import java.util.List;
import java.util.function.Predicate;

public class PredictTest {


    public static void main(String[] args) {

    }

    private static void conditionFilter(List<Integer> list, Predicate<Integer> predicate) {
        list.forEach(e -> {
            if (predicate.test(e)) {
                System.out.println(e);
            }
        });
    }
}
