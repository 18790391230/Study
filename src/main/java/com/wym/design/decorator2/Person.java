package com.wym.design.decorator2;

public class Person {


    public Person() {
    }

    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("name=" + name);
    }
}
