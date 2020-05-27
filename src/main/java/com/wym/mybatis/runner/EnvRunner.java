package com.wym.mybatis.runner;

import com.wym.mybatis.context.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author v_ymmwu
 */
@Component
public class EnvRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EnvRunner.class.getSimpleName());

    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        String env = environment.getProperty("spring.profiles.active");
        ContextUtils.setEnv(env);
        logger.info("env============================" + env);
    }
}
