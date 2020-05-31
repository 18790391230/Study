package com.wym.juc.function;

import com.wym.common.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionTest {


    public static void main(String[] args) {

        //1.类名::静态方法名
        //2.实例名::实例方法名
        //3.类名::实例方法名
        //4.类名::new

        List<User> list = new ArrayList<>();
        User user = new User();
        user.setAge(11);

        User user2 = new User();
        user2.setAge(1);
        list.add(user);
        list.add(user2);

//        Optional<List<User>> optional = Optional.of(list);
        Optional<List<User>> optional = Optional.ofNullable(null);

        System.out.println(optional.map(e -> e.get(0)).orElse(null));
        System.out.println(optional.map(e -> e.get(0)).orElseGet(OptionTest::newUser));
    }
//
//    private static User get(Optional<List<User>> optional) {
//
//        return optional.map(e->e.get(0)).orElse(null);
//    }
//
//    private static User get2(Optional<List<User>> optional) {
//
//        return optional.map(e -> e.get(0)).orElseGet(OptionTest::newUser);
//    }

    private static User rtnTest() {
        Optional<List<User>> optional = Optional.ofNullable(null);

        System.out.println(optional.map(e -> e.get(0)).orElse(null));
        System.out.println(optional.map(e -> e.get(0)).orElseGet(OptionTest::newUser));

        return optional.map(e -> e.get(0)).orElseGet(null);
    }

    private static User newUser() {
        return new User();
    }

}
