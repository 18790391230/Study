package com.wym.mybatis;

import com.wym.sentinel.SentinelAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {"com.wym.common.datasourceconfig"}, basePackageClasses = MybatisApplication.class)
public class MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }

    @Bean
    public SentinelAspect sentinelAspect() {
        return new SentinelAspect();
    }
}
