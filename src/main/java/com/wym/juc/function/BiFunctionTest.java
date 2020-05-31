package com.wym.juc.function;

import com.wym.common.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class BiFunctionTest {

    public static void main(String[] args) {

        List<User> list = new ArrayList<>();
        User user = new User();
        user.setAge(11);

        User user2 = new User();
        user2.setAge(1);
        list.add(user);
        list.add(user2);

        BiFunction<Integer, List<User>, List<User>> biFunction = (age, ls) ->
                list.stream()
                        .filter(e -> e.getAge() > 10)
                        .collect(Collectors.toList());

        List<User> apply = biFunction.apply(10, list);

        for (User u : apply) {
            System.out.println(u.getAge());
        }
    }


}
