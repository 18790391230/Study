package com.wym.mybatis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wym.mybatis.dao.b.IAccountMapper;
import com.wym.common.model.User;
import com.wym.mybatis.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;


@SpringBootTest
public class MybatisTest {


    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountMapper accountMapper;

    @Test
    public void test1() throws JsonProcessingException {
        List<User> list = userService.getList();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(list));
    }

    @Test
    public void transactionTest() throws IOException {
        System.out.println(MessageFormat.format("insert thread id:{0}", Thread.currentThread().getId()));
        User user = new User();
        user.setAge(12);
        user.setName("李四");
        userService.insert(user);
        System.in.read();
//        Assert.isTrue(i == 1, "事务测试出错！");
    }
}
