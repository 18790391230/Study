package com.wym.mybatis.service.impl;

import com.wym.mybatis.dao.a.IUserMapper;
import com.wym.mybatis.model.User;
import com.wym.mybatis.service.IUserService;
import com.wym.mybatis.vo.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    @Override
    public List<User> getList() {
        Pages.startPage(1, 10);
        return userMapper.getList();
    }
}
