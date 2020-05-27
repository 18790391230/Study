package com.wym.mybatis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wym.mybatis.dao.a.IUserMapper;
import com.wym.mybatis.dao.b.IAccountMapper;
import com.wym.mybatis.model.Account;
import com.wym.mybatis.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author v_ymmwu
 */
@SpringBootTest(classes = MybatisApplication.class)
public class MybatisTest {


    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private IAccountMapper accountMapper;

    @Test
    public void test1() throws JsonProcessingException {
        User user = userMapper.selectByPrimaryKey(1);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(user));

        Account account = accountMapper.selectByPrimaryKey(1);
        System.out.println(objectMapper.writeValueAsString(account));
    }
}
