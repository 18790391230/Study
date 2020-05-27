package com.wym.mybatis.runner;

import com.wym.mybatis.context.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author v_ymmwu
 */
@Component
public class EnvRunner implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        String env = environment.getProperty("spring.profiles.active");
        ContextUtils.setEnv(env);
        System.out.println("env============================" + env);
    }
}
