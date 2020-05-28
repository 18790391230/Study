package com.wym.mybatis.interceptor;

import com.wym.mybatis.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;


@ControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class.getSimpleName());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<String> errorHandler(Exception ex) {

        String code = null, msg = null;
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            code = "405";
            msg = MessageFormat.format("不支持当前请求方法，请使用[{}]方法请求",
                    Arrays.toString(((HttpRequestMethodNotSupportedException) ex).getSupportedMethods()));
        } else if (ex instanceof MethodArgumentNotValidException) {
            code = "400";
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            StringBuilder sb = new StringBuilder();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError error : allErrors) {
                sb.append(error.getDefaultMessage()).append("\n");
            }
            msg = sb.toString();
        } else {
            code = "500";
            msg = "未知错误";
        }
        Response<String> response = new Response<>();
        response.setCode(code);
        response.setMsg(msg);
        logger.error("发生错误!", ex);

        return response;
    }
}
