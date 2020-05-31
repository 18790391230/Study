package com.wym.mybatis.service.impl;

import com.wym.mybatis.dao.a.IUserMapper;
import com.wym.common.model.User;
import com.wym.mybatis.service.IUserService;
import com.wym.mybatis.vo.Pages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    @Override
    public List<User> getList() {
        Pages.startPage(1, 10);
        return userMapper.getList();
    }

    @Async
    @Transactional(transactionManager = "managerA")
    @Override
    public void insert(User user) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
        log.info("insert thread id:{}", Thread.currentThread().getId());
        int effectRow = userMapper.insertSelective(user);
        int i = 10 / 0;
    }
}
