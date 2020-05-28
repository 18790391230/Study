package com.wym.mybatis.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = "com.wym.mybatis.dao.b", sqlSessionTemplateRef = "sqlSessionTemplateB")
public class DataSourceBConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mybatis.druid.b")
    public DataSource dataSourceB() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryB() {

        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSourceB());
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mapper" +
                    "/b/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateB() {
        return new SqlSessionTemplate(sqlSessionFactoryB());
    }

    @Bean
    public DataSourceTransactionManager managerA() {
        return new DataSourceTransactionManager(dataSourceB());
    }
}
