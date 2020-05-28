package com.wym.mybatis.context;


public class ContextUtils {

    private static String environment = null;

    public static void setEnv(String env) {
        environment = env;
    }

    public static String getEnv() {
        return environment;
    }
}
