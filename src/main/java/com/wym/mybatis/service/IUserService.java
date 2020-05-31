package com.wym.mybatis.service;

import com.wym.common.model.User;

import java.util.List;


public interface IUserService {

    List<User> getList();

    void insert(User user);
}
