package com.wym.mybatis.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * handlerMapping使用的是BeanNameUrlHandlerMapping
 *
 * mappedHandler.getHandler() => HttpRequestHandlerController
 *
 * ha => HttpRequestHandlerAdapter
 */
@Component("/test2")
public class HttpRequestHandlerController implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HttpRequestHandlerController()..........");
    }
}
