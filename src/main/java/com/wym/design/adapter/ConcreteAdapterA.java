package com.wym.design.adapter;

public class ConcreteAdapterA implements RequestAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler.toString().equals("支付宝");
    }

    @Override
    public String handle(String request, Object handler) {
        return "ConcreteAdapterA结果";
    }
}
