package com.wym.design.simplefacotory;

public class OperationSub implements Operation {
    @Override
    public double getResult(double numberA, double numberB) {
        return numberA - numberB;
    }
}
