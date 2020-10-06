package com.wym.design.simplefacotory;

public class OperationFactory {


    public static Operation createOperation(String oper) {
        switch (oper) {
            case "+":
                return new OperationAdd();
            case "-":
                return new OperationSub();
            case "*":
                return new OperationMulti();
            case "/":
                return new OperationDiv();
        }
        throw new RuntimeException("操作符非法");
    }
}
