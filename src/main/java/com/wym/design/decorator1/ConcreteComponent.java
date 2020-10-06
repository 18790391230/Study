package com.wym.design.decorator1;

public class ConcreteComponent implements Component {

    @Override
    public void operation() {
        System.out.println("ConcreteComponent operation...");
    }
}
