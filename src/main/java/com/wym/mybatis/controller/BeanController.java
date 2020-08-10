package com.wym.mybatis.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1. BeanController implements Controller的情况
 * handlerMapping使用的是BeanNameUrlHandlerMapping
 * mappedHandler.getHandler() => BaseController
 *
 * HandlerAdapter ha => SimpleControllerHandlerAdapter  (内部调用handleRequest()方法)
 *
 * 2. 使用@Controller的情况
 * handlerMapping使用的是RequestMappingHandlerMapping
 * mappedHandler.getHandler() => 加了@RequestMapping的方法
 *
 * HandlerAdapter ha => RequestMappingHandlerAdapter
 *
 * 参考DispatchServlet.properties
 */
@Component("/test")
public class BeanController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("BeanController handleRequest()...");
        return null;
    }
}
