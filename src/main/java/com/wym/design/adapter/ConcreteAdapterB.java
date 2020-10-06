package com.wym.design.adapter;

public class ConcreteAdapterB implements RequestAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler.toString().equals("weixin");
    }

    @Override
    public String handle(String request, Object handler) {
        return "ConcreteAdapterB结果";
    }
}
