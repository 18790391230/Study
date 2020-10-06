package com.wym.design.decorator2;

public class DecoratorTest2 {


    public static void main(String[] args) {
        Person p = new Person("张三");

        BigTrouser bigTrouser = new BigTrouser();
        TShirts tShirts = new TShirts();
        Sneakers sneakers = new Sneakers();
        bigTrouser.decorate(p);
        tShirts.decorate(bigTrouser);
        sneakers.decorate(tShirts);

        sneakers.show();
    }
}
