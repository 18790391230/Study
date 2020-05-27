package com.wym.mybatis.context;

/**
 * @author v_ymmwu
 */
public class ContextUtils {

    private static String environment = null;

    public static void setEnv(String env) {
        environment = env;
    }

    public static String getEnv() {
        return environment;
    }
}
