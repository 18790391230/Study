package com.wym.asynctest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author v_ymmwu
 */
@EnableAsync
@EnableAspectJAutoProxy
@SpringBootApplication
@ComponentScan(basePackages = "com.wym.common.datasourceconfig", basePackageClasses = AsyncTestApplication.class)
public class AsyncTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncTestApplication.class, args);
    }
}
