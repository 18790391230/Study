### SpringMVC



#### 执行流程

整个流程都是在DispachServlet的doDispachServlet()方法中完成的。

1. 遍历handlerMappings，查找可以处理request的handler并返回（其实返回的是处理器执行器链）。
2. 遍历handlerAdapters，查找可以处理该handler的适配器。
3. 调用拦截器的**preHandle()**方法，如果有一个拦截器返回false，就跳过后面的拦截器并返回false，返回之前会根据索引逆序调用之前预处理成功的那些拦截器的afterCompletion()方法
4. 调用adapter的handler()方法，内部调用handler的处理方法并返回ModelAndView。
5. 通过request获取视图名
6. 逆序调用所有拦截器的**postHandle()**方法
7. 遍历viewResolvers通过视图名查找视图并返回视图对象。
8. 调用view的render渲染视图并响应给客户端。
9. 逆序调用拦截器的**afterCompletion()**方法





HandlerMapping与HandlerAdapter对应关系：

| HandlerMapping               | HandlerAdapter                 | 对应的Handler                          |
| ---------------------------- | ------------------------------ | -------------------------------------- |
| BeanNameUrlHandlerMapping    | SimpleControllerHandlerAdapter | 实现Controller接口的Bean               |
| RequestMappingHandlerMapping | RequestMappingHandlerAdapter   | 使用@Controller类的@RequestMapping方法 |
| BeanNameUrlHandlerMapping    | HttpRequestHandlerAdapter      | 实现HttpRequestHandler的Bean           |





#### 拦截器

| 方法              | 执行时机                                               | 是否一定会执行                                       |
| ----------------- | ------------------------------------------------------ | ---------------------------------------------------- |
| preHandle()       | 通过HandlerMapping获取handler之后，调用handler方法之前 | 否，如果前面的拦截器返回了false                      |
| postHandle()      | 调用handler方法之后，渲染视图之前                      | 否，如果前面的拦截器返回了false或hanlder出现异常     |
| afterCompletion() | 视图渲染完成后                                         | 否，如果前面的拦截器返回了false，不受handler异常影响 |







