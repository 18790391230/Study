package com.wym.mybatis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wym.mybatis.dao.a.IUserMapper;
import com.wym.mybatis.dao.b.IAccountMapper;
import com.wym.mybatis.model.Account;
import com.wym.mybatis.model.User;
import com.wym.mybatis.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author v_ymmwu
 */
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
}
