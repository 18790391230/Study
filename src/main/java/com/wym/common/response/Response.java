package com.wym.common.response;

import io.swagger.annotations.ApiModel;


@ApiModel("请求响应结果")
public class Response<T> {

    public static final String CODE_OK = "200";
    public static final String CODE_ERROR = "5000";
    public static final String MSG_OK = "ok";
    public static final String MSG_ERROR = "error";


    private String code;

    private String msg;

    private String sign;

    private T data;

    public Response() {
        this.code = CODE_OK;
        this.msg = MSG_OK;
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Response<T> ok(T data, String sign) {
        Response<T> response = new Response<>();
        response.data = data;
        response.sign = sign;
        return response;
    }

    public static <T> Response<T> error(T data) {
        return error(data, MSG_ERROR);
    }

    private static <T> Response<T> error(T data, String msg) {
        Response<T> response = new Response<>();
        response.code = CODE_ERROR;
        response.msg = msg;
        response.data = data;
        return response;
    }

    public static <T> Response<T> error(Exception ex, T data) {
        return error(data, ex.getMessage());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
