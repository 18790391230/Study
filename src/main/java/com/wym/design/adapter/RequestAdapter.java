package com.wym.design.adapter;

public interface RequestAdapter {


    boolean supports(Object handler);


    String handle(String request, Object handler);
}
