package com.wym.design.simplefacotory;

public class OperationDiv implements Operation{
    @Override
    public double getResult(double numberA, double numberB) {
        return numberA / numberB;
    }
}
