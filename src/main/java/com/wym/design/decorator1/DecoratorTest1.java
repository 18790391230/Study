package com.wym.design.decorator1;

public class DecoratorTest1 {

    public static void main(String[] args) {

        ConcreteComponent cc = new ConcreteComponent();
        ConcreteDecoratorA ca = new ConcreteDecoratorA(cc);
        ConcreteDecoratorB cb = new ConcreteDecoratorB(ca);

        cb.operation();

    }
}
